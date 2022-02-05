# wffweb-demo-deployment

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

After deployed to Heroku, set the following config vars in Heroku Settings

|        Key         |         Value                                             |
|--------------------|:---------------------------------------------------------:|
| MAVEN_CUSTOM_GOALS | clean package                                             |
| MAVEN_CUSTOM_OPTS  | --update-snapshots -DskipTests=true                       |
| DOMAIN_URL         | _eg: https://wffweb.herokuapp.com_                        |
| ORIGIN_DOMAIN_URL  | same as _DOMAIN_URL_ eg: _https://wffweb.herokuapp.com_   |
| DOMAIN_WS_URL      | eg: _wss://wffweb.herokuapp.com_                          |
| SESSION_TIMEOUT  (Optional)  |         5                                       |


##### This demo project is deployed at [demo.webfirmframework.com](http://demo.webfirmframework.com)

###### Anybody can contribute to this repository. Any changes made to this demo app will be reflected in [demo.webfirmframework.com](http://demo.webfirmframework.com).
