package com.medimap.microservices.product;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
		// Create the product
		String createRequestBody = """
        {
            "id": "678362fd74800b1153a1e1b0",
            "name": "Aspirine",
            "description": "Painkiller tablet",
            "price": 100.00
        }
    """;
		Response createResponse = RestAssured.given()
				.contentType("application/json")
				.body(createRequestBody)
				.when()
				.post("/api/product/add")
				.then()
				.statusCode(201)
				.extract()
				.response();

		System.out.println("Create Response: " + createResponse.asString());
		String createdProductId =
				RestAssured.given()
						.contentType("application/json")
						.body(createRequestBody)
						.when()
						.post("/api/product/add")
						.then()
						.statusCode(201)
						.extract()
						.path("id");
		// Verify product exists
		RestAssured.given()
				.when()
				.get("/api/product/"+createdProductId)
				.then()
				.statusCode(200)
				.body("name", Matchers.equalTo("Aspirine"));

		// Define the updated product details
		String updateRequestBody = """
        {
            "name": "Aspirine1234",
            "description": "Headache tablet",
            "price": 120.00
        }
    """;

		// Update the product
		RestAssured.given()
				.contentType("application/json")
				.body(updateRequestBody)
				.when()
				.put("/api/product/update/678362fd74800b1153a1e1b0")
				.then()
				.statusCode(200)
				.body("id", Matchers.equalTo("678362fd74800b1153a1e1b0"))
				.body("name", Matchers.equalTo("Aspirine1234"))
				.body("description", Matchers.equalTo("Headache tablet"))
				.body("price", Matchers.equalTo(120.00));
	}

}
