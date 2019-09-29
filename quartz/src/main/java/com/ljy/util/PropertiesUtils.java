package com.ljy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件工具
 * @author admin
 *
 */
public class PropertiesUtils {
    
    public static String getContent(String key) {
    	InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream("url.properties");
        Properties props = new Properties();
        try {
            props.load(in);
            return props.getProperty(key);
        } catch (IOException e) {
        	return "";
        }
    }
    
    public static void main(String[] args) {
		System.out.println(getContent("PICTUREURL"));
	}

}
