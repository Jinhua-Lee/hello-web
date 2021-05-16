package com.servlet;

import com.domain.PageBean;
import com.domain.User;
import com.service.UserService;
import com.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * 用户访问Servlet层
 *
 * @author Jinhua
 */
@SuppressWarnings("all")
@WebServlet(name = "UserServlet", urlPatterns = "/UserServlet")
public class UserServlet extends BaseServlet {

    private final UserService userService = new UserServiceImpl();

    /**
     * 登录方法
     *
     * @param req  http请求
     * @param resp http响应
     * @return 转发页面
     * @throws ServletException servlet异常
     * @throws IOException      IO异常
     */
    public String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 封装数据
        // 调用业务方法
        // 获取页面提交的数据：用户名、密码
        String name = req.getParameter("username");
        String password = req.getParameter("pwd");
        // 调用Service层，判断是否与数据库的用户名密码匹配
        if (userService.login(name, password)) {
            // 若成功，将用户名和密码保存到Session中，代表用户合法
            // 并且进入成功登录后的欢迎页面
            req.getSession().setAttribute("username", name);
            req.setAttribute("msg", "欢迎用户 " + name + "\r\n");
            return "/success.jsp";
        } else {
            System.out.println("登录失败！");
            // 若失败，则弹窗提示并且重定向到登录页面
            return "/login.jsp";
        }
    }

    /**
     * 注册方法
     *
     * @param req  http请求
     * @param resp http响应
     * @return 转发页面
     * @throws ServletException servlet异常
     * @throws IOException      IO异常
     */
    public String register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取页面参数
        String name = req.getParameter("name");
        String pwd = req.getParameter("pwd");
        String sex = req.getParameter("sex");
        String home = req.getParameter("home");
        String info = req.getParameter("info");

        User user = new User(name, pwd, sex, home, info);
        String pwdConfig = req.getParameter("pwdConfig");
        final int mapInitSize = 8;
        // 进行表单校验
        Map<String, String> errors = new HashMap<>(mapInitSize);
        // 用户名校验
        if (Objects.isNull(name) || name.trim().isEmpty()) {
            errors.put("name", "用户名不能为空！");
        } else if (name.length() < 3 || name.length() > 15) {
            errors.put("name", "用户名长度必须在3-14之间！");
        } else if (Objects.nonNull(userService.findUserByName(name))) {
            errors.put("name", "用户名已经存在！！！");
        }

        // 密码校验
        if (Objects.isNull(pwd) || pwd.trim().isEmpty()) {
            errors.put("pwd", "密码不能为空！");
        } else if (pwd.length() < 4 || pwd.length() > 15) {
            errors.put("pwd", "密码长度必须在4-15之间！");
        }
        // 密码确认框校验
        if (!pwdConfig.equals(pwd)) {
            errors.put("pwdConfig", "密码不匹配！");
        }

        // 性别校验
        if (Objects.isNull(sex) || sex.trim().isEmpty()) {
            errors.put("sex", "性别不能为空！");
        }
        // 家乡校验
        if (Objects.isNull(home) || home.trim().isEmpty()) {
            errors.put("home", "家乡不能为空！");
        }

        if (errors.size() > 0) {
            // 保存errors到request域
            req.setAttribute("errors", errors);
        }
        // 若注册成功则跳转到登录页面
        else {
            if (userService.register(user)) {
                return "/login.jsp";
            }
        }
        // 注册失败则弹出提示，并返回到该页面
        return "/register.jsp";
    }

    /**
     * 删除方法
     *
     * @param req  http请求
     * @param resp http响应
     * @return 转发页面
     * @throws ServletException servlet异常
     * @throws IOException      IO异常
     */
    public String delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 首先获得id号
        String id = req.getParameter("id");
        int uid = Integer.parseInt(id);

        if (userService.deleteUser(uid)) {
            // 转发到该页
            return "/UserServlet?method=showAll";
        } else {
            // 若失败，则弹窗提示并且重定向到登录页面
            return "/login.jsp";
        }
    }

    /**
     * 展示方法
     *
     * @param req  http请求
     * @param resp http响应
     * @return 转发页面
     * @throws ServletException servlet异常
     * @throws IOException      IO异常
     */
    public String showAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PageBean<User> pageBean = userService.getUserAll();

        // 设置页面参数
        req.setAttribute("pageBean", pageBean);

        // 转发到所有用户信息页面
        return "/showAll.jsp";
    }

    /**
     * 封装参数到实体中
     *
     * @param req       Http请求
     * @param beanClass 实体类的类对象
     * @param <T>       实体的具体类型
     * @return 返回封装的实体类型
     */
    private <T> T requestToBean(HttpServletRequest req, Class<T> beanClass) {
        try {
            // 创建封装数据的bean
            T bean = beanClass.getDeclaredConstructor().newInstance();
            Map<String, String[]> map = req.getParameterMap();

            final int mapSize = 16;
            // 这里过滤不需要封装的参数
            Map<String, String[]> map1 = new HashMap<>(mapSize);
            map1.putAll(map);
            if (beanClass.equals(User.class)) {
                String methodKey = "method";
                if (map1.get(methodKey) != null) {
                    map1.remove(methodKey);
                }
                if (map1.get("pageBean.pageSize") != null) {
                    map1.remove("pageBean.pageSize");
                }
                if (map1.get("pageBean.pageCurrent") != null) {
                    map1.remove("pageBean.pageCurrent");
                }
            }

            System.out.println("-------------------");
            for (String s : (Set<String>) map1.keySet()) {
                System.out.println(s);
            }

            // 执行封装
            BeanUtils.populate(bean, map1);
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 使用Apache的Bean工具
        User bean = requestToBean(req, User.class);
        System.out.println(bean.toString());
        // 使用自定义的Bean工具
//		User bean = BeanUtil.getBean(User.class, req);

        // 进行更新操作
        if (userService.updateUserInfo(bean)) {
            // 转到展示所有用户的页面
            return "/UserServlet?method=showAll";
        }
        return "/login.jsp";
    }

    /**
     * 分页查询 + 多条件组合查询
     *
     * @param req  Http请求
     * @param resp Http响应
     * @return 返回转发的页面
     * @throws ServletException Servlet异常
     * @throws IOException      IO异常
     */
    public String queryByPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取条件
        User criteria = requestToBean(req, User.class);
        // 获取页面参数，进行转换
        String pageSize = req.getParameter("pageBean.pageSize");
        // 每页的条数
        int pc = getPageCurrent(req);
        int ps = Integer.parseInt(pageSize);
        // 执行一次查询，查询到pageBean对象得到用户实体
        PageBean<User> pageBean = userService.findAll(pc, ps, criteria);

        req.setAttribute("pageBean", pageBean);
        // 保存数据到Request域中
        req.setAttribute("Criteria", criteria);

        return "/query.jsp";
    }


    /**
     * 获得GET请求的URL
     *
     * @param req Http请求
     * @return 返回请求的URL字符串
     */
    private String getUrl(HttpServletRequest req) {
        String contextPath = req.getContextPath();
        String method = req.getParameter("method");
        String servletPath = req.getServletPath();
        String queryString = req.getQueryString();
        if (queryString.contains("&pageBean.pageCurrent=")) {
            int index = queryString.lastIndexOf("&pageBean.pageCurrent");
            queryString = queryString.substring(index);
        }
        return contextPath + servletPath + "?" + "method=" + method + queryString;
    }

    /**
     * 根据Http请求获得当前页码
     *
     * @param req Http请求
     * @return 当前页码
     */
    private int getPageCurrent(HttpServletRequest req) {
        String pageCurrent = req.getParameter("pageBean.pageCurrent");

        if (pageCurrent == null || pageCurrent.trim().isEmpty()) {
            return 1;
        } else {
            int pc = Integer.parseInt(pageCurrent);
            if (pc <= 1) {
                return 1;
            }
        }
        return Integer.parseInt(pageCurrent);
    }
}
