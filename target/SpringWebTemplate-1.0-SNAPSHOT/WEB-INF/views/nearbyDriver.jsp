<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Nearby Driver</title>
</head>
<body>

<p>Nearby Driver</p>

<c:forEach var="driver" items="${displayPlaces2}">
    <tr>
        <td>${driver}</td>
        <br>
    </tr>
</c:forEach>


<p>Another table formula: </p>

<c:forEach var="displayPlaces2" items="${displayPlaces2}">
    <tr>
        <td>${displayPlaces2}</td>
        <br>
    </tr>
</c:forEach>


</body>
</html>
