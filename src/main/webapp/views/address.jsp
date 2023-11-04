<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Email</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
</head>

<body>
    <style>
        <%@include file="styles/address.css" %>
    </style>
    <c:if test="${fn:length(error)>0}">
        <div class="alert">
            <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            <h4 style="color:red"><b>${error}</b></h4>
        </div>
    </c:if>

    <div class="header">
        <li><h4>Адреса</h4></li>
    </div>

    <form action="add_address"  method="post" class="groupTable">
        <input type="hidden" name="id_address" value="${addr.id}" required>
        <input type="hidden" name="id_owner" value="${owner.id}" required>
        <table border="2" >
            <tr>
                <th for="country" class="form-field-label">Країна</th>
                <td>
                    <input name="country"
                           value="${addr.country}"
                           class="form-field-input-input"
                           id="country"
                           required
                           type="text"
                           placeholder="Країна">
                </td>
            </tr>
            <tr>
                <th for="region" class="form-field-label">Область (регіон)</th>
                <td>
                    <input name="region"
                           value="${addr.region}"
                           class="form-field-input-input"
                           id="region"
<%--                           required--%>
                           type="text"
                           placeholder="Область (регіон)">
                </td>
            </tr>
            <tr>
                <th for="city" class="form-field-label">Населений пункт</th>
                <td>
                    <input name="city"
                           value="${addr.city}"
                           class="form-field-input-input"
                           id="city"
<%--                           required--%>
                           type="text"
                           placeholder="Населений пункт">
                </td>
            </tr>
            <tr>
                <th for="address" class="form-field-label">Адреса</th>
                <td>
                    <input name="address"
                           value="${addr.address}"
                           class="form-field-input-input"
                           id="address"
                    <%--                           required--%>
                           type="text"
                           placeholder="Адреса">
                </td>
            </tr>
<%--            <tr>--%>
<%--                <th for="is_prior" class="form-field-label">Поточний?</th>--%>
<%--                <td>--%>
<%--                    <select class="form-field-input-input" id="is_prior" name="current" required>--%>
<%--                        <option <c:if test="${owner.getAddresses().get(addr)}==true">selected</c:if>>Основний</option>--%>
<%--                        <option <c:if test="${not owner.getAddresses().get(addr)}">selected</c:if>>Додатковий</option>--%>
<%--                    </select>--%>
<%--                </td>--%>
<%--            </tr>--%>
        </table>
        <li><input class="form-btn" type="submit" value="Зберегти"></li>
        <li><a class = "form-btn" href="addresses?id_owner=${owner.id}">Назад</a></li>
    </form>
</body>
</html>