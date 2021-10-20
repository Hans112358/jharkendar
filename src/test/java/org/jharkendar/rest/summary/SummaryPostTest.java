package org.jharkendar.rest.summary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class SummaryPostTest extends SummaryBaseTest {

    @Test
    void create_summary_minimal() {
        String topicId = createTopic("topic");
        CreateSummaryDto dto = new CreateSummaryDto(
                "title",
                null,
                topicId,
                null
        );

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .when()
                        .post(summaryUrl)
                        .then();

        response.statusCode(201);
        assertThat(isUuid(extractUuid(response))).isTrue();
    }

    @Test
    void create_summary_full() {
        String topicId = createTopic("topic");
        String tagId1 = createTag("tag1");
        String tagId2 = createTag("tag2");

        CreateSummaryDto dto = new CreateSummaryDto(
                "title",
                "text",
                topicId,
                Arrays.asList(tagId1, tagId2)
        );

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .when()
                        .post(summaryUrl)
                        .then();

        response.statusCode(201);
        assertThat(isUuid(extractUuid(response))).isTrue();
    }

    @Test
    void not_create_summary_if_topic_is_not_existing() {
        CreateSummaryDto dto = new CreateSummaryDto(
                "title",
                null,
                "123",
                null
        );

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .when()
                        .post(summaryUrl)
                        .then();

        response.statusCode(404);
        response.body(is("No Topic found for id 123"));
    }

    @Test
    void not_create_summary_if_tag_is_not_existing() {
        String topicId = createTopic("topic");
        String tagId1 = createTag("tag1");
        String tagId2 = createTag("tag2");

        CreateSummaryDto dto = new CreateSummaryDto(
                "title",
                "text",
                topicId,
                Arrays.asList(tagId1, tagId2, "abc")
        );

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .when()
                        .post(summaryUrl)
                        .then();

        response.statusCode(404);
        response.body(is("No Tag found for id abc"));
    }

}
