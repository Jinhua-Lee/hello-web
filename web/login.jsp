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
    <div class="log_in">
        <strong>请输入验证码：</strong>
        <label>
            <input type="text" name="verifyCode"/>
        </label><br/><br/>
    </div>
    <div class="log_in">
        <img id="verify" src="VerifyCodeServlet?method=getVerifyPic" alt="验证码">
        <a href="javascript:_change()">看不清，换一张</a><br>
    </div>
    <input type="submit" value="登录"/>&nbsp;&nbsp;&nbsp;
    <input type="reset" value="重置"/>
</form>

<a href="register.jsp">
    <input type="button" value="新用户注册">
</a>
<script type="text/javascript">
    function _change() {
        const picSrc = document.getElementById("verify");
        picSrc.src = "VerifyCodeServlet?method=getVerifyPic&a=" + new Date().getTime();
    }
</script>
</body>
</html>
