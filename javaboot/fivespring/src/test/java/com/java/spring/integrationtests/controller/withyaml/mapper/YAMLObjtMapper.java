package com.java.spring.integrationtests.controller.withyaml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;



public class YAMLObjtMapper implements ObjectMapper {

    private com.fasterxml.jackson.databind.ObjectMapper  objectMapper;
    protected TypeFactory typeFactory;
    public YAMLObjtMapper() {
        objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        typeFactory = TypeFactory.defaultInstance();
    }

    @Override
    public Object deserialize(ObjectMapperDeserializationContext objectMapperDeserializationContext) {
        try {
            String dataToDeserialize = objectMapperDeserializationContext.getDataToDeserialize().asString();
            Class type = (Class) objectMapperDeserializationContext.getType();
            return objectMapper.readValue(dataToDeserialize,typeFactory.constructType(type));

        } catch (JsonProcessingException e) {

            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Object serialize(ObjectMapperSerializationContext objectMapperSerializationContext) {

        try {
            return objectMapper.writeValueAsString(objectMapperSerializationContext.getObjectToSerialize());

        }catch (JsonProcessingException e){

            e.printStackTrace();
        }
        return null;
    }
}
