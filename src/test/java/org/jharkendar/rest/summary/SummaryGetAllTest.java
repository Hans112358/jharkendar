package org.jharkendar.rest.summary;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        LinkedHashMap<String, Object> publicSummaryDtos = (LinkedHashMap<String, Object>) responseGet.extract().as(List.class).get(0);
        assertThat(publicSummaryDtos.get("id")).isEqualTo(uuid);
        assertThat(publicSummaryDtos.get("tagIds")).isEqualTo(new ArrayList<>());
        assertThat(publicSummaryDtos.get("title")).isEqualTo("title");
        assertThat(publicSummaryDtos.get("topicId")).isEqualTo(topicId);
    }

}
