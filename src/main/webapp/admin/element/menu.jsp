<%--
  Created by IntelliJ IDEA.
  User: mrzhu
  Date: 8/28/15
  Time: 17:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<li>
    <a href="#"><i class="fa fa-sitemap fa-fw"></i>账户管理<span class="fa arrow"></span></a>
    <ul class="nav nav-second-level">
        <shiro:hasPermission name="accountList">
            <li>
                <a href="javascript:loadPage('accountList.html','#page-wrapper');">账户列表</a>
            </li>
        </shiro:hasPermission>
        <shiro:hasPermission name="accountInfo">
            <li>
                <a href="javascript:loadPage('accountInfo.html','#page-wrapper');">我的账户</a>
            </li>
        </shiro:hasPermission>
    </ul>
</li>