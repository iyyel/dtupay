# 02267 SDWS - Group 11 - dtu-pay

1.  [Introduction](#Introduction)
2.  [Microservices](#Microservices)
3.  [REST](#REST)
4.  [Payment-service](#Payment-service)
5.  [Token-service](#Payment-service)
6.  [Account-service](#account-service)
7.  [Report-service](#report-service)
8.  [Client application](#Clientapplication)
9.  [OpenAPI](#OpenAPI)  
10. [Installation](#Installation)
11. [Contributors](#Contributors)


## Introduction

This repository contains the source code and deliverables from Group 11 for the course 02267 Software Development of Web Services held in January 2021 at the Technical University of Denmark, DTU.

The system consists of four microservices called Payment-service, Token-service, Account-service and Report-service. A REST interfaces giving an outside interface for the system exists as well. The Client application is used for executing end-to-end tests.


## Microservices

The microservices are running in the virtual-machine given by DTU Compute. The
base URL for the virtual-machine is `g-11.compute.dtu.dk`. The main REST interface is listening on port `8080`, so for accessing it you would go to `g-11.compute.dtu.dk:8080`. The report service used for managerial purposes is found on port `8083`. You can access the Swagger UI by appending `/swagger-ui` at the end of the URL.


### REST

The REST project holds the unified REST interface for the system. Essentially, all it does it take in a HTTP request and then map it to a respective RabbitMQ message to the internal microservices.


### Payment-service

The purpose of the payment-service is to execute money transfers with the FastMoney bank, both regular payments and refunds. This will be triggered by a message sent from the REST project.


### Token-service

Token-service is used by the customer application to request tokens and by the merchant to ask for one of 
these tokens on behalf of the customer during a payment situation. Therefore token-service functionality is also triggered in payment situations when it has to validate the token that is given for the payment.



### Account-service

The account service keeps track of the user accounts that exist in DTUPay.
It interacts with the 3rd party FastMoney BankService who controls the bank accounts. It is essentially
used for user management of DTUPay.


### Report-service

The report service is used to generate reports for the customers, merchants and managers of the system. It does this by always listening for messages that indicate whether a payment has succeeded or not,
and then records that such that it can be used
for reporting later. Report-service has its own
REST interface that managers can use and generate
these reports from.


### Client-application

This project attempts to emulate an actual usage of the system as seen from the client. It contains both simulations for the customer application and the merchant application. It uses these applications in the end-to-end tests.


## OpenAPI

To fetch the OpenAPI specification for the system, you can execute the following CURL command. Change the port to get the specification for
the respective service. `8080` for the unified REST interfance and `8083` for the report service. These specifications also exist in the root of the project as `openapi-spec-dtupay-api.yml` and `openapi-spec-management-api` respectively.

```
curl -o openapi-spec.yml g-11.compute.dtu.dk:8080/openapi
```


## Installation

To build and install the system locally using docker, execute the following script. Docker and
docker-compose is required. 

```
build_and_run.sh
```

Essentially what this script will do is first package all of the services, build their Docker containers, and then use docker-compose to configure to the containers for you and run them. You can use the `mvn_package_all.sh` script only building the executable binaries if desirable.


## Contributors

- Tobias Rydberg (s173899)
- Sebastian Lindhard Budsted (s135243)
- Daniel Larsen (s151641)
- Emil Kosiara (s174265)
- Troels Lund (s161791)
- Kasper L. Stilling (s141250)