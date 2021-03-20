package com.util;

import java.sql.*;
import java.util.*;

/**
 * 数据库JDBC工具类封装
 *
 * @author Jinhua
 */
public class MyDbUtils {
    /**
     * 定义连接数据库的常量
     */
    private static final String DRIVER = PropertiesResolver.getValue("jdbc.driver");
    private static final String URL = PropertiesResolver.getValue("jdbc.url");
    private static final String USER = PropertiesResolver.getValue("jdbc.user");
    private static final String PASSWORD = PropertiesResolver.getValue("jdbc.password");
    private static Connection conn = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;

    /**
     * 本地线程对象，保存线程绑定的连接
     */
    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<>();

    // 静态代码块注册驱动，只运行一次
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("驱动注册成功~");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("驱动注册失败！");
        }
    }

    /**
     * 定义获取数据库连接的方法
     */
    private static Connection getConnection() {
        // 从本地线程中获取连接对象
        conn = CONNECTION_THREAD_LOCAL.get();
        // 判断获取到的连接对象是否为空
        if (conn == null) {
            // 创建连接对象，放到本地线程中
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                CONNECTION_THREAD_LOCAL.set(conn);
                System.out.println("连接数据库成功~");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("连接数据库失败！");
            }
        }
        return conn;
    }

    /**
     * 定义关闭数据库连接的方法
     */
    private static void closeConnection() {
        conn = CONNECTION_THREAD_LOCAL.get();
        // 从本地线程中获取连接对象
        try {
            if (conn != null) {
                if (!conn.isClosed()) {
                    conn.close();
                    CONNECTION_THREAD_LOCAL.remove();
                    conn = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义给外部简单增、删、改的方法
     * 可变参数为设置占位符"?"的Object数据
     *
     * @param sql        增、删、改的语句
     * @param parameters 语句中的占位符参数
     * @return 返回受影响的行数
     */
    public static int executeUpdate(String sql, Object... parameters) {
        int row = 0;
        // 获取连接对象
        conn = getConnection();
        try {
            pst = conn.prepareStatement(sql);
            if (parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    // 设置参数，下标从1开始
                    pst.setObject(i + 1, parameters[i]);
                }
            }
            // 执行SQL语句，返回受影响的行数
            row = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (!pst.isClosed()) {
                    pst.close();
                }
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;
    }

    /**
     * 定义给外部查询的方法
     *
     * @param sql        查询的语句
     * @param parameters 占位符的参数
     * @return 构建好的列的集合作为一个表
     */
    public static List<Map<String, Object>> executeQuery(String sql, Object... parameters) {
        List<Map<String, Object>> table = new ArrayList<>();
        conn = getConnection();
        try {
            pst = conn.prepareStatement(sql);
            // 判断是否需要设置参数
            if (parameters.length > 0) {
                for (int i = 0; i < parameters.length; i++) {
                    // 设置占位符对应的参数，占位符从1开始
                    pst.setObject(i + 1, parameters[i]);
                }
            }
            // 通过编译对象执行SQL指令
            rs = pst.executeQuery();
            if (rs != null) {
                // 获取结果集的元数据
                ResultSetMetaData rsd = rs.getMetaData();
                // 获取当前表的总列数
                int columnCount = rsd.getColumnCount();
                // 遍历结果集
                while (rs.next()) {
                    // 创建存储当前行的集合对象
                    Map<String, Object> row = new LinkedHashMap<>();
                    // 遍历当前行每一列
                    for (int i = 0; i < columnCount; i++) {
                        // 获取列的编号获取列名
                        String columnName = rsd.getColumnName(i + 1);
                        // 通过列名获取当前遍历列的值
                        Object columnValue = rs.getObject(columnName);
                        // 列名和获取值作为 K 和 V 存入Map集合
                        row.put(columnName, columnValue);
                    }
                    // 把每次遍历列的Map集合存储到List集合中
                    table.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert rs != null;
                rs.close();
                pst.close();
                closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 返回构建好的集合
        return table;
    }

    public static void main(String[] args) {
        String sql = "Select * From Usr";
        // 获取结果的List集合（可看作一个表）
        List<Map<String, Object>> table = MyDbUtils.executeQuery(sql);

        for (Map<String, Object> row : table) {
            ArrayList<String> rowNames = new ArrayList<>(row.keySet());
            for (String name : rowNames) {
                System.out.println(row.get(name));
            }
        }
    }
}
