package com.java.spring.integrationtests.vo.Wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class WrapperPersonVO implements Serializable {


    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmmbeddedVO emmbeddedVO;

    public WrapperPersonVO() {
    }

    public PersonEmmbeddedVO getEmmbeddedVO() {
        return emmbeddedVO;
    }

    public void setEmmbeddedVO(PersonEmmbeddedVO emmbeddedVO) {
        this.emmbeddedVO = emmbeddedVO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WrapperPersonVO that)) return false;
        return Objects.equals(getEmmbeddedVO(), that.getEmmbeddedVO());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmmbeddedVO());
    }
}
