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
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String jsonPath = CURRENT_PATH + File.separator + EXTERNAL_DIR + File.separator;

    public static <T> T convertJsonInObject(String jsonName, TypeReference<T> typeRef) {
        String path = jsonPath + jsonName;
        File file = new File(path);

        try {
            return mapper.readValue(file, typeRef);
        } catch (IOException e) {
            LOG.error("Error during the conversation of json file in object. Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void writeObjectInJsonFile(String jsonName, Object obj) {
        String path = jsonPath + jsonName;
        try {
            mapper.writeValue(new File(path), obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
