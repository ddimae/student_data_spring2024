<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Addresses</title>
    <meta charset="UTF-8">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <style>
        <%@include file="styles/addresses_person.css" %>
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
                <form action="add_address">
                    <input type="hidden" name="id_owner" value="${owner.id}">
                    <input class="buttonforheader" type="submit" value="Додати адресу">
                </form>
            </li>
            <li><a class = "form-btn" href="${ref_to_return}">Назад</a></li>
    </div>

    <div class="groupTable">
        <table border="2" id="Addresses_table">
            <tr>
                <th>Країна</th>
                <th>Регіон</th>
                <th>Micтo</th>
                <th>Адреса</th>
                <th>Поточний</th>
                <th>Змінити статус</th>
                <th>Вилучити</th>
            </tr>
            <c:forEach items="${addresses}" var="addrEntity">
            <tr>
                <td><c:out value="${addrEntity.getKey().country}"/></td>
                <td><c:out value="${addrEntity.getKey().region}"/></td>
                <td><c:out value="${addrEntity.getKey().city}"/></td>
                <td><c:out value="${addrEntity.getKey().address}"/></td>
                <td>
                    <c:if test="${addrEntity.getValue()==true}">Основний</c:if>
                    <c:if test="${addrEntity.getValue()==false}">Додатковий</c:if>
                </td>
                <td>
                    <form action="address_current">
                        <input type="hidden" name="id_address" value="${addrEntity.getKey().id}">
                        <input type="hidden" name="id_owner" value="${owner.id}">
                        <input class="buttonfortable" type="submit"
                               <c:if test="${addrEntity.getValue()==false}">value="Основний"
                               </c:if>
                               <c:if test="${addrEntity.getValue()==true}">value="Додатковий"
                               </c:if>
                        >
                    </form>
                </td>
                <td>
                    <form action="delete_address">
                        <input type="hidden" name="id_address" value="${addrEntity.getKey().id}">
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
            return confirm('Address буде вилучено. Продовжити?');
        }

        // For sorting
        function sortTable() {
            var table, rows, switching, i, x, y, shouldSwitch;
            table = document.getElementById("addresses_table");
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
