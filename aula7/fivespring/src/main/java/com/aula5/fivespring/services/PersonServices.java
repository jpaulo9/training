package com.aula5.fivespring.services;


import com.aula5.fivespring.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public Person findById (String id){

        logger.info("Uma pessoa");

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("uuu");
        person.setLastName("kkk");
        person.setAddress("Lugar");
        person.setGender("gender");
        return person;
    }

    public List<Person> findAll(){
        logger.info("Várias pessoas");

        List<Person> personList = new ArrayList<>();

        for(int i = 0; i<=8;i++){

            personList.add(mockPerson(i));
        }

        return personList;
    }

    private Person mockPerson(int i) {

        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("uuu"+i);
        person.setLastName("kkk"+i);
        person.setAddress("Lugar"+i);
        person.setGender("gender"+i);
        return person;

    }

    public Person create(Person person) {
        logger.info("Criado");
        return person;

    }

    public Person update(Person person) {
        logger.info("Update");
        return person;

    }


    public void delete(String id) {
        logger.info("Excluído");



    }
}
