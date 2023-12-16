package com.java.spring.integrationtests.repositroy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.java.spring.config.TestConfigs;
import com.java.spring.integrationtests.testcontainers.AbstractIntegrationTest;
import com.java.spring.integrationtests.vo.PersonVO;
import com.java.spring.model.Person;
import com.java.spring.repository.PersonRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository personRepository;

    private static Person person;

    public static void setup(){
        person = new Person();
    }

    @Test
    @Order(1)
    public void TGetPersonsByName () throws IOException, JsonMappingException, JsonProcessingException {


        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));
        person = personRepository.findPersonsByName("ayr", pageable).getContent().get(0);

        assertEquals(1, person.getId());
        assertEquals("Ayrton",person.getFirstName());
        assertEquals("Senna",person.getLastName());
        assertEquals("São Paulo",person.getAddress());
        assertEquals("Male",person.getGender());
        assertTrue(person.getEnabled());

    }

    @Test
    @Order(2)
    public void TDisablePerson () throws IOException {

        personRepository.disablePerson(person.getId());
        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));
        person = personRepository.findPersonsByName("ayr", pageable).getContent().get(0);



        assertEquals(1, person.getId());
        assertEquals("Ayrton",person.getFirstName());
        assertEquals("Senna",person.getLastName());
        assertEquals("São Paulo",person.getAddress());
        assertEquals("Male",person.getGender());
        assertFalse(person.getEnabled());

        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());




    }
}
