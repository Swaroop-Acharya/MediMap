package com.medimap.order;

import com.medimap.order.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import static org.hamcrest.MatcherAssert.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)  // Automatically set up WireMock on a random port
class OrderServiceApplicationTests {

    // Declare MySQLContainer with @ServiceConnection
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0");

    @LocalServerPort
    private Integer port;

    // Spring Boot will manage the lifecycle of the container
    static {
        mySQLContainer.start(); // This will be managed by Spring Boot, no need for manual start in static block
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldCreateOrder() {
        // Request body for creating an order
        String requestBody = """
                {
                    "orderNumber":"abs1232",
                    "skuCode":"Derma Facewash",
                    "quantity":12,
                    "price":123
                }
                """;

        // Set up a stub for the inventory service with WireMock
        InventoryClientStub.stubInventoryCall("Derma Facewash", 12);

        // Make the actual API request using RestAssured
        String responseBodyString = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("api/order")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        // Validate the response body
        assertThat(responseBodyString, Matchers.is("Order placed successfully!"));
    }
}
