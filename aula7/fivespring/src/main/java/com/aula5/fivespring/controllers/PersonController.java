package com.aula5.fivespring.controllers;


import com.aula5.fivespring.model.Person;
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

    @RequestMapping(value = "/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getPerson (@PathVariable(value = "id") String id
                        )throws Exception{

        return personServices.findById(id);
    }
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person newPerson (@RequestBody Person person){

        return personServices.create(person);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person upPerson (@RequestBody Person person){

        return personServices.update(person);
    }
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll (){

        return personServices.findAll();
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE)
    public void deletePerson (@PathVariable(value = "id") String id){

         personServices.delete(id);
    }









}
