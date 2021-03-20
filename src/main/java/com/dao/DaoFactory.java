package com.dao;

import java.util.ResourceBundle;

/**
 * Dao工厂
 *
 * @author Jinhua
 */
public class DaoFactory {
    private static final ResourceBundle BUNDLE;

    static {
        BUNDLE = ResourceBundle.getBundle("dao");
    }


    public static UserDao getUserDao() {
        String userDaoImpl = BUNDLE.getString("com.milli.dao.Userdao");
        try {
            Class<?> clazz = Class.forName(userDaoImpl);
            return (UserDao) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
