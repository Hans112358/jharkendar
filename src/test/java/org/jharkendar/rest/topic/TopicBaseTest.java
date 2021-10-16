package org.jharkendar.rest.topic;

import io.quarkus.test.common.http.TestHTTPResource;
import org.jharkendar.rest.BaseTest;

import java.net.URL;

public class TopicBaseTest extends BaseTest {

    @TestHTTPResource("/topic")
    URL topicUrl;

    @Override
    public URL getUrl() {
        return topicUrl;
    }
}
