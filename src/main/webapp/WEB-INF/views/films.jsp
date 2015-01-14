<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<html>
<body>
<h2>Films List</h2>
    <ul>
    <c:if test="${not empty fims}">
        <c:forEach var="films" items="${films}">
        <li><a href="/films/${film.getId()}">${film.getId()}</a>: ${fn:escapeXml(film.getTitle())}</li>
        </c:forEach>
    </c:if>
    </ul>
    <p><a href="films/form">Add</a></p>
</body>
</html>