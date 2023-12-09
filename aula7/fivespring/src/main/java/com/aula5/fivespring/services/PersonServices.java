package com.aula5.fivespring.services;


import com.aula5.fivespring.data.vo.v1.PersonVO;
import com.aula5.fivespring.exceptions.ResourceNotFounException;
import com.aula5.fivespring.mapper.DozerMapper;
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

    public PersonVO findById (Long id){

        logger.info("Uma pessoa");

                var entity =personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFounException("No records found for this ID"));

        var personvo = DozerMapper.parseObject(entity, PersonVO.class);
        return personvo;
    }

    public List<PersonVO> findAll(){
        logger.info("Várias pessoas");


        return DozerMapper.parseListObjects(personRepository.findAll(), PersonVO.class);
    }


    public PersonVO create(PersonVO person) {
        logger.info("Criado");
        var entity = DozerMapper.parseObject(person, Person.class);

        var vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);

        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Update");

       var entity = personRepository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFounException("Not found ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
        return vo;

    }


    public void delete(Long id) {
        logger.info("Excluído");

        var entity = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFounException("Not found ID"));

            personRepository.delete(entity);
    }
}
