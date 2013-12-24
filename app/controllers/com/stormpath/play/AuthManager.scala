package controllers.com.stormpath.play

import com.stormpath.sdk.client.{ClientBuilder, Client}
import com.stormpath.sdk.application.Application
import com.stormpath.sdk.authc.{UsernamePasswordRequest, AuthenticationResult}
import com.stormpath.sdk.account.Account
import com.stormpath.sdk.resource.ResourceException


object AuthManager {

    def authenticate(username: String, password: String): Boolean = {

    var applicationRestUrl : String = "https://api.stormpath.com/v1/applications/3TqbyZ1qo74eDM4gTo2H94"

    var path : String = System.getProperty("user.home") + "/.stormpath/apiKey.properties"

    var client : Client = new ClientBuilder().setApiKeyFileLocation(path).build()

    var application : Application = client.getResource(applicationRestUrl, classOf[Application])

    try {

      var account : Account = application.authenticateAccount(new UsernamePasswordRequest(username, password, null)).getAccount()
      true

    } catch {

      case e: Exception => false
    }

  }

}
