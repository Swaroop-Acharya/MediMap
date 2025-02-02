package com.medimap.inventory;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer=new MySQLContainer<>("mysql:8.3.0");

	@LocalServerPort
	private Integer port;

	static{
		mySQLContainer.start();
	}

	@BeforeEach
	void setup(){
		RestAssured.baseURI="http://localhost";
		RestAssured.port=this.port;

		// Add inventory before each test, so stock check works
		String requestBody= """
            {
                "skuCode":"DermaCo. Sunscreen",
                "quantity":100
            }
            """;
		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("api/inventory");

	}


	@Test
	void shoudlCreateInventory() {
		String requestBody= """
				{
				    "skuCode":"DermaCo. Sunscreen",
				    "quantity":100
				}
				""";
		RestAssured.given().contentType("application/json")
				.body(requestBody).when().post("api/inventory")
				.then()
				.body("skuCode", Matchers.equalTo("DermaCo. Sunscreen"))
				.body("quantity",Matchers.equalTo(100));
	}

	@Test
	void checksForStockStatus(){
		boolean response= RestAssured.given().queryParam("skuCode","DermaCo. Sunscreen")
				.queryParam("quantity",100)
				.when()
				.get("api/inventory/isInStock")
				.then()
				.statusCode(200)
				.log().all()
				.extract().response().as(Boolean.class);
		boolean negativeResponse= RestAssured.given().queryParam("skuCode","DermaCo. Sunscreen")
				.queryParam("quantity",1000)
				.when()
				.get("api/inventory/isInStock")
				.then()
				.statusCode(200)
				.log().all()
				.extract().response().as(Boolean.class);

		Assertions.assertTrue(response);
		Assertions.assertFalse(negativeResponse);

	}

}
