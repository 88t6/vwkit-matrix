package matrix.module.based.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import matrix.module.common.exception.ServiceException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wangcheng
 */
public class JacksonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJsonString(Object obj) {
        try {
            String str = objectMapper.writeValueAsString(obj);
            return str;
        } catch (JsonProcessingException var2) {
            throw new ServiceException(var2);
        }
    }

    public static JsonNode parseJson(String str) {
        try {
            JsonNode tree = objectMapper.readTree(str);
            return tree;
        } catch (IOException var2) {
            throw new ServiceException("Parse json string fail", var2);
        }
    }

    public static <T> T parseJson(String str, Class<T> clazz) {
        try {
            T val = objectMapper.readValue(str, clazz);
            return val;
        } catch (IOException var3) {
            throw new ServiceException("Parse json string fail", var3);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> List<T> parseJsonAsList(String str, Class<T> clazz) {
        try {
            TypeReference valueType = new ListTypeReference(clazz);
            Object obj = objectMapper.readValue(str, valueType);
            return (List) obj;
        } catch (IOException var4) {
            throw new ServiceException("Parse json list fail", var4);
        }
    }

    public static <T> T createObject(Object source, Class<T> target) {
        try {
            String json = objectMapper.writeValueAsString(source);
            return objectMapper.readValue(json, target);
        } catch (IOException var3) {
            throw new ServiceException("create object fail", var3);
        }
    }

    private static class ListTypeReference extends TypeReference<List<?>> {
        private Type listType;

        @SuppressWarnings("rawtypes")
        protected ListTypeReference(Class clazz) {
            this.listType = new ParameterizedListTypeImpl(clazz);
        }

        @Override
        public Type getType() {
            return this.listType;
        }
    }

    private static class ParameterizedListTypeImpl implements ParameterizedType {
        private Type type;

        @SuppressWarnings("rawtypes")
        protected ParameterizedListTypeImpl(Class clazz) {
            this.type = clazz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{this.type};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
