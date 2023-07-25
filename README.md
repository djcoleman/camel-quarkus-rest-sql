# Camel Quarkus REST SQL Example
A Camel Quarkus example that demonstrates how to use SQL via JDBC along with Camel's REST DSL to expose a RESTful API. This example was ported to the Camel Quarkus platform from https://github.com/fabric8-quickstarts/spring-boot-camel-rest-sql.

## What Does It Do?

A back-end service runs a Camel route that periodically creates new `Book` orders and stores them in a database table. Another Camel route then polls the database for new orders and proccesses them.

A front-end REST API is made available to query the list of unique books in the database, and retrieve details of a particular order.

## Running the Example

You can either run the example locally, or remotely on a Kubernetes/OpenShift node.

### Running Locally

To run the example locally, you need the following pre-requisites:

  * JDK 17+
  * Maven 3.8+

You can build and run the example in `dev` mode with the following command:

```bash
mvn quarkus:dev
```

Running in `dev` mode means that any changes made to the sources will automatically get rebuilt and redeployed when they're saved. Also, in this example Quarkus will automatically launch a MariaDB database and load the schema.

The first build may take a few minutes to download the dependencies and fetch the database container, but you should start to see log messages similar to the following:

```
2023-07-25 13:58:56,237 INFO  [generate-order] (Camel (camel-1) thread #2 - timer://new-order) Inserted new order 1
2023-07-25 13:59:06,167 INFO  [generate-order] (Camel (camel-1) thread #2 - timer://new-order) Inserted new order 2
2023-07-25 13:59:16,164 INFO  [generate-order] (Camel (camel-1) thread #2 - timer://new-order) Inserted new order 3
2023-07-25 13:59:26,170 INFO  [generate-order] (Camel (camel-1) thread #2 - timer://new-order) Inserted new order 4
2023-07-25 13:59:26,247 INFO  [process-order] (Camel (camel-1) thread #1 - sql://select%20*%20from%20orders%20where%20processed%20=%20false) Processed order #id 1 with 9 copies of the 'ActiveMQ in Action' book
```

The following REST endpoints will be available when the example is running:

  * http://localhost:8080/camel-rest-sql/books/ - The list of unique book titles.
  * http://localhost:8080/camel-rest-sql/books/order/1 - The details of the first order.
  * http://localhost:8080/camel-rest-sql/api-doc - The OpenAPI documentation of the available endpoints.

### Deploying on Kubernetes/OpenShift with Odo

[Odo](https://odo.dev/) is a CLI tool that simplifies the workflow when working with containerised applications running on Kubernetes or OpenShift.

Pre-requisites:

  * [Odo](https://odo.dev/docs/overview/installation)
  * A Kubernetes or OpenShift node/cluster, for example `minikube` running locally, or a free account with Red Hat's [Developer Sandbox](https://developers.redhat.com/developer-sandbox).

On Kubernetes, first create a namespace where the project will be deployed:
```bash
odo create namespace camel-quarkus-rest-sql
```

The `odo create namespace` command creates a Kubernetes namespace and sets that as the default.

To run the application in development mode, just run the following command:

```bash
odo dev --port-forward 8080:8080
```

The `odo dev` command is driven by the contents of the `.devfile.yaml` file in the root directory. The devfile format is described at https://devfile.io, but there are two key sections for this deployment. The `component` sections describe containers that will be started, and the `command` sections describe which commands will be run in the containers. There are two container components for this project - a `java` component that will run the `mvn` command to build and run the example, and a `mariadb` component that will run the database server.

The `prepare-database` command populates the database with the contents of `src/main/resources/schema.sql`, which creates the `orders` table used by the example. This command is run during the build phase.

The second command is the `mvn quarkus:dev` command that builds and starts the example, and is executed during the run phase.

Finally, the `--port-forward 8080:8080` option maps port 8080 on the pod to the same port number on your local machine so that you can access the endpoints using the same URLs as if running locally.

