package org.jharkendar.rest.tag;

import io.quarkus.test.common.http.TestHTTPResource;
import org.jharkendar.rest.BaseTest;

import java.net.URL;

public class TagBaseTest extends BaseTest {

    @TestHTTPResource("/tag")
    URL tagUrl;

    @Override
    public URL getUrl() {
        return tagUrl;
    }
}
