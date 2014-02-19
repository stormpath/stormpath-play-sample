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

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._
import scala.concurrent.Future

/**
 * The main controller handling login, logout and authentication actions.
 *
 */
object MainController extends Controller {

  private var loggedUser : Option[User] = None

  /**
   * Creates the login form and authenticates the actual login request.
   *
   * @param request the request containing the login information
   * @return the result of the authentication
   */
  def loginForm(implicit request: Request[_]) = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => {
        loggedUser = User.authenticate(email, password)
        loggedUser.isDefined
      }
    })
  )

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  /**
   * Handle login form submission.
   *
   * @return the CustomDataController's main page if the authentication has been successful, the login page otherwise
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        loggedUser = None
        BadRequest(html.login(formWithErrors))
      },
      user  => {
        Redirect(routes.CustomDataController.index).withSession("email" -> loggedUser.get.email, "fullName" -> loggedUser.get.fullName, "customDataHref" -> loggedUser.get.customDataHref)
      }
    );
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.MainController.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  // -- Javascript routing

  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        CustomDataController.addCustomDataItem,
        CustomDataController.deleteCustomDataItem
      )
    ).as("text/javascript") 
  }

}

/**
 * Provide security features
 */
trait Secured {
  
  /**
   * Retrieve the connected user email.
   */
  private def email(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.MainController.login)
  
  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Future[SimpleResult]) =
    Security.Authenticated(email, onUnauthorized) { user =>
    Action.async { request =>
      email(request).map { login =>
        f(login)(request)
      }.getOrElse(Future.successful(onUnauthorized(request)))
    }
  }

}

