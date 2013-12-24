package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._
import controllers.com.stormpath.play.AuthManager

object MainController extends Controller {

  // -- Authentication

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      //case (email, password) => User.authenticate(email, password).isDefined
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(html.login(loginForm))
    //login.render(form(Login.class))
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      //user => Redirect(routes.Projects.index).withSession("email" -> user._1)
      user => Redirect(routes.MainController.index).withSession("email" -> user._1)
    )
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

//  def javascriptRoutes = Action { implicit request =>
//    import routes.javascript._
//    Ok(
//      Routes.javascriptRouter("jsRoutes")(
//        Projects.add, Projects.delete, Projects.rename,
//        Projects.addGroup, Projects.deleteGroup, Projects.renameGroup,
//        Projects.addUser, Projects.removeUser, Tasks.addFolder,
//        Tasks.renameFolder, Tasks.deleteFolder, Tasks.index,
//        Tasks.add, Tasks.update, Tasks.delete
//      )
//    ).as("text/javascript")
//  }

}

  /**
   * Provide security features
   */
  trait Secured {

    /**
     * Retrieve the connected user email.
     */
    private def username(request: RequestHeader) = request.session.get("email")

    /**
     * Redirect to login if the user in not authorized.
     */
    private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.MainController.login)

    // --

    /**
     * Action for authenticated users.
     */
    def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }

}
