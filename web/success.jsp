<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":"
        + request.getServerPort() + "/";
%>
<html>
<head>
    <title>登录成功</title>
    <link rel="icon" href="ic_launcher.png" type="image/x-icon"/>
</head>
<body bgcolor="#d3d3d3">
    <h1 style="color: darkmagenta; text-align: center">登录成功~！</h1>
    <div align="center">
        <strong>${msg}</strong><br><br>
        <a href="UserServlet?method=showAll">
            <input type="button" value="查看所有用户" />
        </a>
        <a href="query.jsp">
            <input type="button" value="按条件查询用户信息" />
        </a>
    </div>
</body>
</html>
