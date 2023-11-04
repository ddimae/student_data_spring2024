<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Студенти</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
</head>
<body>
    <style>
        <%@include file="styles/students.css" %>
    </style>

    <div class="header">
            <li><h4>Група <span>${group}</span></h4></li>
            <li>
                <input class="header-search"
                       type="text" placeholder="Пошук за прізвищем"
                       id="search-text"
                       onkeyup="tableSearch()"
                >
            </li>
            <li> <a class = "form-btn" href="groups">Назад</a></li>
    </div>

    <div class="groupTable">
        <table border="2" id="students_table">
            <tr>
                <th>Прізвище</th>
                <th>Імя</th>
                <th>По-батькові</th>
                <th>Дата народження</th>
                <th>Бюджет/Контракт</th>
                <th>Стипендія</th>
                <th colspan="5">Відомості про студента</th>
            </tr>
            <c:forEach items="${students}" var="stud">
                <tr>
                    <td><c:out value="${stud.lastName}"/></td>
                    <td><c:out value="${stud.firstName}"/></td>
                    <td><c:out value="${stud.middleName}"/></td>
                    <td><c:out value="${stud.dateOfBirth}"/></td>
                    <td>
                        <c:if test="${stud.contract==false}">Бюджет</c:if>
                        <c:if test="${stud.contract==true}">Контракт</c:if>
                    </td>
                    <td>
                        <c:if test="${stud.takeScholarship==true}">ТАК</c:if>
                        <c:if test="${stud.takeScholarship==false}">-</c:if>
                    </td>
                    <td>
                        <form action="edit_student">
                            <input type="hidden" name="id" value="${stud.id}">
                            <input class="buttonfortable" type="submit" value="Змінити">
                        </form>
                    </td>
                    <td>
                        <form action="phones">
                            <input type="hidden" name="id_owner" value="${stud.id}">
                            <input class="buttonfortable" type="submit" value="Телефони">
                        </form>
                    </td>
                    <td>
                        <form action="emails">
                            <input type="hidden" name="id_owner" value="${stud.id}">
                            <input class="buttonfortable" type="submit" value="Пошта">
                        </form>
                    </td>
                    <td>
                        <form action="addresses">
                            <input type="hidden" name="id_owner" value="${stud.id}">
                            <input class="buttonfortable" type="submit" value="Адреси">
                        </form>
                    </td>
<%--                    <td>--%>
<%--                        <form action="student_parents">--%>
<%--                            <input type="hidden" name="id" value="${stud.id}">--%>
<%--                            <input class="buttonfortable" type="submit" value="Батьки">--%>
<%--                        </form>--%>
<%--                    </td>--%>
                </tr>
            </c:forEach>
        </table>
    </div>
    <script type="text/javascript">
        // For confirm deleting
        function confirmation() {
            return confirm('Are you sure you want to do this?');
        }

        // For filtering
        function tableSearch() {
            var phrase = document.getElementById('search-text');
            var table = document.getElementById('students_table');
            var regPhrase = new RegExp(phrase.value, 'i');
            var flag = false;
            for (var i = 1; i < table.rows.length; i++) {
                //!!!
                //flag = false;
                //for (var j = table.rows[i].cells.length - 1; j >= 0; j--) {
                flag = regPhrase.test(table.rows[i].cells[0].innerHTML); //cells[j]
                //if (flag) break;
                //}
                if (flag) {
                    table.rows[i].style.display = "";
                } else {
                    table.rows[i].style.display = "none";
                }

            }
        }

        // For sorting
        function sortTable() {
            var table, rows, switching, i, x, y, shouldSwitch;
            table = document.getElementById("students_table");
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
