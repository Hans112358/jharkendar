package org.jharkendar.rest.tag;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TagResourceGetAllTest extends TagBaseTest {

    @Test
    void get_two_items() {
        String createTagDto1 = toJson(new CreateTagDto("Important stuff"));
        String createTagDto2 = toJson(new CreateTagDto("Other stuff"));

        ValidatableResponse response1 = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto1)
                .when()
                .post(tagUrl)
                .then();

        ValidatableResponse response2 = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto2)
                .when()
                .post(tagUrl)
                .then();

        String id1 = extractUuid(response1);
        String id2 = extractUuid(response2);

        given()
                .when()
                .get(tagUrl)
                .then()
                .statusCode(200)
                .body(containsString("{\"id\":\"" + id1 + "\",\"name\":\"Important stuff\"}"))
                .body(containsString("{\"id\":\"" + id2 + "\",\"name\":\"Other stuff\"}"));
    }

    @Test
    void get_empty_list() {
        given()
                .when()
                .get(tagUrl)
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

}
