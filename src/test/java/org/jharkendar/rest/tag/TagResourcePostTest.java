package org.jharkendar.rest.tag;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TagResourcePostTest extends TagBaseTest {

    @Test
    void create_tag() {
        String createTagDto = toJson(new CreateTagDto("Important stuff"));

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createTagDto)
                        .when()
                        .post(tagUrl)
                        .then();

        response.statusCode(201);
        response.body(is(""));

        String uuid = extractUuid(response);

        assertThat(response.extract().header("location")).startsWith(tagUrl.toString());
        assertThat(isUuid(uuid)).isTrue();
    }

    @Test
    void not_create_tag_with_empty_body() {

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(tagUrl)
                .then();

        response.statusCode(400);
    }

    @Test
    void not_create_tag_with_empty_name() {
        String createTagDto = toJson(new CreateTagDto(null));

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto)
                .when()
                .post(tagUrl)
                .then();

        response.statusCode(400);
        response.body(containsString("name cannot be empty"));
    }


}
