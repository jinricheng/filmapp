<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%--
  Created by IntelliJ IDEA.
  User: jin-123
  Date: 16/01/2015
  Time: 23:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Film Search</title>
</head>
<body>


<h3>Search Film</h3>
    <c:set var="method" value="POST"/>
    <c:set var="action" value="/films/searchResult"/>


<form:form method="${method}" action="${action}">
    <table>
        <tr>
            <td><form >SearchByTitle </form></td>
            <td><input type="text"  name="result" ></td>
        </tr>

        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>
</form:form>

</body>
</html>
