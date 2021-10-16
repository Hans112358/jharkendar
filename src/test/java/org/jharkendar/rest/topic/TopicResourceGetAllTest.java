package org.jharkendar.rest.topic;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TopicResourceGetAllTest extends TopicBaseTest {

    @Test
    void get_two_items() {
        String createTopicDto1 = toJson(new CreateTopicDto("Important stuff"));
        String createTopicDto2 = toJson(new CreateTopicDto("Other stuff"));

        ValidatableResponse response1 = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto1)
                .when()
                .post(topicUrl)
                .then();

        ValidatableResponse response2 = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto2)
                .when()
                .post(topicUrl)
                .then();

        String id1 = extractUuid(response1);
        String id2 = extractUuid(response2);

        given()
                .when()
                .get(topicUrl)
                .then()
                .statusCode(200)
                .body(containsString("{\"id\":\"" + id1 + "\",\"name\":\"Important stuff\"}"))
                .body(containsString("{\"id\":\"" + id2 + "\",\"name\":\"Other stuff\"}"));
    }

    @Test
    void get_empty_list() {
        given()
                .when()
                .get(topicUrl)
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

}
