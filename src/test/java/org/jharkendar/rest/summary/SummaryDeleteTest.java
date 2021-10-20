package org.jharkendar.rest.summary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class SummaryDeleteTest extends SummaryBaseTest {

    @Test
    void delete_summary() {

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


        String uuid = extractUuid(response);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(summaryUrl + "/" + uuid)
                .then()
                .statusCode(200);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(summaryUrl + "/" + uuid)
                .then()
                .statusCode(404);
    }

    @Test
    void not_delete_summary_if_not_existing() {

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(summaryUrl + "/123")
                .then()
                .statusCode(404)
                .body(is("No Summary found for id 123"));
    }
}
