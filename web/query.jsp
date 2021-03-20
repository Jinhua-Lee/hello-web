<%@ page import="com.domain.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.domain.PageBean" %><%--
  Created by IntelliJ IDEA.
  User: 子期
  Date: 2018/8/3
  Time: 0:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <title>多条件组合查询用户信息</title>
        <style type="text/css">
            .input_wid {
                width: 100px;
            }
        </style>
        <link rel="icon" href="ic_launcher.png" type="image/x-icon"/>
    </head>

    <body bgcolor="#d3d3d3">
        <h1 style="text-align: center; color: tomato">多条件组合查询用户信息</h1>
        <hr>
        <div style="width: 250px; float: left">
            <h3 style="color: firebrick">请输入查询条件</h3>
            <form action="UserServlet" method="get">
                <input type="hidden" name="method" value="queryByPage">
                <%--<input type="hidden" name="id" value="">--%>
                <p>姓名：
                    <input type="text" name="name" value="${Criteria.name}">
                </p>
                <%--<p>密码：--%>
                <%--<input type="text" name="pwd">--%>
                <%--</p>--%>
                <p>性别：
                    <select name="sex">
                        <option value="" <c:if test="${Criteria.sex eq ''}">selected="selected"</c:if>>
                            所有</option>
                        <option value="男" <c:if test="${Criteria.sex eq '男'}">selected="selected"</c:if>>
                            男</option>
                        <option value="女"<c:if test="${Criteria.sex eq '女'}">selected="selected"</c:if>>
                            女</option>
                    </select>
                </p>
                <p>家乡：
                    <input type="text" name="home" value="${Criteria.home}">
                </p>
                <p>备注：
                    <input type="text" name="info" value="${Criteria.info}">
                </p>
                <hr>
                每页条数：
                <select name="pageBean.pageSize">
                    <option value="1" <c:if test="${pageBean.pageSize eq '1'}">selected="selected"</c:if>>
                        1</option>
                    <option value="10" <c:if test="${pageBean.pageSize eq '10'}">selected="selected"</c:if>>
                        10</option>
                    <option value="20" <c:if test="${pageBean.pageSize eq '20'}">selected="selected"</c:if>>
                        20</option>
                    <option value="50" <c:if test="${pageBean.pageSize eq '50'}">selected="selected"</c:if>>
                        50</option>
                </select>
                <input type="hidden" name="pageBean.pageCurrent" />
                <input type="submit" value="查询">
            </form>
        </div>

        <div style="width: 600px; float: left">
            <h3 style="color: firebrick">查询结果</h3>
            <table width="800px" border="1" cellpadding="0" align="center">
                <tr bgcolor="#ff7f50">
                    <th>ID</th>
                    <th>姓名</th>
                    <th>密码</th>
                    <th>性别</th>
                    <th>家乡</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                <c:forEach var="U" items="${pageBean.beanList}">
                    <form action="UserServlet" method="post">
                        <input type="hidden" name="method" value="update">
                        <input type="hidden" name="pageBean.pageSize" value="${pageBean.pageSize}">
                            <%--表单数据分行显示--%>
                        <tr>
                            <td bgcolor="#ccccff">
                                <input class="input_wid" type="text" value="${U.id}" name="id" readonly="readonly">
                            </td>
                            <td bgcolor="#ccff99">
                                <input class="input_wid" type="text" value="${U.name}" name="name">
                            </td>
                            <td bgcolor="#ccccff">
                                <input class="input_wid" type="text" value="${U.pwd}" name="pwd">
                            </td>
                            <td bgcolor="#ccff99">
                                <input class="input_wid" type="text" value="${U.sex}" name="sex">
                            </td>
                            <td bgcolor="#ccccff">
                                <input class="input_wid" type="text" value="${U.home}" name="home">
                            </td>
                            <td bgcolor="#ccff99">
                                <input class="input_wid" type="text" value="${U.info}" name="info">
                            </td>
                            <td bgcolor="#ccccff">
                                <a href="UserServlet?method=delete&id=${U.id}">
                                        <%--这里传递的参数是一个id，没有传递表单数据--%>
                                    <input type="button" value="删除">
                                </a>
                                <a>
                                        <%--这里是submit方法，传递表单数据--%>
                                    <input type="submit" value="更新">
                                </a>
                            </td>
                        </tr>
                    </form>
                </c:forEach>
            </table>
            &nbsp;&nbsp;
            <div align="center">
                当前第 ${pageBean.pageCurrent} 页 / 共${pageBean.totalPages} 页 &nbsp;
                <a href="UserServlet?method=queryByPage&name=${Criteria.name}&sex=${Criteria.sex}&home=${Criteria.home}&info=${Criteria.info}&pageBean.pageSize=${pageBean.pageSize}&pageBean.pageCurrent=${1}">首页</a>
                &nbsp;
                <c:if test="${pageBean.pageCurrent > 1}">
                    <a href="UserServlet?method=queryByPage&name=${Criteria.name}&sex=${Criteria.sex}&home=${Criteria.home}&info=${Criteria.info}&pageBean.pageSize=${pageBean.pageSize}&pageBean.pageCurrent=${pageBean.pageCurrent-1}">上一页</a>
                    &nbsp;
                </c:if>
                <%--计算begin和end--%>
                <c:choose>
                    <%--如果总页数不足10页，直接显示所有页数--%>
                    <c:when test="${pageBean.totalPages <= 10}">
                        <c:set var="begin" value="1" />
                        <c:set var="end" value="${pageBean.totalPages}" />
                    </c:when>
                    <%--当总页数大于10，通过公式计算--%>
                    <c:otherwise>
                        <c:set var="begin" value="${pageBean.pageCurrent - 5}" />
                        <c:set var="end" value="${pageBean.pageCurrent + 4}" />
                        <%--头溢出--%>
                        <c:if test="${begin < 1}">
                            <c:set var="begin" value="1" />
                            <c:set var="end" value="10" />
                        </c:if>
                        <%--尾溢出--%>
                        <c:if test="${end > pageBean.totalPages}">
                            <c:set var="begin" value="${pageBean.totalPages - 9}" />
                            <c:set var="end" value="${pageBean.totalPages}" />
                        </c:if>
                    </c:otherwise>
                </c:choose>
                <%--循环打印页码--%>
                <c:forEach var="i" begin="${begin}" end="${end}">
                    &nbsp;<span <c:if test="${i == pageBean.pageCurrent}">style="color: red"</c:if>>${i}</span>&nbsp;
                </c:forEach>
                <c:if test="${pageBean.pageCurrent < pageBean.totalPages}">
                    <a href="UserServlet?method=queryByPage&name=${Criteria.name}&sex=${Criteria.sex}&home=${Criteria.home}&info=${Criteria.info}&pageBean.pageSize=${pageBean.pageSize}&pageBean.pageCurrent=${pageBean.pageCurrent+1}">下一页</a>
                    &nbsp;
                </c:if>
                    <%--UserServlet?method=queryByPage&name=${Criteria.name}&sex=${Criteria.sex}&home=${Criteria.home}&info=${Criteria.info}&pageBean.pageSize=${pageBean.pageSize}--%>
                <a href="UserServlet?method=queryByPage&name=${Criteria.name}&sex=${Criteria.sex}&home=${Criteria.home}&info=${Criteria.info}&pageBean.pageSize=${pageBean.pageSize}&pageBean.pageCurrent=${pageBean.totalPages}">尾页</a>
            </div>

            <hr>
        </div>
    </body>
    <script>

    </script>
</html>
