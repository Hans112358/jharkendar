package org.jharkendar.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TopicResourceGetAllTest extends BaseTest{

    @Test
    void get_two_items() {
        String createTopicDto1 = toJson(new CreateTopicDto("Important stuff"));
        String createTopicDto2 = toJson(new CreateTopicDto("Other stuff"));

        ValidatableResponse response1 = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto1)
                .when()
                .post("/topic")
                .then();

        ValidatableResponse response2 = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto2)
                .when()
                .post("/topic")
                .then();

        String id1 = extractUuid(response1);
        String id2 = extractUuid(response2);

        given()
                .when()
                .get("/topic")
                .then()
                .statusCode(200)
                .body(containsString("{\"id\":\"" + id1 + "\",\"name\":\"Important stuff\"}"))
                .body(containsString("{\"id\":\"" + id2 + "\",\"name\":\"Other stuff\"}"));
    }

    @Test
    void get_empty_list() {
        given()
                .when()
                .get("/topic")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

}
