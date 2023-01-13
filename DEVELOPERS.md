# User API Dev Guide

## Tech Stack
* Java 11
* SpringBoot
* Docker compose
* PostgreSQL
* Lombok
* Mapstruct
* Spring Data JPA
* H2 in-memory DB
* Mockito
* MockMvc
* Flyway

### This is a SpringBoot based User RESTful API service, it supports the below functions:
* Create a new Client
* Get a Client by Client ID
* List all Clients
* Create an Account for specific Client
* List all Accounts

## Building
* Run the below command under the root directory
```shell
./gradlew clean build 
```
## Unit Testing
* Run unit tests by using the command shown
```shell
./gradlew clean test
```
## Database
#### PostgreSQL is being applied here, and we are also using Docker Compose to make local development convenient
* When running for the first time, initialise the Docker dependencies using the below make target:
```shell
docker-compose up -d
```
* Afterwards, Docker dependencies can be started using the below target:
```shell
docker-compose start
```
* To stop the Docker dependencies and **keep data**, run the below target:
```shell
docker-compose stop
```
* To completely tear-down the Docker dependencies, run:
```shell
docker-compose down
```
## Running
#### There are two ways to run the application locally:
* Run below command under root directory regardless of the build process has been executed or not
```shell
./gradlew bootRun
```
* Execute this command strictly after build process mentioned above is done
```shell
 java  -jar ./build/libs/userapi-0.0.1-SNAPSHOT.jar
```

## Design and Implementation
* Hibernate builds the entity layer to connect PostgreSQL database server
* Flyway is used as database migration tool
* Spring Data JPA builds the repository layer & H2 in-mem database used as Unit Test to test this layer
* Mapstruct used to map data between entities and dtos
* Mockito and MockMvc unit test service and controller layer
* Defined customised exceptions for better error handling and all the exceptions can be centrally handled in one place (ControllerExceptionHandler.java)
* Lombok automatically generates getter,setter, constructor, hashcode, log etc.
* Docker compose has been used in local to easily set up the local environment.
* High test coverage

## Additional Information
* Please find the postman collection file in **postman** folder and import into your postman to check more details for existing endpoints. Also,don't forget update the collections if there is any update.
* As we are using Flyway for database migration, if there is any change for database needed please add new script under **db/migration** folder to make it done, don't modify existing scripts!!!


