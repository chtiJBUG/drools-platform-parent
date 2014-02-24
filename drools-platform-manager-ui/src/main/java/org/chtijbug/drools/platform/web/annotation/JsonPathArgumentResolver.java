package org.chtijbug.drools.platform.web.annotation;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JsonPathArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSONBODYATTRIBUTE = "JSON_REQUEST_BODY";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonArg.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String body = getRequestBody(webRequest);
        JSONArray jsonArray = JsonPath.read(body, parameter.getParameterAnnotation(JsonArg.class).value());
        JavaType collectionType = TypeFactory.defaultInstance().constructType(parameter.getGenericParameterType());
        return objectMapper.readValue(jsonArray.toJSONString(), collectionType);
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String jsonBody = (String) servletRequest.getAttribute(JSONBODYATTRIBUTE);
        if (jsonBody == null) {
            try {
                String body = IOUtils.toString(servletRequest.getInputStream());
                servletRequest.setAttribute(JSONBODYATTRIBUTE, body);
                return body;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonBody;

    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
