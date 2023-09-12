# Microservice decomposition of Digital Twins for the management of computational Workflows

## Master's degree thesis in Computer Engineering at the University of Florence

The project consists of three microservices developed in **Java 17** following the specifications of **Jakarta EE 10**.

Each microservice has been developed and optimized to run on an **Open Liberty** runtime server on which the
**Hibernate** ORM platform has been installed. The RDBMS used is **MySQL**.
The configurations needed to automatically deploy and run Open Liberty/Hibernate and MySQL on a **Docker**
container are already included with each microservice.

The communication between microservices and between microservices and the outside world is entrusted to the **gRPC**
framework.
Therefore, in order to use microservices as a _black box_, you need to develop a client in one of the programming
languages supported by gRPC using the Protocol Buffers (.proto) file available in the _src/main/proto_ directory
of each project. See the official tutorial of your programming language
(e.g. [the Java tutorial](https://grpc.io/docs/languages/java/basics/)) for further details.

## How to use microservices
1. Clone the repo:
    ```console
    git clone https://github.com/lorenzobucci/Tesi.git
    ```

2. Navigate to the folder of the service you want to build/run:
    ```console
    cd Tesi/[service_name]_service
    ```
   
### Building .war only
1. You can build the _war_ package with Maven using the command (_mvnw_ on **Unix** or _mvnw.cmd_ on **Windows**):
   ```console
    ./mvnw -f pom.xml clean package
    ```

2. You'll find the _war_ file in the target folder.

### Running
Microservices can be run by following one of two approaches defined below:
* Docker containerization (suggested)
* Run on physical machine

#### Docker containerization
In the root folder of each project there are two files: _Dockerfile_ and _docker-compose.yaml_.

In the _Dockerfile_ there are the directives to build a Docker image which, starting from a `openjdk:17` image, contains
Open Liberty and the microservice in question.
The _docker-compose.yaml_ file configures two containers to run: a container with the image built by the
_Dockerfile_; a container with `mysql:latest` containing the MySQL RDBMS. The two containers communicate with each other
through a Docker bridge.

All the necessary operations are already preconfigured, therefore each microservice can be launched only with the command:
```console
docker compose -f docker-compose.yaml up
```

**N.B.** The three microservices are cohesive with each other and need to be able to communicate to operate properly, so we
recommend that you always run all three together.
For reasons related to Docker and the bridge that allows inter-microservices communication, the `mobile-device-service`
microservice must **always** be run before the others.

#### Run on physical machine
In this scenario it's assumed that JDK 17 is installed on the machine and MySQL is already running and reachable
over the network (or on _localhost_).

1. Depending on your operating system (or your IDE if Maven is running from a development environment) configure the 
following environment variables:
   * GRPC_PORT: Open Liberty listening HTTP port
   * MYSQL_HOSTNAME: MySQL hostname
   * MYSQL_PORT: MySQL listening port
   * DB_NAME: MySQL database name
   * MYSQL_USERNAME: MySQL username
   * MYSQL_PASSWORD: MySQL password

   The `mobile-device-service` module also needs the following additional variables:
   * SVC_MGMT_HOSTNAME: hostname of the node where `services-management-service` is running
   * SVC_MGMT_PORT: listening port of the node running `services-management-service`

   The `services-management-service` module also needs the following additional variables:
   * RES_MGMT_HOSTNAME: hostname of the node where `resources-management-service` is running
   * RES_MGMT_PORT: listening port of the node running `resources-management-service`

2. Perform a cleanup operation, compile the source code and generate gRPC code (_mvnw_ on **Unix** or _mvnw.cmd_ on **Windows**):
      ```console
    ./mvnw -f pom.xml clean compile
    ```

3. Run the microservice with Open Liberty:
      ```console
    ./mvnw -f pom.xml liberty:run
    ```

## Testing
You can run E2E tests to verify the correct execution of microservices and their interaction in a simulation scenario.
JDK 17 must be installed on the machine where the tests will be performed.

1. Clone the repo:
    ```console
    git clone https://github.com/lorenzobucci/Tesi.git
    ```
   
2. For each microservice, access the corresponding directory:
    ```console
    cd Tesi/[service_name]_service
    ```
   1. Edit the _docker-compose.yaml_ file to enable port 908x exposure for the `open-liberty` service. 
   The necessary instructions are already contained in the file, you just need to uncomment them.

   2. Follow the instructions in the [Docker containerization](#docker-containerization) section.

3. Navigate to the E2E test folder:
   ```console
   cd Tesi/e2e_test
    ```
   
4. Run the tests with the command (_mvnw_ on **Unix** or _mvnw.cmd_ on **Windows**):
   ```console
   ./mvnw -f pom.xml clean test
    ```
