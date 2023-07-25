package org.acme.cq.quickstart;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RestApi extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
            .contextPath("/camel-rest-sql").apiContextPath("/api-doc")
            .apiProperty("api.title", "Camel REST API")
            .apiProperty("api.version", "1.0")
            .apiProperty("cors", "true")
            .apiContextRouteId("doc-api")
            .bindingMode(RestBindingMode.json);

        rest("/books").description("Books REST service")
            .get("/").description("The list of all the books")
                .routeId("books-api")
                .to("sql:select distinct description from orders?outputClass=org.acme.cq.quickstart.Book")
            .get("order/{id}").description("Details of an order by id")
                .routeId("order-api")
                .to("sql:select * from orders where id = :#${header.id}?outputType=SelectOne&outputClass=org.acme.cq.quickstart.Order");
    }
    
}
