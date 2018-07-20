package com.mmall.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转Json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //设置日期格式为:yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略 在json字符串中存在,但是在java对象中不存在对应属性的情况.防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将任意类型T通过Jackson序列化成字符串
     * @param obj 任意类型T
     * @param <T> 泛型
     * @return 返回序列化后的字符串
     */
    public static <T> String objToString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }
    /**
     * 将任意类型T通过Jackson序列化成格式化后字符串
     * @param obj 任意类型T
     * @param <T> 泛型
     * @return 返回序列化后的字符串
     */
    public static <T> String objToStringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse object to String error",e);
            return null;
        }
    }

    /**
     * 将String类型的字符串反序列化为Clazz类型的对象
     * @param str 需要反序列化的字符串
     * @param clazz 反序列化后的类型
     * @param <T> 表示将此方法声明为泛型方法(也可理解为声明方法持有一个类型T)
     * @return clazz类型的对象
     */
    public static <T>T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str: objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn(" Parse String to Object error",e);
            return null;
        }
    }

    /**
     * 反序列化复杂类型,如List<Object>
     * @param str 需要反序列化的字符串
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T>T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return typeReference.getType().equals(String.class) ? (T) str : objectMapper.readValue(str, typeReference);
        } catch (IOException e) {
            log.warn(" Parse String to Object error",e);
            return null;
        }
    }

    /**
     * 反序列化复杂类型,如List<Object>,Map<Object,Object>
     * @param str 需要反序列化的字符串
     * @param collectionClass 外层的类的class
     * @param elementClass 内部的类的class
     * @param <T> 反序列化后的对象
     * @return T
     */
    public static <T>T string2Obj(String str, Class<?> collectionClass,Class<?>... elementClass) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn(" Parse String to Object error",e);
            return null;
        }
    }


    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setEmail("zshang@qq.com");
        String userJson = JsonUtil.objToString(user);

        String userJsonPretty = JsonUtil.objToStringPretty(user);

        log.info("userJson:{}", userJson);
        log.info("userJsonPretty:{}",userJsonPretty);

        User user1 = JsonUtil.string2Obj(userJson, User.class);

        System.out.println(user1);
    }
}
