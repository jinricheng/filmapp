<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<body>
<h2>Films Lists</h2>
    <ul>
    <c:if test="${not empty films}">
        <c:forEach var="film" items="${films}">
        <li>${film.getId()}:<a href="/films/${film.getId()}">${fn:escapeXml(film.getTitle())}</a></li>
        </c:forEach>
    </c:if>
    </ul>
    <p><a href="/users">Users List</a></p>
    <p><a href="/films/form">Add New Film</a></p>
    <p><a href="/search">Search films</a></p>
</body>
</html>