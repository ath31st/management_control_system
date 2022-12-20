# Management control system

Microservice training project with Apache Kafka and Keycloak auth.

## Project objectives
### What has already been implemented

 - Backend service (receiving, processing, delivering orders, making payments, refunding funds, creating, updating, mailing catalogs)
 - Shop service (providing catalogs, placing orders, issuing deliveries, returning deliveries, initial payment processing)
 - Gateway service ()
 - Project security (Keycloak for user registration, authentication and authorization, Kafka SASL authentication for services)

### Planned

 - Implement the discount system
 - Integrate Apache Cassandra for logging events from all project modules
 - Collecting and displaying statistics for managers and administrators in the web interface
 - Make a simple but visual web interface for customers (registration, purchases, refunds, viewing purchase history)

## Table of Contents

* [Application 'Management control system'](#management-control-system)
    * [Project objectives](#project-objectives)
        * [What has already been implemented](#what-has-already-been-implemented)
        * [Planned](#planned)
    * [Architecture](#architecture)
    * [Services](#services)
        * [Gateway service](#gateway)
        * [Backend service with PostgresQL database](#backend-with-PostgresQL-database)
        * [Shop1-n with Mongo database](#Shop1-n-with-Mongo-database)
        * [Kafka](#kafka)
        * [Zookeeper](#zookeeper)
        * [Keycloak with PostgresQL database](#keycloak-with-PostgresQL-database)
        * [Mongo-express](#mongo-express)
    * [Security](#security)
    * [Running Instructions](#running-instructions)
        * [Via docker](#via-docker)
            * [Usage](#usage)
                * [Get a token:](#get-a-token)
                * [Backend service:](#backend-service)
    * [References and further reading](#references-and-further-reading)

## Architecture

The microservice architectural style is an approach to developing a single application as a suite of small services, each running in its own process and communicating with lightweight mechanisms, often an HTTP resource API.
![image info](images/image01.jpg)

## Services

Each of the backing services must be located using a statically defined route

### Gateway

Implementation of an API gateway that is the single entry point for all managers and administrator.

### Backend with PostgresQL database

Work in progress

### Shop1-n with Mongo database

Work in progress

### Kafka

Kafka is a distributed system consisting of servers and clients that communicate via a high-performance TCP network protocol.
It can be deployed on bare-metal hardware, virtual machines, and containers in on-premise as well as cloud environments.

Servers: Kafka is run as a cluster of one or more servers that can span multiple datacenters or cloud regions.
Some of these servers form the storage layer, called the brokers. Other servers run Kafka Connect to continuously
import and export data as event streams to integrate Kafka with your existing systems such as relational databases as
well as other Kafka clusters. To let you implement mission-critical use cases, a Kafka cluster is highly scalable and
fault-tolerant: if any of its servers fails, the other servers will take over their work to ensure continuous operations without any data loss.

Clients: They allow you to write distributed applications and microservices that read, write, and process streams of events
in parallel, at scale, and in a fault-tolerant manner even in the case of network problems or machine failures. Kafka ships
with some such clients included, which are augmented by dozens of clients provided by the Kafka community: clients are available
for Java and Scala including the higher-level Kafka Streams library, for Go, Python, C/C++, and many other programming languages
as well as REST APIs.

In this project I created the following topics:
 - t.amount (in this topic, the producer is the backend service, and the consumers are all shop services. information about the amount of remaining products is transmitted in the published messages)
 - t.catalogue (in this topic, the producer is the backend service, and the consumers are the specific shop and gateway services. information about catalogs is transmitted in published messages)
 - t.delivery (delivery topic, producer backend service, consumers shop services. the messages transmit information about the delivered product, the customer, the purchase amount, and so on)
 - t.order (the topic of ordering, producers shop services, consumer backend service. the messages transmit information about the order received from the customer)
 - t.payment (order payment topic, producers shop services, consumer backend service. the messages contain information about the payment (or non-payment) of the order)
 - t.delivery-result (the topic of the results of order delivery, here the producers are shop services, and the consumer is a backend service. the messages transmit information about the results of delivery, including unsuccessful)

### Zookeeper

ZooKeeper is used in distributed systems for service synchronization and as a naming registry.  When working with Apache Kafka,
ZooKeeper is primarily used to track the status of nodes in the Kafka cluster and maintain a list of Kafka topics and messages.

ZooKeeper has five primary functions:
 - Controller Election. The controller is the broker responsible for maintaining the leader/follower relationship for all partitions. 
If ever a node shuts down, ZooKeeper ensures that other replicas take up the role of partition leaders replacing the partition
leaders in the node that is shutting down.

 - Cluster Membership. ZooKeeper keeps a list of all functioning brokers in the cluster.

 - Topic Configuration. ZooKeeper maintains the configuration of all topics, including the list of existing topics, 
number of partitions for each topic, location of the replicas, configuration overrides for topics, preferred leader node, among other details.

 - Access Control Lists (ACLs). ZooKeeper also maintains the ACLs for all topics.  This includes who or what is allowed 
to read/write to each topic, list of consumer groups, members of the groups, and the most recent offset each consumer group
received from each partition.

 - Quotas. ZooKeeper accesses how much data each client is allowed to read/write.

### Keycloak with PostgresQL database

Keycloak is an SSO solution for web apps, mobile and RESTful web services. It is an authentication server where users can centrally login,
logout, register, and manage their user accounts. The Keycloak admin UI can manage roles and role mappings for any application secured by Keycloak.
The Keycloak Server can also be used to perform social logins via the user's favorite social media site i.e. Google, Facebook, Twitter etc.

In this project I used the following features:
 - OAuth Bearer token auth for REST Services
 - OAuth 2.0 Grant requests
 - Deployable as a WAR, appliance, or on Openshift. Completely clusterable
 - Token claim, assertion, and attribute mappings 
 - Completely centrally managed user and role mapping metadata. Minimal configuration at the application side

### Mongo-express

Web-based MongoDB admin interface written with Node.js, Express and Bootstrap3

## Security

Work in progress

## Running Instructions
### Via docker

```bash
$ cd management_control_system
$ mvn clean install
$ docker-compose up --build -d
```

#### Usage

Work in progress

#### Results

Work in progress

## References and further reading
    * http://microservices.io/

    * https://www.keycloak.org/docs-api/19.0.1/rest-api/#_users_resource
    * https://medium.com/chain-analytica/keycloak-work-with-client-roles-in-spring-boot-a34d74947c93
    * https://codersee.com/how-to-set-up-keycloak-admin-client-with-spring-boot-and-kotlin/
    * https://hub.docker.com/r/jboss/keycloak

    * https://mac-blog.org.ua/kafka-sasl-ssl
    * https://hub.docker.com/r/wurstmeister/kafka
    * https://hub.docker.com/r/wurstmeister/zookeeper