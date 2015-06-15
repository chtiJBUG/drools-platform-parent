package org.chtijbug.drools.platform.persistence.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by nheron on 18/05/15.
 */
public class JSONHelper {
    private static Logger logger = getLogger(JSONHelper.class);

    public static String getJSONStringFromJavaObject(Object ojb) {
        String result = null;
        try {
            Writer strWriter = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(strWriter, ojb);
            result = strWriter.toString();
        } catch (IOException e) {
            logger.error("private String getJSONStringFromJavaObject(Object ojb)", e);
        }

        return result;
    }

    public static Object getObjectFromJSONString(String jsonObject, Class valueType) {
        Object result = null;
        try {
            Writer strWriter = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(jsonObject.getBytes(), valueType);
        } catch (IOException e) {
            logger.error("private Object getObjectFromJSONString(String jsonObject,Class valueType)", e);
        }

        return result;
    }
}
