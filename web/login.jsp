<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>你好Web</title>
    <link rel="icon" href="ic_launcher.png" type="image/x-icon"/>
    <style type="text/css">
        .log_in {
            border-radius: 5px;
            color: firebrick;
        }
    </style>
</head>

<body bgcolor="#d3d3d3">
<h1 style="color: tomato; font-family: 微软雅黑,monospace; text-align: center">你好~</h1>
<hr>
<form action="UserServlet" method="post">
    <input type="hidden" name="method" value="login">
    <div class="log_in">
        <strong>用户名：</strong>
        <label>
            <input type="text" name="username"/>
        </label><br/> <br/>
    </div>
    <div class="log_in">
        <strong>密码：</strong>
        <label>
            <input type="password" name="pwd"/>
        </label><br/><br/>
    </div>
    <input type="submit" name="subtn" value="登录"/>&nbsp;&nbsp;
    <input type="reset" name="resbtn" value="重置"/>
</form>

<a href="register.jsp">
    <input type="button" value="新用户注册">
</a>
</body>
</html>
