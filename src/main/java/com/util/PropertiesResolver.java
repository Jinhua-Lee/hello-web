package com.util;

import java.util.ResourceBundle;

/**
 * 属性文件的解析类
 *
 * @author Jinhua
 */
public class PropertiesResolver {
    private static final ResourceBundle BUNDLE;

    static {
        BUNDLE = ResourceBundle.getBundle("mysql");
    }

    // 根据键查找值
    public static String getValue(String key) {
        return BUNDLE.getString(key);
    }
}
