package org.jharkendar.rest.topic;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.jharkendar.rest.summary.CreateSummaryDto;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.jharkendar.util.JsonMapper.toJson;

@QuarkusTest
class TopicDeleteTest extends TopicBaseTest {

    @TestHTTPResource("/summary")
    URL summaryUrl;

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
                .body(is("No Topic found for id 123"));
    }

    @Test
    void not_delete_topic_if_associated_summary_existing() {
        String createTopicDto = toJson(new CreateTopicDto("Important stuff"));

        ValidatableResponse topicResponse = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createTopicDto)
                .when()
                .post(topicUrl)
                .then();

        String topicId = extractUuid(topicResponse);

        CreateSummaryDto createSummaryDto = new CreateSummaryDto(
                "title",
                "text",
                topicId,
                null
        );

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(createSummaryDto)
                .when()
                .post(summaryUrl)
                .then();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(topicUrl + "/" + topicId)
                .then()
                .statusCode(405)
                .body(is("The following summaries still contain the Topic Important stuff: title"));
    }


}
