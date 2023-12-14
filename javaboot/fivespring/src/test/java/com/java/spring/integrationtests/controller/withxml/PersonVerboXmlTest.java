package com.java.spring.integrationtests.controller.withxml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.java.spring.config.TestConfigs;
import com.java.spring.integrationtests.testcontainers.AbstractIntegrationTest;
import com.java.spring.integrationtests.vo.AccountCredentialsVO;
import com.java.spring.integrationtests.vo.PersonVO;
import com.java.spring.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonVerboXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonVO personvo;

	@BeforeAll
	public static void setup(){

		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		personvo = new PersonVO();

	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException{

		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

		var acessToken =
				given().basePath("/auth/signin")
						.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class)
						.getAccessToken();
		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + acessToken)
						.setBasePath("api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();

	}

	@Test
	@Order(1)
	public void testCreate () throws IOException {

		mockPerson();


		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_OK)
						.body(personvo)
				.when()
						.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		personvo = persistedPerson;
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId()>0);

		assertNotNull("Richard",persistedPerson.getFirstName());
		assertNotNull("Stallman",persistedPerson.getLastName());
		assertNotNull("New York City, US",persistedPerson.getAddress());
		assertNotNull("Male",persistedPerson.getGender());
	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin () throws IOException {

		mockPerson();

		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_INVALID)
							.body(personvo)
								.when()
									.post()
										.then()
											.statusCode(403)
												.extract()
													.body()
														.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);

	}


	@Test
	@Order(3)
	public void testFindById () throws IOException {

		mockPerson();


		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_OK)
						.pathParam("id", personvo.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		personvo = persistedPerson;
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId()>0);

		assertNotNull("Richard",persistedPerson.getFirstName());
		assertNotNull("Stallman",persistedPerson.getLastName());
		assertNotNull("New York City, US",persistedPerson.getAddress());
		assertNotNull("Male",persistedPerson.getGender());
	}
	@Test
	@Order(4)
	public void testFindByIdWithWrongOrigin () throws IOException {

		mockPerson();


		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_INVALID)
						.pathParam("id", personvo.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);

	}

	private void mockPerson() {

		personvo.setFirstName("Richard");
		personvo.setLastName("Stallman");
		personvo.setAddress("New York City, US");
		personvo.setGender("Male");
	}

}
