<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Телефон</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <style>
        <%@include file="styles/phone.css" %>
    </style>
    <c:if test="${fn:length(error)>0}">
        <div class="alert">
            <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            <h4 style="color:red"><b>${error}</b></h4>
        </div>
    </c:if>

    <div class="header">
        <li><h4>Редагування номеру телефону</h4></li>
    </div>

    <form action="add_phone" method="post" class="groupTable">
        <input type="hidden" name="id_phone" value="${phone.id}" required>
        <input type="hidden" name="id_owner" value="${owner.id}" required>

        <table border="2" >
            <tr>
                <th for="phone_number" class="form-field-label">Номер телефону</th>
                <td>
                    <input name="phone_number"
                           value="${phone.phoneNumber}"
                           class="form-field-input-input"
                           id="phone_number"
                           required
                           type="tel"
                           name="phone_number"
                           placeholder="Введіть номер телефону"
                           pattern="[0-9]{12}"
                           size="12"
                           title="12 цифр без '+'">
                </td>

            </tr>
            <tr>
                <th for="is_active" class="form-field-label">Чи доступний?</th>
                <td>
                    <select class="form-field-input-input" id="is_active" name="active" required>
                        <option <c:if test="${phone.active}">selected</c:if>>Активний</option>
                        <option <c:if test="${not phone.active}">selected</c:if>>Неактивний</option>
                    </select>
                </td>
            </tr>

            <tr>
                <th for="is_prior" class="form-field-label">Є основним?</th>
                <td>
                    <select class="form-field-input-input" id="is_prior" name="prior" required>
                        <option <c:if test="${phone.prior}">selected</c:if>>Основний</option>
                        <option <c:if test="${not phone.prior}">selected</c:if>>Додатковий</option>
                    </select>
                </td>
            </tr>
        </table>

        <li><input class="form-btn" type="submit" value="Зберегти"></li>
        <li><a class = "form-btn" href="phones?id_owner=${owner.id}">Назад</a></li>
    </form>
</body>

</html>
