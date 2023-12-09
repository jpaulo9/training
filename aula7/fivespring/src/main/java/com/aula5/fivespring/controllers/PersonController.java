package com.aula5.fivespring.controllers;


import com.aula5.fivespring.model.Person;
import com.aula5.fivespring.repository.PersonRepository;
import com.aula5.fivespring.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    private PersonServices personServices ;


    @GetMapping(value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getPerson (@PathVariable(value = "id") Long id
                        )throws Exception{

        return personServices.findById(id);
    }
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person newPerson (@RequestBody Person person){

        return personServices.create(person);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person upPerson (@RequestBody Person person){

        return personServices.update(person);
    }
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll (){

        return personServices.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public void deletePerson (@PathVariable(value = "id") Long id){

         personServices.delete(id);
    }









}
