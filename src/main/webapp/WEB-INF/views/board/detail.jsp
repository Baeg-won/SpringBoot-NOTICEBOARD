<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<input type="hidden" id="board_id" name="board_id" value="${board.id}"/>
	<button class="btn btn-secondary" onclick="location.href='/?page=${page}&sort=${sort}&searchType=${searchType}&searchKeyword=${searchKeyword}'"><i class="fa-solid fa-list"></i> 목록</button>
	<c:if test="${board.user.id == principal.user.id}">
	<span style="float: right;">
		<a href="/board/${board.id}/updateForm" class="btn btn-warning"><i class="fa-solid fa-pen-to-square"></i> 수정</a>
		<button id="btn-delete" class="btn btn-danger"><i class="fa-solid fa-trash"></i> 삭제</button>
	</span>
	</c:if>
	<br><br>
	<div class="d-flex justify-content-between">
		<span><i class="fa-solid fa-user"></i> ${board.user.nickname}</span><br>
		<span>
			<i class="fa-solid fa-calendar-days"></i> ${board.createDate} &nbsp;&nbsp;
			<i class="fa-solid fa-eye"></i> ${board.count} &nbsp;&nbsp;
			<i class="fa-solid fa-thumbs-up"></i> ${board.recommendCount}
		</span>
	</div>
	<hr>
	<div class="form-group">
		<h3>${board.title}</h3>
	</div>
	<hr>
	<div class="form-group">
		<div>${board.content}</div>
	</div>
	<c:choose>
		<c:when test="${board.recommend_state}">
			<div style="text-align: center;">
				<c:choose>
					<c:when test="${board.user.id != principal.user.id}">
						<button onClick="index.recommend(${board.id}, ${board.recommend_state})" class="btn btn-success" style="display: inline-block;">
							<i class="fa-regular fa-thumbs-up"></i> 추천 <span>${board.recommendCount}</span>
						</button>
					</c:when>
					<c:otherwise>
						<button onClick="index.recommend(${board.id}, ${board.recommend_state})" class="btn btn-success" style="display: inline-block;" disabled>
							<i class="fa-regular fa-thumbs-up"></i> 추천 <span>${board.recommendCount}</span>
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
							<i class="fa-regular fa-thumbs-up"></i> 추천 <span>${board.recommendCount}</span>
						</button>
					</c:when>
					<c:otherwise>
						<button onClick="index.recommend(${board.id}, ${board.recommend_state})" class="btn btn-outline-success" style="display: inline-block;" disabled>
							<i class="fa-regular fa-thumbs-up"></i> 추천 <span>${board.recommendCount}</span>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:otherwise>
	</c:choose>
	<br>
	<div class="card">
		<div class="card-body">
			<textarea id="reply-content" class="form-control" rows="1"></textarea>
		</div>
		<div class="card-footer">
			<button id="btn-reply-save" class="btn btn-primary"><i class="fa-solid fa-check"></i> 등록</button>
		</div>
	</div>
	<br>
	<div class="card">
		<div class="card-header"><i class="fa-solid fa-comment"></i> 댓글 <span style="color: red;">[${fn:length(board.replys)}]</span></div>
		<ul id="reply-box" class="list-group">
			<c:forEach var="reply" items="${board.replys}">
				<li id="reply-${reply.id}" class="list-group-item justify-content-between">
					<div class="d-flex">
						<p style="font-weight: bold;"><i class="fa-solid fa-user"></i> ${reply.user.nickname}</p>&nbsp;
						<span style="font-size: small; color: gray;">(${reply.createDateTime})</span>
					</div>
					<div>${reply.content}</div>
					<c:if test="${reply.user.nickname == principal.user.nickname}">
						<button onClick="index.replyDelete(${board.id}, ${reply.id})" class="badge float-right"><i class="fa-solid fa-trash"></i> 삭제</button>
					</c:if>
				</li>
			</c:forEach>
		</ul>
	</div>
	<br>
	<div class="list-group">
		<a href="/board/${board.next_board.id}?page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"
			class="list-group-item list-group-item-action <c:if test="${empty board.next_board}">disabled</c:if>"> <span style="font-weight: bold;"><i class="fa-solid fa-arrow-up"></i>&nbsp; 다음글</span> │ <c:choose>
				<c:when test="${empty board.next_board}">
					다음글이 없습니다.
				</c:when>
				<c:otherwise>
					<span style="color: blue;">${board.next_board.title}</span>
				</c:otherwise>
			</c:choose>
		</a> <a href="/board/${board.prev_board.id}?page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"
			class="list-group-item list-group-item-action <c:if test="${empty board.prev_board}">disabled</c:if>"> <span style="font-weight: bold;"><i class="fa-solid fa-arrow-down"></i>&nbsp; 이전글</span> │ <c:choose>
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