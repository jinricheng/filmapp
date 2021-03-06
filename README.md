# :sparkles: :heart: Films App :heart: :sparkles:

[![Build Status](https://travis-ci.org/jinricheng/filmapp.svg?branch=master)](https://travis-ci.org/jinricheng/filmapp)

This is a simple Spring demo of a Films application providing a RESTFul JSON API also available from an HTML user interface. It features:

* Spring MVC with Content Negotiation
* Spring Data JPA/Hibernate persistence over HSQL and Heroku PostgreSQL
* Unit Testing
* Spring Cucumber acceptance tests
* Simple CORS Filter
* Travis Continuous Integration
* ...

To run locally, first build WAR package:
```
mvn package
```

Then run embedded Tomcat server:
```
mvn exec:exec
```

The application will be available at http://localhost:8080/ (locally) & https://filmsapp.herokuapp.com/ (Internet)
