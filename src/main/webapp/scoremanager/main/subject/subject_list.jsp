<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-4">
    <h2>科目管理</h2>

    <p class="mt-3">
        <a href="SubjectCreate.action" class="btn btn-primary">科目登録</a>
    </p>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>科目コード</th>
                <th>科目名</th>
                <th>変更</th>
                <th>削除</th>
            </tr>
        </thead>
        <tbody>

            <c:if test="${empty subjects}">
                <tr>
                    <td colspan="4" class="text-center text-muted">科目が登録されていません</td>
                </tr>
            </c:if>

            <c:forEach var="s" items="${subjects}">
                <tr>
                    <td><c:out value="${s.subjectCd}" /></td>
                    <td><c:out value="${s.subjectName}" /></td>
                    <td><a href="#" class="btn btn-sm btn-outline-secondary">変更</a></td>
                    <td><a href="#" class="btn btn-sm btn-outline-danger">削除</a></td>
                </tr>
            </c:forEach>

        </tbody>
    </table>
</div>