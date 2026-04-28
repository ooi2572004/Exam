<%-- 学生別成績一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">得点管理システム</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績参照</h2>

			<%-- 検索フォーム --%>
			<form method="get">
				<div class="row border mx-3 mb-3 py-2 align-items-end rounded">

					<%-- クラス --%>
					<div class="col-4">
						<label class="form-label" for="class_num">クラス</label>
						<select class="form-select" id="class_num" name="class_num">
							<option value="0">--------</option>
							<c:forEach var="num" items="${class_num_set}">
								<option value="${num}"
									<c:if test="${num == sel_class_num}">selected</c:if>>${num}</option>
							</c:forEach>
						</select>
					</div>

					<%-- 科目（任意） --%>
					<div class="col-4">
						<label class="form-label" for="subject_cd">科目</label>
						<select class="form-select" id="subject_cd" name="subject_cd">
							<option value="0">--------（全科目）</option>
							<c:forEach var="subject" items="${subject_list}">
								<option value="${subject.subjectCd}"
									<c:if test="${subject.subjectCd == sel_subject_cd}">selected</c:if>>${subject.subjectName}</option>
							</c:forEach>
						</select>
					</div>

					<div class="col-4 text-center">
						<button class="btn btn-secondary">検索</button>
					</div>
				</div>
			</form>

			<%-- エラーメッセージ --%>
			<c:if test="${not empty error_msg}">
				<div class="mx-3 text-warning">${error_msg}</div>
			</c:if>

			<%-- 成績一覧テーブル --%>
			<c:if test="${not empty test_list}">
				<div class="mx-3 mb-2">検索結果：${test_list.size()}件</div>
				<div class="table-responsive mx-3">
					<table class="table table-hover table-bordered">
						<thead class="table-secondary">
							<tr>
								<th>学生番号</th>
								<th>氏名</th>
								<th>クラス</th>
								<c:forEach var="subject" items="${subject_list}">
									<c:if test="${empty sel_subject_cd or sel_subject_cd == '0' or sel_subject_cd == subject.subjectCd}">
										<th>${subject.subjectName}</th>
									</c:if>
								</c:forEach>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="tts" items="${test_list}">
								<tr>
									<td>${tts.studentNo}</td>
									<td>${tts.studentName}</td>
									<td>${tts.classNum}</td>
									<c:forEach var="subject" items="${subject_list}">
										<c:if test="${empty sel_subject_cd or sel_subject_cd == '0' or sel_subject_cd == subject.subjectCd}">
											<td class="text-end">
												<c:choose>
													<c:when test="${not empty tts.points[subject.subjectCd]}">${tts.points[subject.subjectCd]}</c:when>
													<c:otherwise>-</c:otherwise>
												</c:choose>
											</td>
										</c:if>
									</c:forEach>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>

		</section>
	</c:param>
</c:import>