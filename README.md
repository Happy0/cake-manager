# Cake Manager Micro Service (fictitious)

## Changes

### Project changed to Spring Boot

#### Running the project

To run a server locally execute the following command (assumes you have an up to date version of the JDK on your path) in the 'cake-manager' subfolder:

`./gradlew bootRun`

and access the following URL:

`http://localhost:8080/`

#### New Technology

"We feel like the software stack used by the original developer is quite outdated, it would be good to migrate the entire application to something more modern."

I took this comment in the README to as an opportunity to try Spring Webflux (https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html).

I was already familiar with Spring Boot from using it at a previous job, but I hadn't tried Spring's new Reactive Programming features.

### Database

For convenience, the application uses an in-memory h2 database but could be easily configured in `application.properites` (or via the environment) to use a different relational database without changing any code.

### Loading cakes

Functionality to load cakes from the supplied JSON file (via a URL) has been moved to `CakeMigrator.java`

### API Changes

* The `controllers` package contains `CakeRestController` which implements the cakes REST API. `CakeStaticController` is used to serve the static html and JavaScript files.

* The `/cakes` `GET` endpoint now takes optional paging parameters to allow clients to retrieve the cakes in segments. These query parameters have the keys `page` and `size` respectively (and 'page' is indexed from 1) as I wanted to explore how to return a HTTP error status code response in Spring flux. Returns a `400` status code for invalid paging parameters.

* Added a `POST` verb handler to the `/cakes` collection which adds a new cake to the system.

### Tests
The tests I added can be ran via the `./gradlew test` command.

#### Integration Tests

* I added integration tests for the `/posts` collection to ensure that it is possible to add new cakes and retrieve the existing cakes in the system, and that error responses are returned where appropriate. These are in the `/src/test/integration` folder.

* Added unit tests for validators ran on using `POST` verb on endpoint and processing query parameters when using the `GET` verb.

### Client

* I made a small react project (the `cake-manager-client` folder at the top level) to implement the client functionality of displaying the cakes in the system. The static files served from the `/` root by the spring boot server app is the files output by building this project.

