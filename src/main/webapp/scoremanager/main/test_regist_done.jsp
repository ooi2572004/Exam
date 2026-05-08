<%-- 成績登録完了JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">得点管理システム</c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績登録</h2>
			<div class="text-center my-5">
				<div class="alert alert-success d-inline-block px-5 py-3">
					登録が完了しました
				</div>
			</div>
			
			<div class="text-center mt-4">
				<a href="TestRegist.action" class="btn btn-secondary me-3">戻る</a>
				<a href="TestList.action" class="btn btn-outline-primary">成績参照</a>
			</div>
		</section>
	</c:param>
</c:import>