package controllers;

import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import views.html.*;

public class MainController extends Controller {
    
    public static Result index() {
        return ok(index.render("Hello from Java"));
    }

    /**
     * Login page.
     */
    public static Result login() {
        return ok(
                login.render(form(Login.class))
        );
    }

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        return ok();
    }


    public static class Login {
        public String email;
        public String password;
    }

}
