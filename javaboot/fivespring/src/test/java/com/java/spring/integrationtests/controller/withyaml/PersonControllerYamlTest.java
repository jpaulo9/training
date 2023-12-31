package com.java.spring.integrationtests.controller.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.java.spring.config.TestConfigs;
import com.java.spring.integrationtests.controller.withyaml.mapper.YAMLObjtMapper;
import com.java.spring.integrationtests.testcontainers.AbstractIntegrationTest;
import com.java.spring.integrationtests.vo.AccountCredentialsVO;
import com.java.spring.integrationtests.vo.PagedModel.PagedModelPerson;
import com.java.spring.integrationtests.vo.PersonVO;
import com.java.spring.integrationtests.vo.TokenVO;
import com.java.spring.integrationtests.vo.Wrappers.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YAMLObjtMapper objectMapper;

	private static PersonVO personvo;

	@BeforeAll
	public static void setup(){

		objectMapper = new YAMLObjtMapper();

		personvo = new PersonVO();

	}


	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException{

		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

		var acessToken =
				given().
						config(RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
												ContentType.TEXT)))
						.accept(TestConfigs.CONTENT_TYPE_YAML)
						.basePath("/auth/signin")
						.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_YAML)
				.body(user, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class, objectMapper)
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
	public void TnewPerson () throws IOException {

         mockPerson();

		var persistedPerson =
				given().spec(specification)
								.config(RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
												ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YAML)
						.accept(TestConfigs.CONTENT_TYPE_YAML)
						.port(TestConfigs.SERVER_PORT)
						.body(personvo, objectMapper)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);

		personvo = persistedPerson;
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getId()>0);

		assertNotNull("Richard",persistedPerson.getFirstName());
		assertNotNull("Stallman",persistedPerson.getLastName());
		assertNotNull("New York City, US",persistedPerson.getAddress());
		assertNotNull("Male",persistedPerson.getGender());

	}
	@Test
	@Order(2)
	public void TFindById () throws IOException {

		mockPerson();


		var persistedPerson =
				given().spec(specification)
						.config(RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
												ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YAML)
						.accept(TestConfigs.CONTENT_TYPE_YAML)
						.pathParam("id", personvo.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);

		personvo = persistedPerson;
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertEquals(personvo.getId(),persistedPerson.getId());

		assertNotNull("Richard",persistedPerson.getFirstName());
		assertNotNull("Stallman",persistedPerson.getLastName());
		assertNotNull("New York City, US",persistedPerson.getAddress());
		assertNotNull("Male",persistedPerson.getGender());
	}
	@Test
	@Order(3)
	public void TDisabelPerson () throws IOException {

		mockPerson();


		var persistedPerson =
				given().spec(specification)
						.config(RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
												ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YAML)
						.accept(TestConfigs.CONTENT_TYPE_YAML)
						.pathParam("id", personvo.getId())
						.when()
						.patch("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);

		personvo = persistedPerson;
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertEquals(personvo.getId(),persistedPerson.getId());

		assertNotNull("Richard",persistedPerson.getFirstName());
		assertNotNull("Stallman",persistedPerson.getLastName());
		assertNotNull("New York City, US",persistedPerson.getAddress());
		assertNotNull("Male",persistedPerson.getGender());
	}

	@Test
	@Order(4)
	public void TUpdatePerson () throws IOException {

		mockPerson();

		var persistedPerson =
				given().spec(specification)
						.config(RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
												ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YAML)
						.accept(TestConfigs.CONTENT_TYPE_YAML)
						.body(personvo, objectMapper)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);

		personvo = persistedPerson;
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertEquals(personvo.getId(),persistedPerson.getId());

		assertNotNull("Richard",persistedPerson.getFirstName());
		assertNotNull("Stallman",persistedPerson.getLastName());
		assertNotNull("New York City, US",persistedPerson.getAddress());
		assertNotNull("Male",persistedPerson.getGender());

	}

	@Test
	@Order(5)
	public void TDeletePerson () throws IOException {

				given().spec(specification)
						.config(RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
												ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YAML)
						.accept(TestConfigs.CONTENT_TYPE_YAML)
						.pathParam("id", personvo.getId())
						.when()
						.delete("{id}")
						.then()
						.statusCode(204);


	}


	@Test
	@Order(6)
	public void TGetPersons () throws IOException, JsonMappingException, JsonProcessingException {


				var wrapperPersonVO = given().spec(specification)
						.config(RestAssuredConfig.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
												ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YAML)
						.accept(TestConfigs.CONTENT_TYPE_YAML)
						.queryParams("page", 3, "size", 10, "direction", "asc")
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PagedModelPerson.class, objectMapper);


		var persons = wrapperPersonVO.getContent();


		PersonVO person1 = persons.get(0);
		assertNotNull(person1);

		assertEquals(834, person1.getId());
		assertEquals("Aloysia",person1.getFirstName());
		assertEquals("Firman",person1.getLastName());
		assertEquals("2504 Sachtjen Junction",person1.getAddress());
		assertEquals("Female",person1.getGender());
		assertTrue(person1.getEnabled());

	}

	@Test
	@Order(7)
	public void TGetPersonsWithOut () throws IOException, JsonMappingException, JsonProcessingException {
		RequestSpecification specificationErr =
				new RequestSpecBuilder()
						.setBasePath("api/person/v1")
						.setPort(TestConfigs.SERVER_PORT)
						.addFilter(new RequestLoggingFilter(LogDetail.ALL))
						.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
						.build();


         given().spec(specificationErr)
				 .config(RestAssuredConfig.config()
						 .encoderConfig(EncoderConfig.encoderConfig()
								 .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
										 ContentType.TEXT)))
				 .contentType(TestConfigs.CONTENT_TYPE_YAML)
				 .accept(TestConfigs.CONTENT_TYPE_YAML)
				.when()
				.get()
				.then()
				.statusCode(403);

	}
	@Test
	@Order(8)
	public void TGetPersonsByName () throws IOException, JsonMappingException, JsonProcessingException {


		var wrapperPersonVO = given().spec(specification)
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
										ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YAML)
				.accept(TestConfigs.CONTENT_TYPE_YAML)
				.pathParam("firstName", "ayr")
				.queryParams("page", 0, "size", 6, "direction", "asc")
				.when()
				.get("findPersonsByname/{firstName}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PagedModelPerson.class, objectMapper);


		var persons = wrapperPersonVO.getContent();


		PersonVO person1 = persons.get(0);
		assertNotNull(person1);

		assertEquals(1, person1.getId());
		assertEquals("Ayrton",person1.getFirstName());
		assertEquals("Senna",person1.getLastName());
		assertEquals("São Paulo",person1.getAddress());
		assertEquals("Male",person1.getGender());
		assertTrue(person1.getEnabled());

	}


	@Test
	@Order(9)
	public void THateoas () throws IOException, JsonMappingException, JsonProcessingException {


		var content = given().spec(specification)
				.config(RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
										ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YAML)
				.accept(TestConfigs.CONTENT_TYPE_YAML)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();


		assertTrue(content.contains("links:\n" +
				"  - rel: \"self\"\n" +
				"    href: \"http://localhost:8888/api/person/v1/834\""));
		assertTrue(content.contains("- rel: \"next\"\n" +
				"  href: \"http://localhost:8888/api/person/v1?limit=12&direction=asc&page=4&size=12&sort=firstName,asc\""));
		assertTrue(content.contains("- rel: \"last\"\n" +
				"  href: \"http://localhost:8888/api/person/v1?limit=12&direction=asc&page=83&size=12&sort=firstName,asc\""));



	}
	private void mockPerson() {

		personvo.setFirstName("Richard");
		personvo.setLastName("Stallman");
		personvo.setAddress("New York City, US");
		personvo.setGender("Male");
		personvo.setEnabled(true);
	}

}
