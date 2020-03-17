# Getting Started
###PROFILE: Dev
#####Add to intellij IDEA the Environment variables:
~~~~
PORT
DATABASE_URL
USERNAME_POSTGRES
PASSWORD_POSTGRES
~~~~
#####How to do:
~~~~
-Edit Configuration in Run/Debug.
-Tab Configuration
-Click on Environment
-Type all Environment variables like this:
DATABASE_URL=VALUE;USERNAME_POSTGRES=VALUE;PASSWORD_POSTGRES=VALUE;PORT=VALUE
~~~~
###PROFILE: Prod
#####Deploy on Heroku and set up Environment variables:
~~~~
heroku config:set PORT=XXXX
heroku config:set DATABASE_URL=XXXX
heroku config:set USERNAME_POSTGRES:XXXX
heroku config:set PASSWORD_POSTGRES:XXXX
heroku config:set SPRING_PROFILES_ACTIVE=prod

