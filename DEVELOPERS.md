# User API Dev Guide

## Tech Stack
* Java 11
* Gradle
* SpringBoot
* Docker
* Docker compose
* PostgreSQL
* Lombok
* Mapstruct
* Spring Data JPA
* H2 in-memory DB
* Mockito
* MockMvc
* Flyway
* Make

### This is a SpringBoot based User RESTful API service, it currently supports the below functions:
* Create a new Client
* Get a Client by Client ID
* List all Clients
* Create an Account for specific Client
* List all Accounts

## Database
### PostgreSQL is being applied here, and we are also using Docker Compose to make local development convenient
* When running for the first time, initialise the Docker dependencies using the below make target:
  ```shell
  make app_local_compose_up
  ```
* Afterwards, Docker dependencies can be started using the below target:
  ```shell
  make app_local_compose_start
  ```
* To stop the Docker dependencies and **keep data**, run the below target:
  ```shell
  make app_local_compose_stop
  ```
* To completely tear-down the Docker dependencies, run:
  ```shell
  make app_local_compose_down
  ```

## Building
* Run the below command under the root directory
  ```shell
  make app_local_build 
  ```
  
## Unit Testing
* Run unit tests by using the command shown
  ```shell
  make app_local_test
  ```

## Running
* Have PostgresSQL up and running by docker-compose before running the application
* A private Spring profile can be used to __override__ the Spring properties specific to your local environment:
  ```
  src/main/resources/application-local.yml
  ```
* Run the application by typing below command:
  ```shell
  make app_local_run
  ```

## Deploying
* Dcokerize the application and deploy docker image anywhere needed
* Run below command to build docker image as per Dockerfile
  ```shell
  make app_docker_build
  ```
* Deploy docker image with variable injection to meet requirements of various environments

## Design and Implementation
* Hibernate builds the entity layer to connect PostgreSQL database server
* Flyway is used as database migration tool
* Spring Data JPA builds the repository layer & H2 in-mem database used as Unit Test to test this layer
* Mapstruct used to map data between entities and dtos
* Mockito and MockMvc unit test service and controller layer
* Defined customised exceptions for better error handling and all the exceptions can be centrally handled in one place (ControllerExceptionHandler.java)
* Lombok makes code base neater by automatically generating getter,setter, constructor, hashcode, log etc.
* Docker compose has been used in local to easily set up the local environment.
* Dockerfile put application files into docker instead of using fat jar.
* High test coverage ensures robustness of the application
* Makefile centrally manages complex commands to make developers's lives easier

## Additional Information
* Please find the postman collection file in **postman** folder and import into your postman to check more details for existing endpoints. Also,don't forget update the collections if there is any update.
* As we are using Flyway for database migration, if there is any change for database needed please add new script under **db/migration** folder to make it done, don't modify existing scripts!!!


