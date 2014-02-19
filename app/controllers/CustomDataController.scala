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

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._
import scala.concurrent.Future
import context.StormpathExecutionContext

/**
 * Handles Custom Data-related requests only allowed to authenticated users.
 *
 */
object CustomDataController extends Controller with Secured {

  // ExecutionContext to handle how and when the asynchronous computation is executed.
  implicit val executionContext = StormpathExecutionContext.executionContext

  //Form to hold custom data field insertion requests data
  val customDataForm = Form(
    tuple(
      "key" -> nonEmptyText,
      "value" -> nonEmptyText
    )
  )

  /**
   * Handles account's custom data retrieval. The actual retrieval operation is delegated to {@link CustomDataModel}.
   *
   * @return the html page to load.
   */
  def index = IsAuthenticated { username => implicit request =>
    val future = Future {
      CustomDataModel.getCustomData(request.session.get("customDataHref").get)
    }
    future.map( customData => {
      Ok(
        html.dashboard(request.session.get("email").get, request.session.get("fullName").get, customData)
      )
    }
    )
  }

  /**
   * This method will insert or update a custom data field into the custom data of the logged in account. When the
   * key does not previously exist, the field is created. If the key already exists, the existing value will be replaced with
   * the newly entered value.
   * </pre>
   * The actual addition operation is delegated to {@link CustomDataModel}.
   * <pre/>
   * This method is restricted to authenticated users only.
   *
   * @return the result of the insertion operation
   */
  def addCustomDataItem() = IsAuthenticated { username => implicit request =>
    val future = Future {
      customDataForm.bindFromRequest.fold(
      errors => BadRequest,
      {
        case (key, value) =>
          val customDataItem = CustomDataItem(key, value)
          CustomDataModel.addCustomDataItem(request.session.get("customDataHref").get, customDataItem)
          Ok(html.customData.item(customDataItem))
      }
      )
    }
    future.map(
      status => status
    )
  }

  /**
   * Deletes a custom data field from the account's custom data.
   * </pre>
   * The actual removal operation is delegated to {@link CustomDataModel}.
   * <pre/>
   * This method is restricted to authenticated users only.
   *
   * @return the status of the removal operation
   */
  def deleteCustomDataItem(key: String) = IsAuthenticated { username => implicit request =>
    val future = Future {
      CustomDataModel.deleteCustomDataItem(request.session.get("customDataHref").get, key)
      Ok
    }
    future.map(
      status => status
    )
  }

}

