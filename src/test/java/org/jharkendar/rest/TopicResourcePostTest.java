package org.jharkendar.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TopicResourcePostTest extends BaseTest {

    @Test
    void create_topic() {
        String createTopicDto = toJson(new CreateTopicDto("Important stuff"));

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createTopicDto)
                        .when()
                        .post("/topic")
                        .then();

        response.statusCode(201);
        response.body(is(""));

        String uuid = extractUuid(response);

        assertThat(response.extract().header("location")).startsWith("http://localhost:8081/topic/");
        assertThat(isUuid(uuid)).isTrue();
    }

    @Test
    void not_create_topic_with_empty_body() {

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/topic")
                .then();

        response.statusCode(400);
    }

    @Test
    void not_create_topic_with_empty_name() {
        String createTopicDto = toJson(new CreateTopicDto(null));

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto)
                .when()
                .post("/topic")
                .then();

        response.statusCode(400);
        response.body(containsString("name cannot be empty"));
    }



}
