# wffweb demo app with wffweb-12.0.0-beta.5
#### It contains sample code for url rewriting/routing, JWT token based authentication/authorization and configuration for multi node support for scaling.

[Fork](https://github.com/webfirmframework/wffweb-demo-deployment/fork) and [![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

After deployed to Heroku, set the following config vars in Heroku Settings

|        Key         |         Value                                             |
|--------------------|:---------------------------------------------------------:|
| MAVEN_CUSTOM_GOALS | clean package                                             |
| MAVEN_CUSTOM_OPTS  | --update-snapshots -DskipTests=true                       |
| DOMAIN_URL         | _eg: https://wffweb.herokuapp.com_                        |
| ORIGIN_DOMAIN_URL  | same as _DOMAIN_URL_ eg: _https://wffweb.herokuapp.com_   |
| DOMAIN_WS_URL      | eg: _wss://wffweb.herokuapp.com_                          |
| SESSION_TIMEOUT  (Optional)  |         5                                       |
| ENABLE_HEARTBEAT  (required only if Heroku)  |         true                    |


##### This demo project is deployed at [wffweb.herokuapp.com](https://wffweb.herokuapp.com)

##### To run this project in your local machine, open this project with IntelliJ IDEA as a maven project and run `com.webfirmframework.web.launcher.Main.main` method.

### How to scale app with multiple nodes?
The only thing you have to do is to deploy this app in multiple server nodes and connect it with a load balancer. 
**No need to configure sticky sessions to the load balancer**.

_(use your own domain names)_

**Eg**: _deploy this wffweb demo app in multiple domain nodes like node1.webfirmframework.com, node2.webfirmframework.com, node2.webfirmframework.com etc... 
Now create a load balancer in the main domain webfirmframework.com and point it to those subdomain nodes. That's all!_

**NB**: _The websocket connection should be publicly accessible in the subdomain nodes. 
The access to http connection in the subdomain nodes may be limited to the load balancer, 
to do that, the load balancer should route request to the nodes along with an app key,
this app key may be checked in the demo app._
