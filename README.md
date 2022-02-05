# wffweb-demo-deployment

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

After deployed to Heroku, set the following config vars in Heroku Settings

|        Key         |         Value                       |
| ------------------ |:-----------------------------------:|
| MAVEN_CUSTOM_GOALS | clean package                       |
| MAVEN_CUSTOM_OPTS  | --update-snapshots -DskipTests=true |

##### This demo project is deployed at [demo.webfirmframework.com](http://demo.webfirmframework.com)

###### Anybody can contribute to this repository. Any changes made to this demo app will be reflected in [demo.webfirmframework.com](http://demo.webfirmframework.com).
