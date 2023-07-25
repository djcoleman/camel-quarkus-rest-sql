package org.acme.cq.quickstart;

import org.apache.camel.builder.RouteBuilder;

public class Backend extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // A first route generates some orders and queue them in DB
        from("timer:new-order?delay=1s&period={{quickstart.generateOrderPeriod:2s}}")
            .routeId("generate-order")
            .bean("orderService", "generateOrder")
            .to("sql:insert into orders (id, item, amount, description, processed) values" +
                "(:#${body.id} , :#${body.item}, :#${body.amount}, :#${body.description}, false)")
            .log("Inserted new order ${body.id}");

        // A second route polls the DB for new orders and processes them
        from("sql:select * from orders where processed = false?" +
                "onConsume=update orders set processed = true where id = :#id&" +
                "delay={{quickstart.processOrderPeriod:5s}}")
            .routeId("process-order")
            .bean("orderService", "rowToOrder")
            .log("Processed order #id ${body.id} with ${body.amount} copies of the '${body.description}' book");

    }
    
}
