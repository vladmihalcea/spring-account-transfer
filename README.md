# Spring Account Transfer

This repository provides a Spring application that suffers from a race condition that breaks the account balance after doing a transfer between two bank accounts. 

The underlying issue is caused by a Lost Update anomaly, which is prevalent on the default isolation level of many relational database systems.

## Application description

The application is developed using Spring Boot and PostgreSQL and has three layers:

- a Web layer that provides a REST controller the clients can use to interact with the application,
- a Service layer to define the transaction boundaries, and
- a Repository layer that provides the data access logic

## How to run the application

To start the application, just execute the `run.bat` batch script,
which builds the Maven artifact as well and attaches the Lighrun agent when starting the application.

After the application is started, we are going to open a tunnel from `localhost` to the Internet, as explained in [this article](https://vladmihalcea.com/tunnel-localhost-public-internet/). This will allow us to use a proper IP address, which is going to be used by the application for filtering requests.

## How to run the concurrent test suite

Start Jmeter and open the `spring-account-transfer.jmx` configuration file located in the root folder of this project. 

Run the test suite and check the `account` balances in the database.

Use the Lightrun logs to assert the balance before and after the transfer is done, as well as the country that's resolved by the Geolocation web service.

In the end, change the isolation level to prevent the Lost Update anomaly.




