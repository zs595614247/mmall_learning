package com.mmall.util;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtils {
    private static Properties props;

    static {
        String fileName = "Config/mmall.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(Object.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
//            logger.error("配置文件读取异常",e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){

        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }
}
