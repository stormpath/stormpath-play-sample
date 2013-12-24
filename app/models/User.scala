package models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps
import com.stormpath.sdk.client.{ClientBuilder, Client}
import com.stormpath.sdk.application.Application
import scala.sys.SystemProperties
import play.api.Logger

//import scala.Application
import com.stormpath.sdk.account.Account
import com.stormpath.sdk.authc.UsernamePasswordRequest

case class User(email: String, name: String, password: String)

object User {

  
  // -- Parsers
  
  /**
   * Parse a User from a ResultSet
   */
//  val simple = {
//    get[String]("user.email") ~
//    get[String]("user.name") ~
//    get[String]("user.password") map {
//      case email~name~password => User(email, name, password)
//    }
//  }
  
  // -- Queries
  
  /**
   * Retrieve a User from email.
   */
//  def findByEmail(email: String): Option[User] = {
//    DB.withConnection { implicit connection =>
//      SQL("select * from user where email = {email}").on(
//        "email" -> email
//      ).as(User.simple.singleOpt)
//    }
//  }
  
  /**
   * Retrieve all users.
   */
//  def findAll: Seq[User] = {
//    DB.withConnection { implicit connection =>
//      SQL("select * from user").as(User.simple *)
//    }
//  }
  
  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Option[User] = {
//    DB.withConnection { implicit connection =>
//      SQL(
//        """
//         select * from user where
//         email = {email} and password = {password}
//        """
//      ).on(
//        "email" -> email,
//        "password" -> password
//      ).as(User.simple.singleOpt)
//    }

    val applicationRestUrl = "https://api.stormpath.com/v1/applications/3TqbyZ1qo74eDM4gTo2H94"

    val path = util.Properties.envOrNone("HOME").get + "/.stormpath/apiKey.properties"

    val client = new ClientBuilder().setApiKeyFileLocation(path).build()

    val application = client.getResource(applicationRestUrl, classOf[Application])

    try {

      val account = application.authenticateAccount(new UsernamePasswordRequest(email, password, null)).getAccount()
      Some(User(email, "", password))

    } catch {

      case e: Exception => Logger.info(e.getMessage)
      None

    }

  }
   
  /**
   * Create a User.
   */
  def create(user: User): User = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into user values (
            {email}, {name}, {password}
          )
        """
      ).on(
        "email" -> user.email,
        "name" -> user.name,
        "password" -> user.password
      ).executeUpdate()
      
      user
      
    }
  }
  
}
