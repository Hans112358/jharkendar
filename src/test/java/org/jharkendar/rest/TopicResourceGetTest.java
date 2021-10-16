package org.jharkendar.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TopicResourceGetTest extends BaseTest {

    @Test
    void get_by_name() {
        String createTopicDto1 = toJson(new CreateTopicDto("Important stuff"));
        String createTopicDto2 = toJson(new CreateTopicDto("Other stuff"));

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto1)
                .when()
                .post("/topic")
                .then();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto2)
                .when()
                .post("/topic")
                .then();

        String id = extractUuid(response);

        given()
                .when()
                .get("/topic/" + id)
                .then()
                .statusCode(200)
                .body(is("{\"id\":\"" + id + "\",\"name\":\"Important stuff\"}"));
    }

    @Test
    void handle_not_existing_topic_name() {
        String createTopicDto = toJson(new CreateTopicDto("Important stuff"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto)
                .when()
                .post("/topic")
                .then();

        given()
                .when()
                .get("/topic/123")
                .then()
                .statusCode(404)
                .body(is("No entity found for id 123"));
    }
}
