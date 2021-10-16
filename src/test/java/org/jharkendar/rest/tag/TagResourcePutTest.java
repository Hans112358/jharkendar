package org.jharkendar.rest.tag;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.jharkendar.rest.tag.CreateTagDto;
import org.jharkendar.rest.tag.UpdateTagDto;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TagResourcePutTest extends TagBaseTest {

    @Test
    void update_name() {
        String createTagDto = toJson(new CreateTagDto("Important stuff"));

        ValidatableResponse responsePost = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto)
                .when()
                .post(tagUrl)
                .then();

        String id = extractUuid(responsePost);

        String updateTagDto = toJson(new UpdateTagDto("Other stuff"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateTagDto)
                .when()
                .put(tagUrl + "/" + id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get(tagUrl + "/" + id)
                .then()
                .statusCode(200)
                .body(is("{\"id\":\"" + id + "\",\"name\":\"Other stuff\"}"));
    }

    @Test
    void not_update_when_id_is_invalid() {

        String updateTagDto = toJson(new UpdateTagDto("Other stuff"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateTagDto)
                .when()
                .put(tagUrl + "/123")
                .then()
                .statusCode(404)
                .body(is("No entity found for id 123"));

    }

    @Test
    void not_update_when_name_is_invalid() {

        String updateTagDto = toJson(new UpdateTagDto(null));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateTagDto)
                .when()
                .put(tagUrl + "/123")
                .then()
                .statusCode(400)
                .body(containsString("name cannot be empty"));

    }


}
