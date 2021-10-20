package org.jharkendar.rest.summary;

import io.quarkus.test.common.http.TestHTTPResource;
import io.restassured.response.ValidatableResponse;
import org.jharkendar.rest.BaseTest;
import org.jharkendar.rest.tag.CreateTagDto;
import org.jharkendar.rest.topic.CreateTopicDto;

import javax.ws.rs.core.MediaType;
import java.net.URL;

import static io.restassured.RestAssured.given;

public class SummaryBaseTest extends BaseTest {

    @TestHTTPResource("/summary")
    URL summaryUrl;

    @Override
    public URL getUrl() {
        return summaryUrl;
    }

    String createTopic(String name) {
        CreateTopicDto dto = new CreateTopicDto(name);

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .when()
                        .post(topicUrl)
                        .then();

        return extractUuid(topicUrl, response);
    }

    String createTag(String name) {
        CreateTagDto dto = new CreateTagDto(name);

        ValidatableResponse response =
                given()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dto)
                        .when()
                        .post(tagUrl)
                        .then();

        return extractUuid(tagUrl, response);
    }
}
