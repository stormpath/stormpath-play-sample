[![Build Status](https://api.travis-ci.org/stormpath/stormpath-play-sample.png?branch=master)](https://travis-ci.org/stormpath/stormpath-play-sample)

# Stormpath Play Framework Sample #

Copyright &copy; 2013 Stormpath, Inc. and contributors. This project is open-source via the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).  

This sample application uses the [Stormpath Scala Plugin](https://github.com/stormpath/stormpath-scala) to demonstrate how to achieve [Stormpath](http://www.stormpath.com) and [Play Framework](www.playframework.com) integration.

This plugin simplifies the integration of the [Stormpath Java SDK](https://github.com/stormpath/stormpath-sdk-java) with Scala and Play applications, providing simple access to Stormpath's user account support, authentication, account registration and password reset workflows, password security and more -with little coding on your part. It provides asynchronous operation while providing a Scala-friendly API.


## Setup ##

### Sign Up For A Stormpath Account ###

1. Create a [Stormpath](http://www.stormpath.com/) developer account and [create your API Keys](http://docs.stormpath.com/console/product-guide/#manage-api-keys) downloading the "apiKey.properties" file into a ".stormpath" folder under your local home directory

2. Within [Stormpath's Admin Console](https://stormpath.com/docs/console/product-guide), create an application and a directory to store your users' accounts.

3. Create at least one Group and Account in the application. Assign the account to the group.

4. Through the Stormpath's Admin interface, note your [application's REST URL](http://www.stormpath.com/docs/libraries/application-rest-url).

### Configure the Sample Application ###

1. Clone stormpath-play-sample into your local machine:

```bash
git clone git@github.com:stormpath/stormpath-play-sample.git
```

3. Edit `stormpath-play-sample/conf/application.conf` and replace the `applicationRestUrl` value with your Application's REST URL.

## Running the Sample Application ##

1. This project uses the [Typesafe Activator](http://typesafe.com/activator) to build and run. You just need to execute:

```bash
cd stormpath-play-sample
./activator ui
```

The activator UI will start in your browser and the source code will be compiled. Once it finishes, the sample app will be automatically started.


### Usage Instructions ###

Once it is running, open the following url in your web browser: `http://localhost:9000`. It will automatically display a login page. Now, go ahead and login with a user with access to the configured application (step 3 of "Configure the Sample Application" section).

After a successful login, the web app will display the existing [custom data](http://docs.stormpath.com/rest/product-guide/#custom-data) for this account. Fields can be deleted and added to it.

You can close your web session by clicking on the upper-right "logout" button or going to `http://localhost:8080/logout`.


## Change Log

### 0.1.0

- Stormpath SDK dependency to latest stable release of 0.9.2
- Provides a wepbage to view and edit account's custom data
- Can be run via the Typesafe Activator
