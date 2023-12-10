package com.java.spring.controllers;


import com.java.spring.data.vo.v1.PersonVO;
import com.java.spring.mockito.services.PersonServices;
import com.java.spring.util.MediaTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/person/v1")
public class PersonController {

    @Autowired
    private PersonServices personServices ;

    @GetMapping(value = "/{id}",
    produces = {MediaTypes.APPLICATION_JSON, MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML})
    public PersonVO findById (@PathVariable(value = "id") Long id
                        )throws Exception{

        return personServices.findById(id);
    }
    @PostMapping(
            consumes = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML},
            produces = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML})
    public PersonVO newPerson (@RequestBody PersonVO person) throws Exception {

        return personServices.create(person);
    }

    @PutMapping(
            consumes = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML},
            produces = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML})
    public PersonVO upPerson (@RequestBody PersonVO person) throws Exception {

        return personServices.update(person);
    }
    @GetMapping(
            produces = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML})
    public List<PersonVO> findAll (){

        return personServices.findAll();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deletePerson (@PathVariable(value = "id") Long id){


         personServices.delete(id);
    }









}
