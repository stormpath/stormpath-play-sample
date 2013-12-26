package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._

object Application extends Controller {

  var loggedUser : Option[User] = None

  // -- Authentication

//  val loginForm = Form (
//    tuple(
//      "email" -> text,
//      "password" -> text
//    ) verifying ("Invalid email or password", result => result match {
//      case (email, password) => {
//        val user = User.authenticate(email, password)
//        //session +("username", user.get.name)
//        user.isDefined
//      }
//    })
//  )

  def updatePasswordForm(implicit request: Request[_]) = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => {
        val user = User.authenticate(email, password)
        loggedUser = user
        user.isDefined
      }
    })
  )

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    //Ok(html.login(loginForm))
    Ok(html.login(updatePasswordForm))
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    //play.Logger.info(request.session + ("username" -> "aaaa") get "username" get)
    //val session = request.session + ("username" -> "aaaa")
    //play.Logger.info("11session empty?: " + request.session.isEmpty)
    //request.session.get("username").map { username => play.Logger.info("username33: " + username) }
    updatePasswordForm.bindFromRequest.fold(
      formWithErrors => {
        loggedUser = None
        BadRequest(html.login(formWithErrors))
      },
      //user => Redirect(routes.Projects.index).withSession("email" -> user._1)
      //user => Redirect(routes.Projects.index).withSession("email" -> user.asInstanceOf[User].email, "username" -> user.asInstanceOf[User].name)
      user => {
        //Redirect(routes.Projects.index).withSession("email" -> user.asInstanceOf[User].email, "username" -> user.asInstanceOf[User].name)
        //play.Logger.info("session: " + session)
        Redirect(routes.Projects.index).withSession("email" -> user._1, "username" -> loggedUser.get.name)
      }
    );
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  // -- Javascript routing

  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
//        Projects.add, Projects.delete, Projects.rename,
//        Projects.addGroup, Projects.deleteGroup, Projects.renameGroup,
//        Projects.addUser, Projects.removeUser, Tasks.addFolder,
//        Tasks.renameFolder, Tasks.deleteFolder, Tasks.index,
//        Tasks.add, Tasks.update, Tasks.delete
        Projects.add, Projects.addGroup, Tasks.addFolder
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
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
  
  // --
  
  /** 
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }

  /**
   * Check if the connected user is a member of this project.
   */
  def IsMemberOf(project: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
    if(Project.isMember(project, user)) {
      f(user)(request)
    } else {
      Results.Forbidden
    }
  }

  /**
   * Check if the connected user is a owner of this task.
   */
  def IsOwnerOf(task: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
    if(Task.isOwner(task, user)) {
      f(user)(request)
    } else {
      Results.Forbidden
    }
  }

}

