package com.ztkmkoo.dss.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 27. 오전 2:16
 */
public class JacksonUtilsTest {

    @Test(expected = MismatchedInputException.class)
    public void readJsonStringEmpty() throws JsonProcessingException {

        final String jsonString = "";
        JacksonUtils.readJsonString(jsonString, new TypeReference<TestSerializable>() {});
    }

    @Test
    public void readJsonStringEmptyJson() throws JsonProcessingException {

        final String jsonString = "{}";
        final TestSerializable serializable = JacksonUtils.readJsonString(jsonString, new TypeReference<TestSerializable>() {});

        assertNotNull(serializable);
        assertNull(serializable.getId());
        assertNull(serializable.getValue());
    }

    @Test
    public void readJsonString() throws JsonProcessingException {

        final String jsonString = "{\"id\": 100, \"value\": \"Hello\"}";
        final TestSerializable serializable = JacksonUtils.readJsonString(jsonString, new TypeReference<TestSerializable>() {});
        final long id = serializable.getId();

        assertNotNull(serializable);
        assertEquals(100L, id);
        assertEquals("Hello", serializable.getValue());
    }

    @Getter
    @Setter
    public static class TestSerializable implements Serializable {

        private static final long serialVersionUID = 1607826276840811992L;
        private Long id;
        private String value;
    }
}