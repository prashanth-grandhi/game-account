## Wallet Service

The goal is to develop a simple game account (wallet service) that is integrated with game engines and used to purchase games and wins.

## Setup Project

The solution uses Spring Boot with Kotlin for the server integrated with a PostgreSQL database to store customer and game event data.

- Go to http://start.spring.io and create a new project:
- Select Gradle Project
- Select Kotlin in the language section
- Select Spring Boot latest stable version
- Enter Group as com.example
- Enter Artifact as game-account
- Add Spring Web, Spring Data JPA, PostgreSQL Driver
 dependencies.
- Click Generate to generate and download the project.

Once the project is generated, unzip it and import it into IntelliJ IDE.

## Configure PostgreSQL Database

Install postgres and create the GAMEACCOUNT database using pgAdmin 4 tool.

Open src/main/resources/application.properties and add database configuration as shown below.

spring.datasource.url=jdbc:postgresql://localhost:5432/GAMEACCOUNT
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

Check if the database service is up and running.

## Create Entity, Repositories and Controller classes

Create data classes for Customer and GameEvent entities.
Create the repository classes for accessing the data from the database. Name it CustomerRepository and GameEventRepository.
Create a controller to handle the purchase and win events. Name it GameAccountController.

## Enable HTTPS encryption for REST API

Generate a self signed certificate:
openssl req -newkey rsa:2048 -nodes -keyout keystore.key -x509 -days 365 -out keystore.crt

Export the cert and key to PKCS12 format:
openssl pkcs12 -export -in keystore.crt -inkey keystore.key -out keystore.p12 -name tomcat

Move the keystore.p12 file to the src/main/resources folder in application.

Open src/main/resources/application.properties and add HTTPS configuration as below.

server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=<keystore_password>
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat

## Run application

Run the application. It will start on port 8443 (as configured in application.properties).
And check that PostgreSQL database is up and running.

## Testing Rest APIS

**POST /api/game-account/customers - Create a customer**

curl --cert-type P12 --cert keystore.p12:password -k -i -H "Content-Type: application/json" -X POST \
-d '{"name":"Tinna"}' \
https://localhost:8443/api/game-account/customers

Output
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 12 Dec 2023 12:24:27 GMT

{"id":4,"name":"Tinna","accountBalance":0.0}

**GET /api/game-account/customers - Get all customers**

curl --cert-type P12 --cert keystore.p12:password -k -i -H 'Accept: application/json' https://localhost:8443/api/game-account/customers

Output
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 12 Dec 2023 12:26:55 GMT

[{"id":2,"name":"Pekka","accountBalance":105.5},{"id":1,"name":"Prashant","accountBalance":70.0},{"id":3,"name":"Timo","accountBalance":200.0},{"id":4,"name":"Tinna","accountBalance":0.0}]

**POST /api/game-account/charge - Customer purchase event**

curl --cert-type P12 --cert keystore.p12:password -k -i -H "Content-Type: application/json" -X POST \
-d '{"customerId":3,"amount":10}' \
https://localhost:8443/api/game-account/charge

Output
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 12 Dec 2023 12:29:05 GMT

{"remainingBalance":190.0}

**POST /api/game-account/win - Customer win event**

curl --cert-type P12 --cert keystore.p12:password -k -i -H "Content-Type: application/json" -X POST \
-d '{"customerId":3,"winningAmount":10.50}' \
https://localhost:8443/api/game-account/win

Output
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 12 Dec 2023 12:30:50 GMT

{"newBalance":200.5}

## Known issues
- I get error when using the self signed certificate "curl: (60) SSL certificate problem: self signed certificate”. I used -k option to ignore certificate verification.










