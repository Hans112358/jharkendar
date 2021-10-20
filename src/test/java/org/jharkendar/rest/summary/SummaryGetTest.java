package org.jharkendar.rest.summary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
class SummaryGetTest extends SummaryBaseTest {

    @Test
    void create_summary_minimal() {
        String topicId = createTopic("topic");
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
                        "title",
                        null,
                        topicId,
                        new ArrayList<>()
                )
        );
    }

    @Test
    void create_summary_full() {
        String topicId = createTopic("topic");
        String tagId = createTag("tag");

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
                        "title",
                        "text",
                        topicId,
                        Collections.singletonList(tagId)
                )
        );
    }

    @Test
    void get_error_when_summary_not_existing() {

        ValidatableResponse responseGet = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(summaryUrl + "/123")
                .then();

        responseGet.statusCode(404);
        responseGet.body(is("No Summary found for id 123"));
    }


}
