<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>

<p><a href="/users">Users List</a></p>
<p><a href="/films">Films List</a></p>
<c:if test="${not empty userfilm}">
    <h2>User ${userfilm.getUsername()}</h2>
    <p>E-mail: ${userfilm.getEmail()}</p>

    <c:if test="${not empty userfilm.getFilms()}">
        <h3>User Films</h3>
        <c:forEach var="film" items="${userfilm.getFilms()}">
            <li>${film.getId()}:<a href="/films/${film.getId()}">${fn:escapeXml(film.getTitle())}</a>
        </c:forEach>
    </c:if>
</c:if>

</body>
</html>
