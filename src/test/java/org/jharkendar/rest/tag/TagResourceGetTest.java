package org.jharkendar.rest.tag;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TagResourceGetTest extends TagBaseTest {

    @Test
    void get_by_name() {
        String createTagDto1 = toJson(new CreateTagDto("Important stuff"));
        String createTagDto2 = toJson(new CreateTagDto("Other stuff"));

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto1)
                .when()
                .post(tagUrl)
                .then();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto2)
                .when()
                .post(tagUrl)
                .then();

        String id = extractUuid(response);

        given()
                .when()
                .get(tagUrl + "/" + id)
                .then()
                .statusCode(200)
                .body(is("{\"id\":\"" + id + "\",\"name\":\"Important stuff\"}"));
    }

    @Test
    void handle_not_existing_tag_name() {
        String createTagDto = toJson(new CreateTagDto("Important stuff"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto)
                .when()
                .post(tagUrl)
                .then();

        given()
                .when()
                .get(tagUrl + "/123")
                .then()
                .statusCode(404)
                .body(is("No Tag found for id 123"));
    }
}
