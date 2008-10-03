<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<select id="list2" name="list2" multiple="true" size="10">
    <c:forEach var="option" items="${MainUpdateList.options}">
    <option value="<c:out value="${option}" />"><c:out value="${option}" /></option>
    </c:forEach>
</select>
