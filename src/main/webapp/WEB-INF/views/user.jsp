<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>

<p><a href="/users">Users</a></p>

<c:if test="${not empty user}">
    <h2>User ${user.getUsername()}</h2>
    <p>E-mail: ${user.getEmail()}</p>

    <c:if test="${not empty user.getFilms()}">
        <h3>User Films</h3>
        <c:forEach var="film" items="${user.getFilms()}">
            <li><a href="/films/${film.getId()}">${film.getId()}</a>: ${fn:escapeXml(film.Title())}</li>
        </c:forEach>
    </c:if>
</c:if>

</body>
</html>
