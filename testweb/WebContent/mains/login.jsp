<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div>${MainLogin.formattedErrorMessage}</div>
<p>chkTest value is ${MainLogin.chkTest[0]}, ${MainLogin.chkTest[1]}, ${MainLogin.chkTest[2]}</p>
<p>QueryString is <%=request.getQueryString()%></p>
<div>
    <form id="frmLogin" name="frmLogin" method="post" action="${MainLogin.controller.formAction}">
    <dl>
    <dt>Login name</dt>
    <dd><input type="text" id="loginName" name="loginName" value="<c:out value="${MainLogin.loginName}"/>"/></dd>
    <dt>Password</dt>
    <dd><input type="password" id="passwd" name="passwd" /> </dd>
    <dt>Test for array input</dt>
    <dd>
        <input type="checkbox" id="chkTest1" name="chkTest" value="1" /> chkTest1
        <input type="checkbox" id="chkTest2" name="chkTest" value="2" /> chkTest2
        <input type="checkbox" id="chkTest3" name="chkTest" value="3" /> chkTest3
    </dd>
    <dt></dt><dd><input type="submit" name="btnLogin" value="Login" /> </dd>
    </dl>
    </form>
</div>