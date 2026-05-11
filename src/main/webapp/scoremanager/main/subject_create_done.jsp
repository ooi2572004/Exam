<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
    <c:param name="title" value="得点管理システム" />
    <c:param name="content">
        <div class="container mt-4">
            <h2>科目情報登録</h2>

            <p class="mt-3">登録が完了しました</p>

            <p class="mt-3">
                <a href="SubjectList.action" class="btn btn-primary">戻る</a>
                <a href="SubjectList.action" class="btn btn-link">科目一覧</a>
            </p>
        </div>
    </c:param>
</c:import>