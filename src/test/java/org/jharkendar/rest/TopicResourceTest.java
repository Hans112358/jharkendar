package org.jharkendar.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TopicResourceTest {

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    public void setUp() {
        entityManager
                .createQuery("delete from JpaTopic ")
                .executeUpdate();
    }

    @Test
    void create_topic() {
        String topicName = toJson(new CreateTopicDto("Important stuff"));

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(topicName)
                        .when()
                        .post("/topic")
                        .then();

        response.statusCode(201);
        response.body(is(""));

        String location = response.extract().header("location");
        String uuid = location.replace("http://localhost:8081/topic/", "");

        assertThat(location).startsWith("http://localhost:8081/topic/");
        assertThat(isUuid(uuid)).isTrue();
    }

    @Test
    void not_create_topic_with_empty_body() throws JsonProcessingException {

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post("/topic")
                .then();

        response.statusCode(400);
    }

    @Test
    void not_create_topic_with_empty_name() throws JsonProcessingException {
        String topicName = toJson(new CreateTopicDto(null));

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(topicName)
                .when()
                .post("/topic")
                .then();

        response.statusCode(400);
        response.body(containsString("name cannot be empty"));
    }

    @Test
    void get_by_name() {
        String topicName = toJson(new CreateTopicDto("Important stuff"));

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(topicName)
                .when()
                .post("/topic");

        given()
                .when()
                .get("/topic?name=Important%20stuff")
                .then()
                .statusCode(200)
                .body(is("[{\"name\":\"Important stuff\"}]"));
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


    boolean isUuid(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
