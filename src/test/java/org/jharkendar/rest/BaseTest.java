package org.jharkendar.rest;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.net.URL;
import java.util.UUID;

@QuarkusTest
public abstract class BaseTest {

    @TestHTTPResource("/topic")
    protected URL topicUrl;

    @TestHTTPResource("/tag")
    protected URL tagUrl;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    public void setUp() {
        entityManager
                .createQuery("delete from JpaSummary ")
                .executeUpdate();

        entityManager
                .createQuery("delete from JpaTopic ")
                .executeUpdate();

        entityManager
                .createQuery("delete from JpaTag ")
                .executeUpdate();
    }

    protected String extractUuid(ValidatableResponse response) {
        String location = response.extract().header("location");
        return location.replace(getUrl().toString() + "/", "");
    }

    protected String extractUuid(URL url, ValidatableResponse response) {
        String location = response.extract().header("location");
        return location.replace(url.toString() + "/", "");
    }

    protected abstract URL getUrl();

    protected boolean isUuid(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }


}
