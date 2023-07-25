package org.acme.cq.quickstart;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("orderService")
public class OrderService {

    private final AtomicInteger counter = new AtomicInteger();

    private final Random amount = new Random();

    private final String[] subjects = { "Camel", "ActiveMQ", "Quarkus" };

    public Order generateOrder() {
        Order order = new Order();
        order.setId(counter.incrementAndGet());
        order.setItem(subjects[counter.get() % subjects.length]);
        order.setAmount(amount.nextInt(10) + 1);
        order.setDescription(order.getItem() + " in Action");
        return order;
    }

    public Order rowToOrder(Map<String, Object> row) {
        Order order = new Order();
        order.setId((Integer) row.get("id"));
        order.setItem((String) row.get("item"));
        order.setAmount((Integer) row.get("amount"));
        order.setDescription((String) row.get("description"));
        order.setProcessed((Boolean) row.get("processed"));
        return order;
    }
}
