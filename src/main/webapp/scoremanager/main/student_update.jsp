<%-- 学生変更JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp" >
	<c:param name="title">
		得点管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section>
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生情報変更</h2>
			<form action="StudentUpdateExecute.action" method="post">
				<%-- 学生番号はhiddenで送信（変更不可） --%>
				<%-- エラー時はno属性、初回表示時はstudent.studentNoを使用 --%>
				<c:set var="currentNo" value="${not empty no ? no : student.studentNo}" />
				<c:set var="currentName" value="${not empty name ? name : student.studentName}" />
				<c:set var="currentEntYear" value="${not empty ent_year ? ent_year : student.entYear}" />
				<c:set var="currentClassNum" value="${not empty class_num ? class_num : student.classNum}" />
				<c:set var="currentIsAttend" value="${not empty is_attend ? is_attend : student.isAttend()}" />
				<input type="hidden" name="no" value="${currentNo}" />

				<div class="mb-3">
					<label for="ent_year">入学年度</label>
					<select class="form-select" id="ent_year" name="ent_year">
						<option value="0">--------</option>
						<c:forEach var="year" items="${ent_year_set}">
							<%-- 現在の入学年度と一致していた場合selectedを追記 --%>
							<option value="${year}" <c:if test="${year == currentEntYear}">selected</c:if>>${year}</option>
						</c:forEach>
					</select>
				</div>
				<div class="mt-2 text-warning">${errors.get("1")}</div>

				<div class="mb-3">
					<label for="no">学生番号</label><br>
					<%-- 学生番号は変更不可のため読み取り専用で表示 --%>
					<input class="form-control" type="text" id="no" value="${currentNo}" readonly />
				</div>

				<div class="mb-3">
					<label for="name">氏名</label><br>
					<input class="form-control" type="text" id="name" name="name"
						value="${currentName}" required maxlength="30"
						placeholder="氏名を入力してください" />
				</div>

				<div class="mb-3">
					<label for="class_num">クラス</label>
					<select class="form-select" id="class_num" name="class_num">
						<c:forEach var="num" items="${class_num_set}">
							<%-- 現在のクラス番号と一致していた場合selectedを追記 --%>
							<option value="${num}" <c:if test="${num == currentClassNum}">selected</c:if>>${num}</option>
						</c:forEach>
					</select>
				</div>

				<div class="mb-3 form-check">
					<label class="form-check-label" for="is_attend">在学中
						<%-- 在学フラグがたっている場合checkedを追記 --%>
						<input class="form-check-input" type="checkbox" id="is_attend"
							name="is_attend" value="true"
							<c:if test="${currentIsAttend}">checked</c:if> />
					</label>
				</div>

				<div class="mx-auto py-2">
					<button type="submit" class="btn btn-secondary" id="update-button">変更して終了</button>
				</div>
			</form>
			<a href="StudentList.action">戻る</a>
		</section>
	</c:param>
</c:import>