<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Адреса - вилучення</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <style><%@include file="styles/address_delete.css"%></style>
    <c:if test="${fn:length(error)>0}">
        <div class="alert">
            <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            <h4 style="color:red"><b>${error}</b></h4>
        </div>
    </c:if>

    <div class="header">
        <h4>Вилучення адреси</h4>
    </div>

    <c:if test="${email.id == 0}">
        <li><p style="color:blue">Record for deleting not found!</p></li>
        <li><input class="form-btn"  type="hidden" name="id_owner" value="${owner.id}" required></li>
        <li><a class = "form-btn" href="addresses?id_owner=${owner.id}">Назад</a></li>
    </c:if>

    <c:if test="${addr.id > 0}">
        <form style="alignment: center" action="delete_address" method="post" class="groupTable">
            <table border="2" >
                <tr>
                    <th class="form-field-label">Адреса:</th>
                    <td>
                        <c:out value="${addr.toString()}"/>
                    </td>
                </tr>
            </table>

            <h4>Ви підтверджуєте видалення контакту?</h4>

            <input type="hidden" name="id_address" value="${addr.id}" required>
            <input type="hidden" name="id_owner" value="${owner.id}" required>
            <input class = "form-btn" type="submit" value="Видалити">
            <a class = "form-btn" href="addresses?id_owner=${owner.id}">Назад</a>

        </form>
    </c:if>


</body>
</html>
