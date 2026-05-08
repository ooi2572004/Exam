<%-- 学生別成績一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">得点管理システム</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績一覧（学生）</h2>

			<%-- 再検索フォーム（検索画面と同じレイアウト） --%>
			<div class="border mx-3 mb-3 py-2 px-3 rounded">
				<%-- 科目情報フォーム --%>
				<form action="TestListSubjectExecute.action" method="get">
					<div class="row align-items-end mb-2">
						<div class="col-auto d-flex align-items-center">
							<span>科目情報</span>
						</div>
						<div class="col-2">
							<label class="form-label mb-1" for="ent_year">入学年度</label>
							<select class="form-select form-select-sm" id="ent_year" name="ent_year">
								<option value="0">----</option>
								<c:forEach var="year" items="${ent_year_set}">
									<option value="${year}">${year}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-2">
							<label class="form-label mb-1" for="class_num">クラス</label>
							<select class="form-select form-select-sm" id="class_num" name="class_num">
								<option value="0">----</option>
								<c:forEach var="num" items="${class_num_set}">
									<option value="${num}">${num}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-3">
							<label class="form-label mb-1" for="subject_cd">科目</label>
							<select class="form-select form-select-sm" id="subject_cd" name="subject_cd">
								<option value="0">--------</option>
								<c:forEach var="subject" items="${subject_list}">
									<option value="${subject.subjectCd}">${subject.subjectName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="col-auto">
							<button class="btn btn-secondary btn-sm">検索</button>
						</div>
					</div>
				</form>

				<%-- 学生情報フォーム --%>
				<form action="TestListStudentExecute.action" method="get">
					<div class="row align-items-end">
						<div class="col-auto d-flex align-items-center">
							<span>学生情報</span>
						</div>
						<div class="col-4">
							<label class="form-label mb-1" for="student_no">学生番号</label>
							<input class="form-control form-control-sm" type="text"
								id="student_no" name="student_no"
								value="${sel_student_no}"
								placeholder="学生番号を入力してください" maxlength="10" required />
						</div>
						<div class="col-auto">
							<button class="btn btn-secondary btn-sm">検索</button>
						</div>
					</div>
				</form>
			</div>

			<%-- ① 氏名表示 --%>
			<c:if test="${not empty test_to_student}">
				<div class="mx-3 mb-1">
					氏名：${test_to_student.studentName}（${test_to_student.studentNo}）
				</div>
			</c:if>

			<%-- エラーメッセージ（代替フロー②） --%>
			<c:if test="${not empty error_msg}">
				<div class="mx-3 mb-2 text-warning">${error_msg}</div>
			</c:if>

			<%-- ② 成績一覧テーブル --%>
			<c:if test="${not empty test_to_student and empty error_msg}">
				<div class="table-responsive mx-3">
					<table class="table table-hover table-bordered">
						<thead class="table-secondary">
							<tr>
								<%-- ③ 科目名 ④ 科目コード ⑤ 回数 ⑥ 点数 --%>
								<th>科目名</th>
								<th>科目コード</th>
								<th>回数</th>
								<th>点数</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="record" items="${test_to_student.records}">
								<tr>
									<td>${record.subjectName}</td>
									<td>${record.subjectCd}</td>
									<td>${record.no}</td>
									<td class="text-end">
										<c:choose>
											<c:when test="${not empty record.point}">${record.point}</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</section>
	</c:param>
</c:import>