package org.jharkendar.rest.summary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class SummaryPutTest extends SummaryBaseTest {

    @Test
    void update_summary_minimal() {
        String topicId = createTopic("topic");
        String newTopicId = createTopic("newTopic");
        CreateSummaryDto dto = new CreateSummaryDto(
                "title",
                null,
                topicId,
                null
        );

        ValidatableResponse responsePost = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .when()
                .post(summaryUrl)
                .then();

        String uuid = extractUuid(responsePost);

        UpdateSummaryDto updateSummaryDto = new UpdateSummaryDto(
                "newTitle",
                null,
                newTopicId,
                null
        );

        ValidatableResponse responsePut = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateSummaryDto)
                .when()
                .put(summaryUrl + "/" + uuid)
                .then();

        responsePut.statusCode(200);

        ValidatableResponse responseGet = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(summaryUrl + "/" + uuid)
                .then();

        responseGet.statusCode(200);
        PublicSummaryDto publicSummaryDto = responseGet.extract().as(PublicSummaryDto.class);
        assertThat(publicSummaryDto).isEqualTo(
                new PublicSummaryDto(
                        uuid,
                        "newTitle",
                        null,
                        newTopicId,
                        new ArrayList<>()
                )
        );
    }

    @Test
    void update_summary_full() {
        String topicId = createTopic("topic");
        String tagId = createTag("tag");
        String newTopicId = createTopic("newTopic");
        String newTagId = createTag("newTag");

        CreateSummaryDto dto = new CreateSummaryDto(
                "title",
                "text",
                topicId,
                Collections.singletonList(tagId)
        );

        ValidatableResponse responsePost = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .when()
                .post(summaryUrl)
                .then();

        String uuid = extractUuid(responsePost);

        UpdateSummaryDto updateSummaryDto = new UpdateSummaryDto(
                "newTitle",
                "newText",
                newTopicId,
                Collections.singletonList(newTagId)
        );

        ValidatableResponse responsePut = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateSummaryDto)
                .when()
                .put(summaryUrl + "/" + uuid)
                .then();

        responsePut.statusCode(200);

        ValidatableResponse responseGet = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(summaryUrl + "/" + uuid)
                .then();

        responseGet.statusCode(200);
        PublicSummaryDto publicSummaryDto = responseGet.extract().as(PublicSummaryDto.class);
        assertThat(publicSummaryDto).isEqualTo(
                new PublicSummaryDto(
                        uuid,
                        "newTitle",
                        "newText",
                        newTopicId,
                        Collections.singletonList(newTagId)
                )
        );
    }

    @Test
    void not_update_if_not_existing() {
        String newTopicId = createTopic("newTopic");

        UpdateSummaryDto updateSummaryDto = new UpdateSummaryDto(
                "newTitle",
                null,
                newTopicId,
                null
        );

        ValidatableResponse responsePut = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateSummaryDto)
                .when()
                .put(summaryUrl + "/abc")
                .then();

        responsePut.statusCode(404);
        responsePut.body(is("No Summary found for id abc"));

    }
}
