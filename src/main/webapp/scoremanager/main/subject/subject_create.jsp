<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container mt-4">
    <h2>科目情報登録</h2>

    <c:if test="${not empty errorCd}">
        <div class="text-danger mb-2">
            <c:out value="${errorCd}" />
        </div>
    </c:if>
    <c:if test="${not empty errorName}">
        <div class="text-danger mb-2">
            <c:out value="${errorName}" />
        </div>
    </c:if>

    <form action="SubjectCreateExecute.action" method="post">
        <div class="mb-3">
            <label for="cd" class="form-label">科目コード</label>
            <input type="text"
                   id="cd"
                   name="cd"
                   class="form-control"
                   placeholder="科目コードを入力してください"
                   value="${cd}"
                   required />
        </div>

        <div class="mb-3">
            <label for="name" class="form-label">科目名</label>
            <input type="text"
                   id="name"
                   name="name"
                   class="form-control"
                   placeholder="科目名を入力してください"
                   value="${name}"
                   required />
        </div>

        <button type="submit" class="btn btn-primary">登録</button>
        <a href="SubjectList.action" class="btn btn-link">戻る</a>
    </form>
</div>