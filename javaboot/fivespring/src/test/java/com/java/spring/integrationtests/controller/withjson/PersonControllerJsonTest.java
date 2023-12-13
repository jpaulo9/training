package com.java.spring.integrationtests.controller.withjson;

import com.java.spring.config.TestConfigs;
import com.java.spring.integrationtests.testcontainers.AbstractIntegrationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.java.spring.integrationtests.vo.PersonVO;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

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
	@Order(1)
	public void testCreate () throws IOException {

		mockPerson();

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_OK)
						.setBasePath("api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
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

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_INVALID)
							.setBasePath("api/person/v1")
								.setPort(TestConfigs.SERVER_PORT)
									.addFilter(new RequestLoggingFilter(LogDetail.ALL))
										.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
											.build();
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
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

		System.out.println(" "+personvo.getId());
		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_OK)
						.setBasePath("api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
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

		specification =
				new RequestSpecBuilder()
						.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_INVALID)
						.setBasePath("api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
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
