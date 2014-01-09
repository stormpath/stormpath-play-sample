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
 * Manage custom data related operations.
 */
object CustomDataController extends Controller with Secured {

  implicit val executionContext = StormpathExecutionContext.executionContext

  val customDataForm = Form(
    tuple(
      "key" -> nonEmptyText,
      "value" -> nonEmptyText
    )
  )

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
   * Add a custom data item.
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
   * Delete custom data.
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

