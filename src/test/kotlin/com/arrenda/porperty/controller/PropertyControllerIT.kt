package com.arrenda.porperty.controller

import com.arrenda.common.BaseIT
import com.arrenda.porperty.dto.PropertyCreateDTO
import com.arrenda.porperty.dto.PropertyUpdateDTO
import com.arrenda.porperty.dto.PropertyViewDTO
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import org.apache.http.HttpStatus.SC_NOT_FOUND
import org.apache.http.HttpStatus.SC_OK
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.Test
import java.util.*


class PropertyControllerIT : BaseIT() {

    @Test
    fun `Should create property`() {
        val propertyCreateDTO = PropertyCreateDTO("test description")
        createProperty(propertyCreateDTO)
    }

    @Test
    fun `Should find by id`() {
        val propertyCreateDTO = PropertyCreateDTO("test description")
        val property = createProperty(propertyCreateDTO)

        given()
            .`when`()
            .get("/properties/${property.id}")
            .then()
            .statusCode(SC_OK)
            .body("id", equalTo(property.id.toString()))
            .body("description", equalTo(propertyCreateDTO.description))
            .body("createdDate", notNullValue())
    }

    @Test
    fun `Should not find by id`() {
        given()
            .`when`()
            .get("/properties/{id}", UUID.fromString("9baf7b7d-5ac7-4b91-8058-71df99283c79"))
            .then()
            .statusCode(SC_NOT_FOUND)
            .body("message", equalTo("Property with ID 9baf7b7d-5ac7-4b91-8058-71df99283c79 not found"))
    }

    @Test
    fun `Should find all`() {
        val propertyCreateDTO1 = PropertyCreateDTO("test description 1")
        val property1 = createProperty(propertyCreateDTO1)

        val propertyCreateDTO2 = PropertyCreateDTO("test description 2")
        val property2 = createProperty(propertyCreateDTO2)

        given()
            .`when`()
            .get("/properties")
            .then()
            .statusCode(SC_OK)
            .body("content.size()", equalTo(2))
            .body("content.id", hasItems(property1.id.toString(), property2.id.toString()))
    }

    @Test
    fun `Should find all paginated`() {
        val propertyCreateDTO1 = PropertyCreateDTO("test description 1")
        val property1 = createProperty(propertyCreateDTO1)

        val propertyCreateDTO2 = PropertyCreateDTO("test description 2")
        createProperty(propertyCreateDTO2)

        given()
            .`when`()
            .queryParam("pageSize", "1")
            .get("/properties")
            .then()
            .statusCode(SC_OK)
            .body("content.size()", equalTo(1))
            .body("content.id", hasItems(property1.id.toString()))
    }

    @Test
    fun `Should find all filtered by description`() {
        val propertyCreateDTO = PropertyCreateDTO("test description")
        val property = createProperty(propertyCreateDTO)

        createProperty(PropertyCreateDTO("Should not return"))

        given()
            .`when`()
            .queryParam("description", "test")
            .get("/properties")
            .then()
            .statusCode(SC_OK)
            .body("content.size()", equalTo(1))
            .body("content.id", hasItems(property.id.toString()))
    }

    @Test
    fun `Should update property`() {
        val propertyCreateDTO = PropertyCreateDTO("test description")
        val property = createProperty(propertyCreateDTO)

        val propertyUpdateDTO = PropertyUpdateDTO(property.id, "updated description")
        updateProperty(propertyUpdateDTO)

        given()
            .`when`()
            .get("/properties/${property.id}")
            .then()
            .statusCode(SC_OK)
            .body("id", equalTo(property.id.toString()))
            .body("description", equalTo(propertyUpdateDTO.description))
            .body("createdDate", notNullValue())
    }

    fun createProperty(propertyCreateDTO: PropertyCreateDTO): PropertyViewDTO {
        return given()
            .contentType(JSON)
            .body(gson.toJson(propertyCreateDTO))
            .`when`()
            .post("/properties")
            .then()
            .statusCode(SC_OK)
            .body("id", notNullValue())
            .body("description", equalTo(propertyCreateDTO.description))
            .body("createdDate", notNullValue())
            .extract()
            .body()
            .`as`(PropertyViewDTO::class.java)
    }

    fun updateProperty(propertyUpdateDTO: PropertyUpdateDTO) {
        given()
            .contentType(JSON)
            .body(gson.toJson(propertyUpdateDTO))
            .`when`()
            .patch("/properties")
            .then()
            .statusCode(SC_OK)
            .body("id", notNullValue())
            .body("description", equalTo(propertyUpdateDTO.description))
            .body("createdDate", notNullValue())
    }
}