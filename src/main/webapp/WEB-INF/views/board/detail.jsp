<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<button class="btn btn-secondary" onclick="location.href='/?page=${page}&sort=${sort}&searchType=${searchType}&searchKeyword=${searchKeyword}'">목록</button>
	<c:if test="${board.user.id == principal.user.id}">
	<span style="float: right;">
		<a href="/board/${board.id}/updateForm" class="btn btn-warning">수정</a>
		<button id="btn-delete" class="btn btn-danger">삭제</button>
	</span>
	</c:if>
	<br><br>
  	작성자: <span>${board.user.nickname}</span><br>
	작성일: <span>${board.createDate}</span><br>
	글번호: <span id="board_id">${board.id}</span><br>
	조회수: <span>${board.count}</span>
	<hr>
	<div class="form-group">
		<h3>${board.title}</h3>
	</div>
	<hr>
	<div class="form-group">
		<div>${board.content}</div>
	</div>
	<hr>
	<c:choose>
		<c:when test="${board.recommend_state}">
			<div style="text-align: center;">
				<c:choose>
					<c:when test="${board.user.id != principal.user.id}">
						<button onClick="index.recommend(${board.id}, ${board.recommend_state})" class="btn btn-success" style="display: inline-block;">
							추천 <span>${board.recommendCount}</span>
						</button>
					</c:when>
					<c:otherwise>
						<button onClick="index.recommend(${board.id}, ${board.recommend_state})" class="btn btn-success" style="display: inline-block;" disabled>
							추천 <span>${board.recommendCount}</span>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:when>
		<c:otherwise>
			<div style="text-align: center;">
				<c:choose>
					<c:when test="${board.user.id != principal.user.id}">
						<button onClick="index.recommend(${board.id}, ${board.recommend_state})" class="btn btn-outline-success" style="display: inline-block;">
							추천 <span>${board.recommendCount}</span>
						</button>
					</c:when>
					<c:otherwise>
						<button onClick="index.recommend(${board.id}, ${board.recommend_state})" class="btn btn-outline-success" style="display: inline-block;" disabled>
							추천 <span>${board.recommendCount}</span>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:otherwise>
	</c:choose>
	<hr>
	<div class="card">
		<div class="card-body">
			<textarea id="reply-content" class="form-control" rows="1"></textarea>
		</div>
		<div class="card-footer">
			<button id="btn-reply-save" class="btn btn-primary">댓글 등록</button>
		</div>
	</div>
	<br>
	<div class="card">
		<div class="card-header">댓글 <span style="color: red;">[${fn:length(board.replys)}]</span></div>
		<ul id="reply-box" class="list-group">
			<c:forEach var="reply" items="${board.replys}">
				<li id="reply-${reply.id}" class="list-group-item justify-content-between">
					<div class="d-flex">
						<p style="font-weight: bold;">${reply.user.nickname}</p>&nbsp;
						<span style="font-size: small; color: gray;">(${reply.createDate})</span>
					</div>
					<div>${reply.content}</div>
					<c:if test="${reply.user.nickname == principal.user.nickname}">
						<button onClick="index.replyDelete(${board.id}, ${reply.id})" class="badge float-right">삭제</button>
					</c:if>
				</li>
			</c:forEach>
		</ul>
	</div>
	<br>
	<div class="list-group">
		<a href="/board/${board.next_board.id}?page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"
			class="list-group-item list-group-item-action <c:if test="${empty board.next_board}">disabled</c:if>"> <span style="font-weight: bold;">다음글</span> │ <c:choose>
				<c:when test="${empty board.next_board}">
					다음글이 없습니다.
				</c:when>
				<c:otherwise>
					<span style="color: blue;">${board.next_board.title}</span>
				</c:otherwise>
			</c:choose>
		</a> <a href="/board/${board.prev_board.id}?page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"
			class="list-group-item list-group-item-action <c:if test="${empty board.prev_board}">disabled</c:if>"> <span style="font-weight: bold;">이전글</span> │ <c:choose>
				<c:when test="${empty board.prev_board}">
					이전글이 없습니다.
				</c:when>
				<c:otherwise>
					<span style="color: blue;">${board.prev_board.title}</span>
				</c:otherwise>
			</c:choose>
		</a>
	</div>
</div>
<br>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>