package br.com.doug.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ObjectConversorUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectConversorUtils.class);

    private static final String CURRENT_PATH = System.getProperty("user.dir");
    private static final String EXTERNAL_DIR = "external-data";

    public static <T> T convertJsonInObject(String jsonName, TypeReference<T> typeRef) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonPath = CURRENT_PATH + File.separator + EXTERNAL_DIR + File.separator + jsonName;
        File file = new File(jsonPath);

        try {
            return mapper.readValue(file, typeRef);
        } catch (IOException e) {
            LOG.error("Error during the conversation of json file in object. Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
