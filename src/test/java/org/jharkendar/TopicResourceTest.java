package org.jharkendar;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TopicResourceTest {

    @Test
    public void get_empty_list() {
        given()
                .when().get("/topic/get")
                .then()
                .statusCode(200)
                .body(is(""));
    }
}
