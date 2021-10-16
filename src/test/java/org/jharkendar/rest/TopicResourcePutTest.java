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
class TopicResourcePutTest extends BaseTest {

    @Test
    void update_name() {
        String createTopicDto = toJson(new CreateTopicDto("Important stuff"));

        ValidatableResponse responsePost = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto)
                .when()
                .post("/topic")
                .then();

        String id = extractUuid(responsePost);

        String updateTopicDto = toJson(new UpdateTopicDto("Other stuff"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateTopicDto)
                .when()
                .put("/topic/" + id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/topic?name=Other%20stuff")
                .then()
                .statusCode(200)
                .body(is("[{\"id\":\"" + id + "\",\"name\":\"Other stuff\"}]"));
    }

    @Test
    void not_update_when_id_is_invalid() {

        String updateTopicDto = toJson(new UpdateTopicDto("Other stuff"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateTopicDto)
                .when()
                .put("/topic/123")
                .then()
                .statusCode(404)
                .body(is("No entity found for id 123"));

    }

    @Test
    void not_update_when_name_is_invalid() {

        String updateTopicDto = toJson(new UpdateTopicDto(null));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateTopicDto)
                .when()
                .put("/topic/123")
                .then()
                .statusCode(400)
                .body(containsString("name cannot be empty"));

    }


}
