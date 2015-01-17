<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<html>
<body>
<h1>Results of Search</h1>

<h2>Result Lists</h2>
<ul>
    <c:if test="${not empty films}">
        <c:forEach var="film" items="${films}">
            <li>${film.getId()}:<a href="/films/${film.getId()}">${fn:escapeXml(film.getTitle())}</a></li>
        </c:forEach>
    </c:if>
</ul>
<p><a href="/users">Users List</a></p>
<p><a href="/films">Film List</a></p>
</body>
</html>