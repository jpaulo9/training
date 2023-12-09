package com.aula5.fivespring.services;


import com.aula5.fivespring.exceptions.ResourceNotFounException;
import com.aula5.fivespring.model.Person;
import com.aula5.fivespring.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {


    @Autowired
    private PersonRepository personRepository;
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    public Person findById (Long id){

        logger.info("Uma pessoa");

        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFounException("No records found for this ID"));
    }

    public List<Person> findAll(){
        logger.info("Várias pessoas");


        return personRepository.findAll();
    }


    public Person create(Person person) {
        logger.info("Criado");
        return personRepository.save(person);

    }

    public Person update(Person person) {
        logger.info("Update");

       Person entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFounException("Not found ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return personRepository.save(entity);

    }


    public void delete(Long id) {
        logger.info("Excluído");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFounException("Not found ID"));

            personRepository.delete(entity);
    }
}
