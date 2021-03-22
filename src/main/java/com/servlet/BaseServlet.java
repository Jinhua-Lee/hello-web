package com.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * java通用BaseServlet类
 * <p>
 * 方法约定：
 * public String xXX() {}
 * public void xXX() {}
 *
 * @author Jinhua
 */
public class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // 获取请求方法
        String method = req.getParameter("method");
        System.out.println("正在执行 " + method + "方法");
        // 通过反射获取当前类的类对象
        Class<?> clazz = this.getClass();
        // 通过反射获取对应的方法
        try {
            // 通过反射获取对应方法
            Method servletMethod = clazz.getMethod(method,
                    HttpServletRequest.class, HttpServletResponse.class);
            // 获取方法对象的返回值的简单类名
            String returnName = servletMethod.getReturnType().getSimpleName();
            // 通过方法返回值进行处理逻辑
            String stringStr = "String";
            if (stringStr.equals(returnName)) {
                String result = servletMethod.invoke(this, req, resp).toString();

                // 转发到对应页面
                req.getRequestDispatcher(result).forward(req, resp);
            } else {
                // 直接调用当前类中的方法即可
                servletMethod.invoke(this, req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
