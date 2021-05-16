package com.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 访问过滤器
 *
 * @author Jinhua
 */
@WebFilter(filterName = "VisitFilter", urlPatterns = "/*")
public class VisitFilter implements Filter {

    @Override
    public void init(FilterConfig config) {
        System.out.println("访问过滤器初始化~");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        // 强制转换请求和响应对象 --> HTTP请求和响应对象
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 获取Session中的用户信息
        Object username = request.getSession().getAttribute("username");

        // 获取调用的方法名称
        String methodName = request.getParameter("method");

        String url = request.getRequestURL().toString() + "?method=" + methodName;

        System.out.println(url);

        // 如果当前页是登录或者注册界面
        final String loginStr = "login";
        final String registerStr = "register";
        if (url.contains(loginStr) || url.contains(registerStr)) {
            chain.doFilter(request, response);
        } else {
            if (username == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println("访问过滤器销毁~");
    }

}
