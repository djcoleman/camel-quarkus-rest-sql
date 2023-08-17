package org.acme.cq.quickstart;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@QuarkusTest
public class BookApiTest {
    
    @Test
    public void testBooksEndpoint() throws Exception {
        Thread.sleep(20000);

        TypeRef<List<Book>> typeRef = new TypeRef<>() {};

        List<Book> books = given()
            .when().get("/camel-rest-sql/books/")
            .then()
            .statusCode(200)
            .extract().as(typeRef);

        assertEquals(3, books.size()); 
        assertEquals("ActiveMQ in Action", books.get(0).getDescription());
        assertEquals("Quarkus in Action", books.get(1).getDescription());
        assertEquals("Camel in Action", books.get(2).getDescription());
    }
}
