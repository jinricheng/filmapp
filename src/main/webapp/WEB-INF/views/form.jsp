<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
    <title>Film Form</title>
</head>
<body>

<c:choose>
    <c:when test="${film.getId()>0}">
        <h3>Update Film</h3>
        <c:set var="method" value="PUT"/>
        <c:set var="action" value="/films/${film.getId()}"/>
    </c:when>
    <c:otherwise>
        <h3>Create Film</h3>
        <c:set var="method" value="POST"/>
        <c:set var="action" value="/films"/>
    </c:otherwise>
</c:choose>

<form:form method="${method}" action="${action}" modelAttribute="film">
    <table>
        <tr>
            <td><form:label path="title">Title</form:label></td>
            <td><form:input path="title"/> <i><form:errors path="title"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:label path="year">Year</form:label></td>
            <td><form:input path="year"/> <i><form:errors path="year"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:label path="mpaa_rating">Mppa_Rating</form:label></td>
            <td><form:input path="mpaa_rating"/> <i><form:errors path="mpaa_rating"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:label path="runtime">Runtime</form:label></td>
            <td><form:input path="runtime"/> <i><form:errors path="runtime"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:label path="synopsis">Synopsis</form:label></td>
            <td><form:input path="synopsis"/> <i><form:errors path="synopsis"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:label path="email">Email</form:label></td>
            <td><form:input path="email"/> <i><form:errors path="email"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:hidden path="poster"/> <i><form:errors path="poster"></form:errors></i></td>
        </tr>
        <tr>
            <td><form:hidden path="date"/> <i><form:errors path="date"></form:errors></i></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>
</form:form>

</body>
</html>
