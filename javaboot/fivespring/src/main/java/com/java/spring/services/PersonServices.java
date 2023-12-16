package com.java.spring.services;


import com.java.spring.controllers.PersonController;
import com.java.spring.data.vo.v1.PersonVO;
import com.java.spring.exceptions.RequireObjectIsNullException;
import com.java.spring.exceptions.ResourceNotFounException;
import com.java.spring.mapper.DozerMapper;
import com.java.spring.model.Person;
import com.java.spring.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {


    @Autowired
    private PersonRepository personRepository;
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    private PagedResourcesAssembler<PersonVO> assembler;

    public PersonVO findById (Long id) throws Exception {

        logger.info("Uma pessoa");

                var entity =personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFounException("No records found for this ID"));

        PersonVO personvo = DozerMapper.parseObject(entity, PersonVO.class);
        personvo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personvo;
    }

    @Transactional
    public PersonVO disabelPerson (Long id) throws Exception {

        logger.info("disabilitar uma pessoa");
        personRepository.disablePerson(id);

        var entity =personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFounException("No records found for this ID"));

        PersonVO personvo = DozerMapper.parseObject(entity, PersonVO.class);
        personvo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personvo;
    }

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        logger.info("Várias pessoas");

        var personPage = personRepository.findAll(pageable);
        var personVOpage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));

        personVOpage.map(p -> {
            try {
                return p.add(linkTo(methodOn(PersonController.class)
                                .findById(p.getKey()))
                                .withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        "asc")).withSelfRel();
        return assembler.toModel( personVOpage, link);

    }
    public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable) {
        logger.info("Várias pessoas");

        var personPage = personRepository.findPersonsByName(firstName, pageable);
        var personVOpage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));

        personVOpage.map(p -> {
            try {
                return p.add(linkTo(methodOn(PersonController.class)
                        .findById(p.getKey()))
                        .withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        "asc")).withSelfRel();
        return assembler.toModel( personVOpage, link);

    }


    public PersonVO create(PersonVO person) throws Exception {
        logger.info("Criado");
        if (person == null) throw new RequireObjectIsNullException();

        var entity = DozerMapper.parseObject(person, Person.class);

        PersonVO vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) throws Exception {
        logger.info("Update");

        if (person == null) throw new RequireObjectIsNullException();

       var entity = personRepository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFounException("Not found ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        PersonVO vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;

    }


    public void delete(Long id) {
        logger.info("Excluído");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFounException("Not found ID"));

            personRepository.delete(entity);
    }
}
