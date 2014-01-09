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
import com.stormpath.scala.provider.StormpathAuthenticationService
import context.StormpathExecutionContext
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

object StormpathManager {

  private val path = util.Properties.envOrNone("HOME").get + "/.stormpath/apiKey.properties"
  private val client = new ClientBuilder().setApiKeyFileLocation(path).build()
  val config = play.api.Play.configuration
  val applicationRestUrl = config.getString("applicationRestUrl").getOrElse {""}
  val service = new StormpathAuthenticationService(client, applicationRestUrl)
  implicit val executionContext = StormpathExecutionContext.executionContext

  def authenticate(email: String, password: String) : Option[User] = {

    val accountFuture = service.doAuthenticate(email, password)

    val account = Await.result(accountFuture, 30 seconds)

    if (account.isFailure) {
      play.Logger.debug(account.failed.get.getMessage)
      return None
    }

    Some( User(account.get.getEmail, account.get.getFullName, account.get.getUsername, account.get.getCustomData.getHref ))
  }

  //TODO: This should be a future invoked via Ajax after login
  def getCustomData(href: String) : Map[String, String] = {
    val customData = client.getResource(href, classOf[CustomData])
    customData.map(i => i._1 -> i._2.toString).toMap
  }

  def addCustomData(href: String, key: String, value: String) = Future {
    val customData = client.getResource(href, classOf[CustomData])
    customData.put(key, value)
    customData.save()
  }

  def deleteCustomDataItem(href: String, key: String) = Future {
    val customData = client.getResource(href, classOf[CustomData])
    customData.remove(key)
    customData.save()
  }

}
