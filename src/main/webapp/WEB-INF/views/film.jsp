<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<body>

<p><a href="/films">films</a></p>

<c:if test="${not empty film}">
    <h2>Film id ${film.getId()}</h2>
    <p>Title: ${fn:escapeXml(film.getTitle())} (<a href="/films/${film.getId()}/form">edit</a>)</p>
    <p>Year: ${fn:escapeXml(film.getYear())}</p>
    <p>Mpaa_Rating: ${fn:escapeXml(film.getMpaa_rating())}</p>
    <p>Runtime: ${fn:escapeXml(film.getRuntime())}</p>
    <p>Sypnosis: ${fn:escapeXml(film.getSynopsis())}</p>
    <p>Poster:<img src = "${fn:escapeXml(film.getPoster())}" /></p>
    <p>By ${film.getEmail()} on ${film.getDate()}</p>

    <form:form method="DELETE" action="/films/${film.getId()}">
        <p><input type="submit" value="Delete"/></p>
    </form:form>
</c:if>

</body>
</html>
