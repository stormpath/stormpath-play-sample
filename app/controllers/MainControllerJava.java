package controllers;

import controllers.com.stormpath.play.AuthManager;
import play.api.mvc.RequestHeader;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import views.html.*;

public class MainControllerJava extends Controller {
//
//    public static Result index() {
//        return ok(index.render("Hello from Java"));
//    }
//
//    /**
//     * Login page.
//     */
//    public static Result login() {
//        return ok(
//                login.render(form(Login.class))
//        );
//    }
//
//    public static Result authenticate() {
//        play.Logger.info("Entering authenticate method...");
//        Form<Login> loginForm = form(Login.class).bindFromRequest();
//        if (loginForm.hasErrors()) {
//            return badRequest(login.render(loginForm));
//        } else {
//            session().clear();
//            session("email", loginForm.get().email);
//            return redirect(
//                    routes.MainController.index()
//            );
//        }
//    }
//
//    //def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login())
//
//    public void onUnauthorized(RequestHeader request) {
//        redirect(routes.MainController.login());
//    }
//
//    public static class Login {
//        public String email;
//        public String password;
//
//        public String validate() {
//            play.Logger.info("Entering validation in class Login: " + email);
//            if (! AuthManager.authenticate(email, password)) {
//                return "Invalid user or password";
//            }
//            return null;
//        }
//
//    }

}
