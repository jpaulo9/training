package com.java.spring.mockito.services;

import com.java.spring.data.vo.v1.PersonVO;
import com.java.spring.exceptions.RequireObjectIsNullException;
import com.java.spring.model.Person;
import com.java.spring.repository.PersonRepository;
import com.java.spring.services.PersonServices;
import com.java.spring.unittests.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices services;

    @Mock
    PersonRepository personRepository;

    @BeforeEach
    void setUpMock() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(input);
    }

    @Test
    void findByIdTest() throws Exception {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(entity));

        var result = services.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        System.out.println(result.toString());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));

        //equals
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());

    }

    @Test
    void findAll() {
        List<Person> entitys = input.mockEntityList();
        when(personRepository.findAll()).thenReturn((entitys));

        var persons = services.findAll();

       assertNotNull(persons);
       assertEquals(14, persons.size());

       var personOne= persons.get(1);
        assertNotNull(personOne);
        assertNotNull(personOne.getKey());
        assertNotNull(personOne.getLinks());

        assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));

        //equals
        assertEquals("Addres Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());

        var personFour= persons.get(4);
        assertNotNull(personFour);
        assertNotNull(personFour.getKey());
        assertNotNull(personFour.getLinks());

        assertTrue(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));

        //equals
        assertEquals("Addres Test4", personFour.getAddress());
        assertEquals("First Name Test4", personFour.getFirstName());
        assertEquals("Last Name Test4", personFour.getLastName());
        assertEquals("Male", personFour.getGender());

        var personSeven= persons.get(7);
        assertNotNull(personSeven);
        assertNotNull(personSeven.getKey());
        assertNotNull(personSeven.getLinks());
        assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));

        //equals
        assertEquals("Addres Test7", personSeven.getAddress());
        assertEquals("First Name Test7", personSeven.getFirstName());
        assertEquals("Last Name Test7", personSeven.getLastName());
        assertEquals("Female", personSeven.getGender());

    }

    @Test
    void createTest() throws Exception {

        Person entity = input.mockEntity(1);
        Person persisted = entity;
        persisted.setId(1L);

        PersonVO personVO = input.mockVO(1);
        personVO.setKey(1L);

        when(personRepository.save(entity)).thenReturn(persisted);
        var result = services.create(personVO);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));

        //equals
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());

    }

    @Test
    void updateTest() throws Exception {

        Person entity = input.mockEntity(1);
        Person persisted = entity;
        persisted.setId(1L);

        PersonVO personVO = input.mockVO(1);
        personVO.setKey(1L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(entity));

        when(personRepository.save(entity)).thenReturn(persisted);
        var result = services.update(personVO);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));

        //equals
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void delete() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(entity));

        services.delete(1L);
    }

    @Test
    void testCreatWithNullPerson (){
        Exception exception = assertThrows(RequireObjectIsNullException.class, ()->{
            services.create(null);
        });
        String expectedMsg = "Não é permitido a persistência de um objeto nulo";
        String actualMsg = exception.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }
    @Test
    void testUpdateWithNullPerson (){
        Exception exception = assertThrows(RequireObjectIsNullException.class, ()->{
            services.update(null);
        });
        String expectedMsg = "Não é permitido a persistência de um objeto nulo";
        String actualMsg = exception.getMessage();
        assertTrue(actualMsg.contains(expectedMsg));
    }
}