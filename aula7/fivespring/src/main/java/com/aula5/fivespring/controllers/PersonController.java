package com.aula5.fivespring.controllers;


import com.aula5.fivespring.data.vo.v1.PersonVO;
import com.aula5.fivespring.model.Person;
import com.aula5.fivespring.repository.PersonRepository;
import com.aula5.fivespring.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/person/v1")
public class PersonController {

    @Autowired
    private PersonServices personServices ;


    @GetMapping(value = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonVO getPerson (@PathVariable(value = "id") Long id
                        )throws Exception{

        return personServices.findById(id);
    }
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonVO newPerson (@RequestBody PersonVO person){

        return personServices.create(person);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonVO upPerson (@RequestBody PersonVO person){

        return personServices.update(person);
    }
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonVO> findAll (){

        return personServices.findAll();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePerson (@PathVariable(value = "id") Long id){


         personServices.delete(id);
    }









}
