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

import controllers.StormpathManager

//Holder for the custom data fields (i.e: key and value)
case class CustomDataItem(key: String, value: String)

/**
 * A domain object model providing custom data operations. It represents the custom data information to be used across this sample app.
 * This object is stateless and all the information is retrieved from Stormpath by means of the {@link StormpathManager}.
 *
 */
object CustomDataModel {

  /**
   * Get custom data from Stormpath.
   *
   * @param href the custom data href to be retrieved from Stormpath
   * @return a sequence of `CustomDataItem` containing the key-values for the requested custom data href
   */
  def getCustomData(href: String): Seq[CustomDataItem] = {
    val customData = StormpathManager.getCustomData(href)
    customData.map { case (key,value) => CustomDataItem(key, value.toString) }(collection.breakOut): Seq[CustomDataItem]
  }

  /**
   * Add a custom data item.
   *
   * @param href the custom data href where the custom data field will be stored in Stormpath.
   * @param customDataItem the actual custom data field to be stored in stormpath
   */
  def addCustomDataItem(href: String, customDataItem: CustomDataItem) = {
    StormpathManager.addCustomData(href, customDataItem.key, customDataItem.value)
  }

  /**
   * Delete a custom data field from Stormapth.
   *
   * @param href the custom data href whose custom data field will be deleted
   * @param key the id of the custom data field that will be removed
   */
  def deleteCustomDataItem(href: String, key: String) = {
    StormpathManager.deleteCustomDataItem(href, key)
  }

}


