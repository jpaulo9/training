package com.java.spring.integrationtests.vo.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.spring.integrationtests.vo.PersonVO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class PersonEmmbeddedVO implements Serializable {


    private static final long serialVersionUID = 1L;


    @JsonProperty("personVOList")
    private List<PersonVO> persons;

    public PersonEmmbeddedVO() {
    }

    public List<PersonVO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonVO> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonEmmbeddedVO that)) return false;
        return Objects.equals(getPersons(), that.getPersons());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersons());
    }
}
