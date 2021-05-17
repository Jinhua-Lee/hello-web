package com.servlet;

import lombok.SneakyThrows;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 通用BaseServlet
 *
 * @author Jinhua
 */
public abstract class BaseServlet extends HttpServlet {

    @Override
    @SneakyThrows
    protected final void service(HttpServletRequest req, HttpServletResponse resp) {
        // 获取请求方法
        String method = req.getParameter("method");
        System.out.println("正在执行 " + method + "方法");
        // 当前类的类对象
        Class<?> clazz = this.getClass();
        // 通过反射获取对应方法
        Method servletMethod = clazz.getMethod(method,
                HttpServletRequest.class, HttpServletResponse.class);
        // 返回值的简单类型名
        String returnTypeName = servletMethod.getReturnType().getSimpleName();
        // 通过方法返回值进行处理逻辑
        String stringStr = "String";
        if (stringStr.equals(returnTypeName)) {
            String result = servletMethod.invoke(this, req, resp).toString();
            // 转发到对应页面
            req.getRequestDispatcher(result).forward(req, resp);
        } else {
            // 直接调用当前类中的方法即可
            servletMethod.invoke(this, req, resp);
        }
    }
}
