package com.medimap.microservices.product;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer= new MongoDBContainer("mongo:latest");

	@LocalServerPort
	private Integer port;


	@BeforeEach
	void setup(){
		RestAssured.baseURI="http://localhost";
		RestAssured.port=port;
	}

	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String requestBody= """
				{
				    "id": "67836eb0c7734e74041a1a57",
				    "name": "Paracetamol",
				    "description": "Fever tablet",
				    "price": 100
				}
		""";
		RestAssured.given().contentType("application/json")
				.body(requestBody).when().post("/api/product/add")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name",Matchers.equalTo("Paracetamol"))
				.body("description",Matchers.equalTo("Fever tablet"))
				.body("price",Matchers.equalTo(100));

	}

	@Test

	void shouldUpdateProduct() {
		// Define the ID of the product to be updated
		String productId = "6783605e4341bb67e2dab5f2";

		// Define the request body with updated product details
		String requestBody = """
       		{
				 "name": "Aspirine1234",
				 "description": "Headache tablet",
				 "price": 100
			}
    """;

		// Perform the PUT request
		RestAssured.given()
				.contentType("application/json") // Set the content type to JSON
				.body(requestBody)              // Attach the updated product details
				.when()
				.put("/api/product/update/" + productId) // Send the PUT request to the update endpoint
				.then()
				.body("id", Matchers.equalTo(productId)) // Validate the product ID in the response
				.body("name", Matchers.equalTo("Aspirine1234")) // Validate the updated name
				.body("description", Matchers.equalTo("Headache tablet")) // Validate the updated description
				.body("price", Matchers.equalTo(120)); // Validate the updated price
	}


}
