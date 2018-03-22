package com.britenet.contacts.task;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

    public static String asJson(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
