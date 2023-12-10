package com.java.spring.controllers;

import com.java.spring.data.vo.v1.PersonVO;
import com.java.spring.mockito.services.PersonServices;
import com.java.spring.util.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "api/person/v1")
@Tag(name = "Pessoas", description = "Endpoints para controle de dados de pessoas")
public class PersonController {

    @Autowired
    private PersonServices personServices ;


    @GetMapping(value = "/{id}",
    produces = {MediaTypes.APPLICATION_JSON, MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML})
    @Operation(summary = "Get a Person", description = "Get a Person",
      tags = {"Pessoas"},
      responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content =@Content (schema = @Schema(implementation = PersonVO.class)) ),//status ok
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),


      } )
    public PersonVO findById (@PathVariable(value = "id") Long id
                        )throws Exception{

        return personServices.findById(id);
    }
    @PostMapping(
            consumes = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML},
            produces = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML})
    @Operation(summary = "New Person", description = "New Person",
            tags = {"Pessoas"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =@Content (schema = @Schema(implementation = PersonVO.class)) ),//status ok
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),


            } )
    public PersonVO newPerson (@RequestBody PersonVO person) throws Exception {

        return personServices.create(person);
    }

    @PutMapping(
            consumes = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML},
            produces = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML})
    @Operation(summary = "Update a Person", description = "Update a Person",
            tags = {"Pessoas"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content =@Content (schema = @Schema(implementation = PersonVO.class)) ),//status ok
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),


            } )
    public PersonVO upPerson (@RequestBody PersonVO person) throws Exception {

        return personServices.update(person);
    }
    @GetMapping(
            produces = {MediaTypes.APPLICATION_JSON,MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_YAML})
    @Operation(summary = "Get all person", description = "Get all person",
            tags = {"Pessoas"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content ={
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                    )
                            } ),//status ok

                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),


            } )
    public List<PersonVO> findAll (){

        return personServices.findAll();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Person", description = "Delete a Person",
            tags = {"Pessoas"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),//status ok
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),


            } )
    public ResponseEntity<?> deletePerson (@PathVariable(value = "id") Long id){


         personServices.delete(id);
         return ResponseEntity.noContent().build();
    }









}
