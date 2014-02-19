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

//The actual `User` holding some of the account's data such as email, full name, username and account custom data href
case class User(email: String, fullName: String, username: String, customDataHref: String)

/**
 * Stateless domain object model creator representing a logged user in this sample application.
 */
object User {

  /**
   * Simple operation that requests an actual user authentication to the {@link StormpathManager}.
   *
   * @param email the email of the user being authenticated
   * @param password the password of the user being authenticated
   * @return an `User` instance containing some of the account data, such as username, full name, username and account custom data href
   */
  def authenticate(email: String, password: String): Option[User] = {
    StormpathManager.authenticate(email, password)
  }

}
