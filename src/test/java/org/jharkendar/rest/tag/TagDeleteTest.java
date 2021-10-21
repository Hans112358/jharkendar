package org.jharkendar.rest.tag;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.jharkendar.rest.summary.CreateSummaryDto;
import org.jharkendar.rest.summary.PublicSummaryDto;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TagDeleteTest extends TagBaseTest {

    @Test
    void delete_tag() {
        String createTagDto = toJson(new CreateTagDto("Important stuff"));

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTagDto)
                .when()
                .post(tagUrl)
                .then();

        String id = extractUuid(response);

        given()
                .when()
                .delete(tagUrl + "/" + id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get(tagUrl)
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void handle_id_not_found() {
        given()
                .when()
                .delete(tagUrl + "/123")
                .then()
                .statusCode(404)
                .body(is("No Tag found for id 123"));
    }

    @Test
    void cascade_delete_to_summary() {

        // create tags, topic and summary
        String topicId = createTopic("topic");
        String tagId1 = createTag("tag1");
        String tagId2 = createTag("tag2");

        CreateSummaryDto dto = new CreateSummaryDto(
                "title",
                "text",
                topicId,
                Arrays.asList(tagId1, tagId2)
        );

        ValidatableResponse summaryResponse =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .when()
                        .post(summaryUrl)
                        .then();

        String summaryUuid = extractUuid(summaryUrl, summaryResponse);


        // delete tag1
        given()
                .when()
                .delete(tagUrl + "/" + tagId1)
                .then()
                .statusCode(200);

        // check that tag1 was also deleted on summary
        ValidatableResponse responseGetSummary = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(summaryUrl + "/" + summaryUuid)
                .then();

        responseGetSummary.statusCode(200);
        PublicSummaryDto publicSummaryDto = responseGetSummary.extract().as(PublicSummaryDto.class);
        assertThat(publicSummaryDto).isEqualTo(
                new PublicSummaryDto(
                        summaryUuid,
                        "title",
                        "text",
                        topicId,
                        Collections.singletonList(tagId2)
                )
        );

    }

}
