# Have a nice day [![Build Status](https://travis-ci.org/pedrovgs/HaveANiceDay.svg?branch=master)](https://travis-ci.org/pedrovgs/HaveANiceDay)

Server-side code for ``Have a nice day`` project written in [Scala](https://scala-lang.org) using [Finatra](https://twitter.github.io/finatra/).

## Run the project

This repository uses [SBT](http://www.scala-sbt.org/) as build tool. To run this project you can just import this repository as an Play or SBT project into your IDE or run the following command:

```
sbt run
```

This will download some dependencies needed to build and run the project and will start listening for HTTP requests on ``localhost:9000``.

## Pass the tests

To build and test this project you can execute ``sbt test``. You can also use sbt interactive mode (you just have to execute ``sbt`` in your terminal) and then use the triggered execution to execute your tests using the following commands inside the interactive mode:

```
~ test // Runs every test in your project
~ test-only *AnySpec // Runs specs matching with the filter passed as param.
```

## Checkstyle

For the project checkstyle we are using [ScalaFMT](http://scalameta.org/scalafmt/). The code format will be evaluated after accepting any contribution to this repository using this tool. You can easily format your code changes automatically by executing ``sbt format``.

## API documentation

The REST API created in this project is documented using [Swagger](https://swagger.io/). Our build configuration will automatically generate a web-site with the API documentation based on the routing files if ``sbt run`` is executed. The documentation can be reviewed locally here: [http://localhost:9000/docs](http://localhost:9000/docs).
   
**The Swagger API description is generated into a file named ``swagger.json`` you can find in the path ``localhost:9000/swagger.json`` after running the app. This file can be used to generate client side code in many different languages automatically using [Swagger Code Gen](https://github.com/swagger-api/swagger-codegen).** You can easily install Swagger Code Gen from brew and execute the following command to generate, for example, the iOS API Client code:
   
```
swagger-codegen generate -i target/swagger/swagger.json -l swift -o ~/Desktop/zolla-ios-api-client
```

The complete documentation of the plugin being used to generate the Swagger specs can be found [here](https://github.com/jakehschwartz/finatra-swagger). 

## Contributing

If you would like to contribute code to this repository you can do so through GitHub by creating a new branch in the repository and sending a pull request or opening an issue. Please, remember that there are some requirements you have to pass before accepting your contribution:

* Write clean code and test it.
* The code written will have to match the product owner requirements.
* Follow the repository code style.
* Write good commit messages.
* Do not send pull requests without checking if the project build is OK in the CI environment.
* Review if your changes affects the repository documentation and update it.
* Describe the PR content and don't hesitate to add comments to explain us why you've added or changed something.