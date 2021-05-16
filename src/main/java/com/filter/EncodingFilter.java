package com.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 编码处理器
 *
 * @author Jinhua
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = "/*")
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("编码过滤器初始化~");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        servletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        servletResponse.setContentType("text/html;charset=UTF-8");

        // 放行，响应其他请求
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        System.out.println("编码过滤器销毁~");
    }
}
