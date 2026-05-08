<%-- 成績参照検索画面JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">得点管理システム</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<%-- ① --%>
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績参照</h2>

			<%-- ② 科目情報 フォーム --%>
			<form action="TestListSubjectExecute.action" method="get">
				<div class="border mx-3 mb-2 py-2 px-3 rounded">
					<div class="row align-items-end">
						<%-- ② ラベル --%>
						<div class="col-auto d-flex align-items-center">
							<span>科目情報</span>
						</div>
						<%-- ③ 入学年度 --%>
						<div class="col-2">
							<label class="form-label mb-1" for="ent_year">入学年度</label>
							<select class="form-select form-select-sm" id="ent_year" name="ent_year">
								<option value="0">----</option>
								<c:forEach var="year" items="${ent_year_set}">
									<option value="${year}">${year}</option>
								</c:forEach>
							</select>
						</div>
						<%-- ④ クラス --%>
						<div class="col-2">
							<label class="form-label mb-1" for="class_num">クラス</label>
							<select class="form-select form-select-sm" id="class_num" name="class_num">
								<option value="0">----</option>
								<c:forEach var="num" items="${class_num_set}">
									<option value="${num}">${num}</option>
								</c:forEach>
							</select>
						</div>
						<%-- ⑤ 科目 --%>
						<div class="col-3">
							<label class="form-label mb-1" for="subject_cd">科目</label>
							<select class="form-select form-select-sm" id="subject_cd" name="subject_cd">
								<option value="0">--------</option>
								<c:forEach var="subject" items="${subject_list}">
									<option value="${subject.subjectCd}">${subject.subjectName}</option>
								</c:forEach>
							</select>
						</div>
						<%-- ⑨ 検索ボタン --%>
						<div class="col-auto">
							<button class="btn btn-secondary btn-sm">検索</button>
						</div>
					</div>
				</div>
			</form>

			<%-- ⑩ 学生情報 フォーム --%>
			<form action="TestListStudentExecute.action" method="get">
				<div class="border mx-3 mb-2 py-2 px-3 rounded">
					<div class="row align-items-end">
						<%-- ⑩ ラベル --%>
						<div class="col-auto d-flex align-items-center">
							<span>学生情報</span>
						</div>
						<%-- ⑪ 学生番号ラベル + ⑫ 入力欄 --%>
						<div class="col-4">
							<label class="form-label mb-1" for="student_no">学生番号</label>
							<input class="form-control form-control-sm" type="text"
								id="student_no" name="student_no"
								placeholder="学生番号を入力してください" maxlength="10" required />
						</div>
						<%-- ⑬ 検索ボタン --%>
						<div class="col-auto">
							<button class="btn btn-secondary btn-sm">検索</button>
						</div>
					</div>
				</div>
			</form>

			<%-- ⑭ 案内メッセージ --%>
			<div class="mx-3 mt-1" style="color:#0066cc; font-size:0.9em;">
				※科目情報を選択または学生情報を入力して検索ボタンをクリックしてください
			</div>

		</section>
	</c:param>
</c:import>
