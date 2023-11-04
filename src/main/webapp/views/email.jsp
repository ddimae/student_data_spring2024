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
        <%@include file="styles/email.css" %>
    </style>
    <c:if test="${fn:length(error)>0}">
        <div class="alert">
            <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            <h4 style="color:red"><b>${error}</b></h4>
        </div>
    </c:if>

    <div class="header">
        <li><h4>Редагування email</h4></li>
    </div>

    <form action="add_email" method="post" class="groupTable">
        <input type="hidden" name="id_email" value="${email.id}" required>
        <input type="hidden" name="id_owner" value="${owner.id}" required>


        <table border="2" >
            <tr>
                <th for="email" class="form-field-label">Email</th>
                <td>
                    <input name="email"
                           value="${email.email}"
                           class="form-field-input-input"
                           id="email"
                           required
                           type="email"
                           placeholder="Введіть email">
                </td>

            </tr>
            <tr>
                <th for="is_active" class="form-field-label">Чи доступний?</th>
                <td>
                    <select class="form-field-input-input" id="is_active" name="active" required>
                        <option <c:if test="${email.active}">selected</c:if>>Активний</option>
                        <option <c:if test="${not email.active}">selected</c:if>>Неактивний</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th for="is_prior" class="form-field-label">Є основним?</th>
                <td>
                    <select class="form-field-input-input" id="is_prior" name="prior" required>
                        <option <c:if test="${email.prior}">selected</c:if>>Основний</option>
                        <option <c:if test="${not email.prior}">selected</c:if>>Додатковий</option>
                    </select>
                </td>
            </tr>
        </table>

        <li><input class="form-btn" type="submit" value="Зберегти"></li>
        <li><a class = "form-btn" href="emails?id_owner=${owner.id}">Назад</a></li>
    </form>
</body>
</html>