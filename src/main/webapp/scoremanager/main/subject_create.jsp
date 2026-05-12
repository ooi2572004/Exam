<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>
    <c:param name="content">

        <section class="me-4">

            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">
                科目情報登録
            </h2>

            <form action="SubjectCreateExecute.action" method="post" class="px-4">

                <!-- 科目コード -->
                <div class="mb-3">
                    <label class="form-label">科目コード</label>

                    <input type="text"
                           name="subject_cd"
                           class="form-control <c:if test='${not empty errors.cd}'>is-invalid</c:if>"
                           placeholder="科目コードを入力してください"
                           value="${cd}"
                           required>


                    <c:if test="${not empty errors.cd}">
                        <div class="invalid-feedback" style="display:block;">
                            ${errors.cd}
                        </div>
                    </c:if>
                </div>

                <!-- 科目名 -->
                <div class="mb-3">
                    <label class="form-label">科目名</label>

                    <input type="text"
                           name="subject_name"
                           class="form-control"
                           placeholder="科目名を入力してください"
                           value="${name}"
                           required>
                </div>

                <button type="submit" class="btn btn-primary">登録</button>
                <div class="mt-3">
                    <a href="SubjectList.action">戻る</a>
                </div>

            </form>

        </section>

    </c:param>
</c:import>