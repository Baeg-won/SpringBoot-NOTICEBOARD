<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="layout/header.jsp"%>

<div class="container">
	<form action="/" method="GET" class="form-inline p-2 bd-highlight justify-content-between">
		<div class="dropdown" style="float: left">
			<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">정렬 기준</button>
			<div class="dropdown-menu">
				<a class="dropdown-item" href="/?page=${param.page}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">최신순</a>
				<a class="dropdown-item" href="/sort/view?page=${param.page}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">인기순</a>
				<a class="dropdown-item" href="/sort/recommend?page=${param.page}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">추천순</a>
			</div>
		</div>
		<div style="float: right">
			<select id="select" class="form-control" onchange="selectSearchType()">
				<option value="title">제목</option>
				<option value="nickname">작성자</option>
			</select>
			<input type="hidden" name="searchType" id="searchType" value="title">
			<input type="text" name="searchKeyword" id="searchKeyword" class="form-control" placeholder="검색">
			<button type="submit" class="btn btn-primary">검색</button>
		</div>
	</form>
	<table class="table table-hover">
		<thead>
			<tr>
				<th style="font-weight: bold;">번호</th>
				<th style="font-weight: bold;">제목</th>
				<th style="font-weight: bold;">작성자</th>
				<th style="font-weight: bold;">등록일</th>
				<th style="font-weight: bold;">조회</th>
				<th style="font-weight: bold;">추천</th>
			</tr>
		</thead>
		<c:forEach var="board" items="${boards.content}">
			<tbody>
				<tr onclick="location.href='/board/${board.id}/?page=${param.page}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}'" style="cursor: pointer;">
					<th>${board.id}</th>
					<th>${board.title}<c:if test="${fn:length(board.replys) > 0}">
							<span style="color: red;">[${fn:length(board.replys)}]</span>
						</c:if>
					</th>
					<th>${board.user.nickname}</th>
					<th>${board.createDate}</th>
					<th>${board.count}</th>
					<th>${board.recommendCount}</th>
				</tr>
			</tbody>
		</c:forEach>
	</table>
	<br>
	<div align="right">
		<button class="btn btn-info" onclick="location.href='/board/writeForm'">글쓰기</button>
	</div>

	<c:set var="startPage" value="${boards.number - boards.number % 5}" />
	<ul class="pagination justify-content-center">
		<li class="page-item <c:if test='${boards.number < 5}'>disabled</c:if>"><a class="page-link" href="?page=${startPage - 5}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"><</a></li>
		<c:forEach var="page" begin="1" end="5">
			<c:if test="${(startPage + page) <= boards.totalPages}">
				<li class="page-item <c:if test='${boards.number eq startPage + page - 1}'>active</c:if>"><a class="page-link" href="?page=${startPage + page - 1}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">${startPage + page}</a></li>
			</c:if>
		</c:forEach>
		<li class="page-item <c:if test='${startPage + 5 > boards.totalPages}'>disabled</c:if>"><a class="page-link" href="?page=${startPage + 5}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">></a></li>
	</ul>
</div>
<br>

<script src="/js/board.js"></script>
<%@ include file="layout/footer.jsp"%>