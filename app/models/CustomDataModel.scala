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

package models


import scala.language.postfixOps
import controllers.StormpathManager

case class CustomDataModel(href: String, customData: Map[String, String])
case class CustomDataItem(key: String, value: String)

object CustomDataModel {

  /**
   * Get custom data.
   */
  def getCustomData(href: String): Seq[CustomDataItem] = {
    val customData = StormpathManager.getCustomData(href)
    customData.map { case (key,value) => CustomDataItem(key, value.toString) }(collection.breakOut): Seq[CustomDataItem]
  }


  /**
   * Add a custom data item.
   */
  def addCustomDataItem(href: String, customDataItem: CustomDataItem) = {
    StormpathManager.addCustomData(href, customDataItem.key, customDataItem.value)
  }

  /**
   * Delete custom data.
   */
  def deleteCustomDataItem(href: String, key: String) = {
    StormpathManager.deleteCustomDataItem(href, key)
  }

}


