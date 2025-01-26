package com.medimap.order;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;



@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceApplicationTests {
	@ServiceConnection
	static MySQLContainer mySQLContainer=new MySQLContainer<>("mysql:8.3.0");

	@LocalServerPort
	private Integer port;

	static {
		mySQLContainer.start();
	}

	@BeforeEach
	void setup(){
		RestAssured.baseURI="http://localhost";
		RestAssured.port=port;
	}



	@Test
	void shouldCreateOrder() {
		String requestBody= """
				{
				    "orderNumber":"abs1232",
				    "skuCode":"Iphone 15 pro max",
				    "quantity":12,
				    "price":123
				}
				""";
		RestAssured.given().contentType("application/json")
				.body(requestBody).when().post("api/order")
				.then()
				.body("orderNumber", Matchers.equalTo("abs1232"))
				.body("skuCode", Matchers.equalTo("Iphone 15 pro max"))
				.body("quantity",Matchers.equalTo(12))
				.body("price",Matchers.equalTo(123));
	}

}
