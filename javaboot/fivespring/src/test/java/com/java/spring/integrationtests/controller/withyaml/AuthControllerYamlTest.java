package com.java.spring.integrationtests.controller.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.java.spring.config.TestConfigs;
import com.java.spring.integrationtests.controller.withyaml.mapper.YAMLObjtMapper;
import com.java.spring.integrationtests.testcontainers.AbstractIntegrationTest;
import com.java.spring.integrationtests.vo.AccountCredentialsVO;
import com.java.spring.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {


    private static YAMLObjtMapper objectMapper;
    private static TokenVO tokenVO;


    @BeforeAll
    public static void setup(){
        objectMapper = new YAMLObjtMapper();
    }
    @Test
    @Order(1)
    public void testSignin() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        RequestSpecification  specification = new RequestSpecBuilder()
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        tokenVO  =
                given().spec(specification)
                        .config(RestAssuredConfig.config()
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
                        .as(TokenVO.class, objectMapper);

        Assertions.assertNotNull(tokenVO.getAccessToken());
        Assertions.assertNotNull(tokenVO.getRefreshToken());


    }


    @Test
    @Order(2)
    public void testRefresh() throws JsonMappingException, JsonProcessingException {

        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

       var newtokenVO  =
                given().config(RestAssuredConfig.config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML,
                                                ContentType.TEXT)))
                        .accept(TestConfigs.CONTENT_TYPE_YAML)
                        .basePath("/auth/refresh")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_YAML)
                        .pathParam("username", tokenVO.getUsername())
                        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer "+ tokenVO.getRefreshToken())
                        .when()
                        .put("{username}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class, objectMapper);

        Assertions.assertNotNull(newtokenVO.getAccessToken());
        Assertions.assertNotNull(newtokenVO.getRefreshToken());


    }


}
