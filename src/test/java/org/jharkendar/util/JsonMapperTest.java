package org.jharkendar.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jharkendar.util.JsonMapper.fromJson;
import static org.jharkendar.util.JsonMapper.toJson;
import static org.junit.jupiter.api.Assertions.*;

class JsonMapperTest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestClass {
        String field1;
        String field2;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class AnotherTestClass {
        String field3;
    }

    @Nested
    class WriterShould {
        @Test
        void write_to_json() {
            TestClass testObject = new TestClass("fieldValue1", "fieldValue2");

            String json = toJson(testObject);

            assertThat(json).isEqualTo("{\n  \"field1\" : \"fieldValue1\",\n  \"field2\" : \"fieldValue2\"\n}");
        }

        @Test
        void write_object_with_one_field_to_json() {
            TestClass testObject = new TestClass("fieldValue1", null);

            String json = toJson(testObject);

            assertThat(json).isEqualTo("{\n  \"field1\" : \"fieldValue1\",\n  \"field2\" : null\n}");
        }

        @Test
        void write_object_with_special_characters_to_json() {
            TestClass testObject = new TestClass("!\"§$&/()=?\\", "fieldValue2");

            String json = toJson(testObject);

            assertThat(json).isEqualTo("{\n  \"field1\" : \"!\\\"§$&/()=?\\\\\",\n  \"field2\" : \"fieldValue2\"\n}");
        }
    }

    @Nested
    class ReaderShould {

        @Test
        void read_json_with_one_field_null() {
            String json = "{\n  \"field1\" : \"fieldValue1\",\n  \"field2\" : null\n}";

            TestClass testObject = fromJson(json, TestClass.class);

            assertThat(testObject.getField1()).isEqualTo("fieldValue1");
            assertThat(testObject.getField2()).isNull();
        }

        @Test
        void read_json_with_special_characters() {
            String json = "{\n  \"field1\" : \"!\\\"§$&/()=?\\\\\",\n  \"field2\" : \"fieldValue2\"\n}";

            TestClass testObject = fromJson(json, TestClass.class);

            assertThat(testObject.getField1()).isEqualTo("!\"§$&/()=?\\");
            assertThat(testObject.getField2()).isEqualTo("fieldValue2");
        }

        @Test
        void throw_exception_when_class_is_wrong() {
            String json = "{\n  \"field1\" : \"fieldValue1\",\n  \"field2\" : null\n}";

            assertThrows(IllegalArgumentException.class,
                    () -> fromJson(json, AnotherTestClass.class)
            );
        }

        @Test
        void throw_exception_when_json_is_invalid() {
            String json = "\"field1\" : \"fieldValue1\",\n  \"field2\" : null\n}";

            assertThrows(IllegalArgumentException.class,
                    () -> fromJson(json, TestClass.class)
            );
        }
    }
}