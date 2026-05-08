<%-- 成績登録JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp" >
	<c:param name="title">得点管理システム</c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績登録</h2>
			
			<form method="get" action="TestRegist.action">
				<div class="row border mx-3 mb-3 py-2 align-items-center rounded">
					<div class="col-2">
						<label class="form-label" for="f1">入学年度</label>
						<select class="form-select" id="f1" name="f1" required>
							<option value="">--------</option>
							<c:forEach var="year" items="${ent_year_set}">
								<option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2">
						<label class="form-label" for="f2">クラス</label>
						<select class="form-select" id="f2" name="f2" required>
							<option value="">--------</option>
							<c:forEach var="num" items="${class_num_set}">
								<option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-4">
						<label class="form-label" for="f3">科目</label>
						<select class="form-select" id="f3" name="f3" required>
							<option value="">--------</option>
							<c:forEach var="subject" items="${subject_set}">
								<option value="${subject.subjectCd}" <c:if test="${subject.subjectCd == f3}">selected</c:if>>${subject.subjectName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2">
						<label class="form-label" for="f4">回数</label>
						<select class="form-select" id="f4" name="f4" required>
							<option value="">--------</option>
							<c:forEach var="num" items="${num_set}">
								<option value="${num}" <c:if test="${num == f4}">selected</c:if>>${num}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-2 text-center">
						<button class="btn btn-secondary mt-4" type="submit">検索</button>
					</div>
				</div>
			</form>

			<%-- Action側でエラーが作られた場合の表示エリア --%>
			<div class="text-warning mx-3 mb-3">${errors.get("point")}</div>

			<c:if test="${students != null}">
				<form action="TestRegistExecute.action" method="post">
					<%-- 再検索や保存用に必要な情報をhiddenで持たせておく --%>
					<input type="hidden" name="f1" value="${f1}">
					<input type="hidden" name="f2" value="${f2}">
					<input type="hidden" name="f3" value="${f3}">
					<input type="hidden" name="f4" value="${f4}">
					
					<table class="table table-hover mx-3">
						<tr>
							<th>入学年度</th>
							<th>学生番号</th>
							<th>氏名</th>
							<th>点数</th>
						</tr>
						<%-- 学生全員のリストをループ --%>
						<c:forEach var="student" items="${students}" varStatus="status">
							<tr>
								<td>${student.entYear}</td>
								<td>
									${student.studentNo}
									<input type="hidden" name="student_no_${status.index}" value="${student.studentNo}">
								</td>
								<td>${student.studentName}</td>
								<td>
									<%-- Mapからこの学生の成績があるか探す --%>
									<c:set var="test" value="${testMap[student.studentNo]}" />
									
									<%-- 成績があれば point を表示、なければ空欄。min="0" max="100" で0〜100以外を弾く --%>
									<input type="number" class="form-control" name="point_${status.index}" 
									       value="${test != null ? test.point : ''}" min="0" max="100" placeholder="未入力">
								</td>
							</tr>
						</c:forEach>
					</table>
					
					<input type="hidden" name="count" value="${students.size()}">
					<div class="text-center mt-3">
						<button type="submit" class="btn btn-primary">登録して終了</button>
					</div>
				</form>
			</c:if>
		</section>
	</c:param>
</c:import>
