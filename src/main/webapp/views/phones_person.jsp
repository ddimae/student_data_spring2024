<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Телефони</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <style>
        <%@include file="styles/phones_person.css" %>
    </style>

    <c:if test="${fn:length(message)>0}">
    <div class="alert">
        <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
        <br>
        <h4 style="color:red"><b>${message}</b></h4>
        <br>
    </div>
    </c:if>

    <div class="header">
        <li><h4>Власник: <span>${owner.initialsToString()}</span></h4></li>
        <li>
            <form action="add_phone">
                <input type="hidden" name="id_owner" value="${owner.id}">
                <input class="buttonforheader" type="submit" value="Додати телефон">
            </form>
        </li>
        <li><a class = "form-btn" href="${ref_to_return}">Назад</a></li>
    </div>

    <div class="groupTable">
        <table border="2" id="phones_table">
            <tr>
                <th>Номер телефону</th>
                <th>Актуальний</th>
                <th>Основний</th>
                <th colspan="2">Змінити статус</th>
                <th>Вилучити</th>
            </tr>
            <c:forEach items="${phones}" var="phone">
            <tr>
                <td><c:out value="${phone.phoneNumber}"/></td>
                <td>
                    <c:if test="${phone.active==true}">Активний</c:if>
                    <c:if test="${phone.active==false}">Неактивний</c:if>
                </td>
                <td>
                    <c:if test="${phone.prior==true}">Основний</c:if>
                    <c:if test="${phone.prior==false}">Додатковий</c:if>
                </td>
                <td>
                    <form action="phone_active">
                        <input type="hidden" name="id_phone" value="${phone.id}">
                        <input type="hidden" name="id_owner" value="${owner.id}">
                        <input class="buttonfortable" type="submit"
                               <c:if test="${phone.active==false}">value="Активний"
                        </c:if>
                               <c:if test="${phone.active==true}">value="Неактивний"
                        </c:if>
                        >
                    </form>
                </td>
                <td>
                    <form action="phone_prior">
                        <input type="hidden" name="id_phone" value="${phone.id}">
                        <input type="hidden" name="id_owner" value="${owner.id}">
                        <input class="buttonfortable" type="submit"
                               <c:if test="${phone.prior==false}">value="Основний"
                        </c:if>
                               <c:if test="${phone.prior==true}">value="Додатковий"
                        </c:if>
                        >
                    </form>
                </td>
                <td>
                    <form action="delete_phone">
                        <input type="hidden" name="id_phone" value="${phone.id}">
                        <input type="hidden" name="id_owner" value="${owner.id}">
                        <input class="buttonfortable" type="submit" value="Вилучити">
                    </form>
                </td>
            </tr>
            </c:forEach>
        </table>
    </div>
    <script type="text/javascript">
        // For confirm deleting
        function confirmation() {
            return confirm('Телефон буде вилучено. Продовжити?');
        }

        // For sorting
        function sortTable() {
            var table, rows, switching, i, x, y, shouldSwitch;
            table = document.getElementById("phones_table");
            switching = true;
            //Сделайте цикл, которая будет продолжаться до тех пор, пока
            //никакого переключения не было сделано:
            while (switching) {
                //начните с того, что скажите: никакого переключения не происходит:
                switching = false;
                rows = table.rows;
                //Цикл через все строки таблицы (за исключением
                //во-первых, который содержит заголовки таблиц):
                for (i = 1; i < (rows.length - 1); i++) {
                    //начните с того, что не должно быть никакого переключения:
                    shouldSwitch = false;
                    //Получите два элемента, которые вы хотите сравнить,
                    //один из текущей строки и один из следующей:
                    //Сортируем по полю нуль = имя
                    x = rows[i].getElementsByTagName("TD")[0];
                    y = rows[i + 1].getElementsByTagName("TD")[0];
                    //проверьте, должны ли две строки поменяться местами:
                    if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                        //если это так, отметьте как переключатель и разорвите петлю:
                        shouldSwitch = true;
                        break;
                    }
                }
                if (shouldSwitch) {
                    /*Если переключатель был отмечен, сделайте переключатель
                    и отметьте, что переключение было сделано:*/
                    rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                    switching = true;
                }
            }
        }
    </script>
</body>
</html>
