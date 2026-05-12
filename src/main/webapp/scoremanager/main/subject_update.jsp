<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp" >
<c:param name="title">得点管理システム</c:param>
<c:param name="content">
<section class="me-4">
<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>
<form action="SubjectUpdateExecute.action" method="post" class="mx-3">
<div class="mb-3">
<label class="form-label">科目コード</label>
<div class="ms-3 mb-2">${subject.subjectCd}</div>
<input type="hidden" name="cd" value="${subject.subjectCd}">
<div class="text-warning mt-1">${errors.get("cd")}</div>
</div>
<div class="mb-3">
<label class="form-label" for="name">科目名</label>
<input type="text" class="form-control" id="name" name="name" value="${subject.subjectName}" required>
</div>
<button class="btn btn-primary" type="submit">変更</button>
</form>
<div class="mx-3 mt-3">
<a href="SubjectList.action">戻る</a>
</div>
</section>
</c:param>
</c:import>