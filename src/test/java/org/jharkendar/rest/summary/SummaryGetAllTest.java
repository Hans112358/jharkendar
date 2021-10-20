package org.jharkendar.rest.summary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class SummaryGetAllTest extends SummaryBaseTest {

    @Test
    void get_summaries() {
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
                .get(summaryUrl)
                .then();

        responseGet.statusCode(200);
        List<PublicSummaryDto> publicSummaryDtos = ((List<PublicSummaryDto>) responseGet.extract().as(List.class));
        assertThat(publicSummaryDtos).containsExactly(
                new PublicSummaryDto(
                        uuid,
                        "title",
                        null,
                        topicId,
                        new ArrayList<>()
                )
        );
    }

}
