package org.jharkendar.rest.topic;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.jharkendar.rest.topic.CreateTopicDto;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TopicDeleteTest extends TopicBaseTest {

    @Test
    void delete_topic() {
        String createTopicDto = toJson(new CreateTopicDto("Important stuff"));

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto)
                .when()
                .post(topicUrl)
                .then();

        String id = extractUuid(response);

        given()
                .when()
                .delete(topicUrl + "/" + id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get(topicUrl)
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void handle_id_not_found() {
        given()
                .when()
                .delete(topicUrl + "/123")
                .then()
                .statusCode(404)
                .body(is("No entity found for id 123"));
    }

}
