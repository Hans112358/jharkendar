package org.jharkendar.rest.tag;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TagDeleteTest extends TagBaseTest {

    @Test
    void delete_tag() {
        String createTagDto = toJson(new CreateTagDto("Important stuff"));

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto)
                .when()
                .post(tagUrl)
                .then();

        String id = extractUuid(response);

        given()
                .when()
                .delete(tagUrl + "/" + id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get(tagUrl)
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void handle_id_not_found() {
        given()
                .when()
                .delete(tagUrl + "/123")
                .then()
                .statusCode(404)
                .body(is("No Tag found for id 123"));
    }

}
