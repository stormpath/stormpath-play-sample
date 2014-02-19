/*
 * Copyright 2013 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import com.stormpath.sdk.client.ClientBuilder
import models.User
import collection.JavaConversions._
import com.stormpath.sdk.directory.CustomData
import play.api.Play.current
import com.stormpath.scala.service.StormpathAuthenticationService
import context.StormpathExecutionContext
import scala.concurrent.{TimeoutException, Future, Await}
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit

/**
 * Manager encapsulating and providing all actual Stormpath operations being executed in this sample application.
 * Actual <a href="http://docs.stormpath.com/java/quickstart/#authenticate-an-account">Stormpath authentication</a> and
 * <a href="http://docs.stormpath.com/java/product-guide/#custom-data">Custom Data</a> operations
 * (i.e. retrieve, delete, insert) are executed here. These operations rely on the
 * <a href="https://github.com/stormpath/stormpath-sdk-java">Stormpath Java SDK</a> to be fulfilled.
 *
 */
object StormpathManager {

  private val path = util.Properties.envOrNone("HOME").get + "/.stormpath/apiKey.properties"
  private val client = new ClientBuilder().setApiKeyFileLocation(path).build()
  private val config = play.api.Play.configuration
  private val applicationRestUrl = config.getString("applicationRestUrl").getOrElse("")
  private val service = new StormpathAuthenticationService(client, applicationRestUrl)
  private implicit val executionContext = StormpathExecutionContext.executionContext
  private val authenticationTimeout : Long = config.getLong("authenticationTimeout").getOrElse(30)

  /**
   * If the authentication is successful, a {@link User} instance is created containing the user email, his full name,
   * the username and his custom data href.
   *
   * @param email the email of the user being authenticated against Stormpath.
   * @param password the password of the user being authenticated against Stormpath.
   * @return an `Option` containing the logged user information if the authentication succeeded, an `Option[None]` otherwise
   */
  def authenticate(email: String, password: String) : Option[User] = {

    val accountFuture = service.doAuthenticate(email, password)

    try {
      val account = Await.result(accountFuture, Duration(authenticationTimeout, TimeUnit.SECONDS) )

      if (account.isFailure) {
        play.Logger.debug(account.failed.get.getMessage)
        return None
      }
      return Some( User(account.get.getEmail, account.get.getFullName, account.get.getUsername, account.get.getCustomData.getHref ))

    } catch {
      case e: TimeoutException => {
        play.Logger.warn(e.getMessage)
        return None
      }
    }
  }

  //TODO: This should be a future invoked via Ajax after login
  /**
   * This method will retrieve all the custom data for the given custom data href.
   *
   * @param href the href of the custom data to be retrieved
   * @return a `Map` containing the keys and values of each custom data field
   */
  def getCustomData(href: String) : Map[String, String] = {
    require(Option(href).isDefined && href.size > 0, "href cannot be empty")

    val customData = client.getResource(href, classOf[CustomData])
    customData.map(i => i._1 -> i._2.toString).toMap
  }

  /**
   * This method will insert or update a custom data field into the custom data of the given custom data href.
   * When the key does not previously exist, the field is created. If the key already exists, the existing value will be replaced with
   * the newly entered value.
   *
   * @param href the href of the custom data where the field will be added.
   * @param key the key of the custom data field to be added/updated
   * @param value the value of the custom data field to be added
   * @return a `Future` object for the asynchronous computation
   */
  def addCustomData(href: String, key: String, value: String) = Future {
    require(Option(href).isDefined && href.size > 0, "href cannot be empty")
    require(Option(key).isDefined && key.size > 0, "key cannot be empty")
    require(!key.contains(" "), "key cannot contain spaces")

    val customData = client.getResource(href, classOf[CustomData])
    customData.put(key, value)
    customData.save()
  }

  /**
   * This method will delete a custom data field from the given custom data href.
   *
   * @param href the href of the custom data whose field will be deleted
   * @param key the key of the custom data field to be deleted
   * @return a `Future` object for the asynchronous computation
   *
   */
  def deleteCustomDataItem(href: String, key: String) = Future {
    require(Option(href).isDefined && href.size > 0, "href cannot be empty")
    require(Option(key).isDefined && key.size > 0, "key cannot be empty")
    require(!key.contains(" "), "key cannot contain spaces")

    val customData = client.getResource(href, classOf[CustomData])
    customData.remove(key)
    customData.save()
  }

}
