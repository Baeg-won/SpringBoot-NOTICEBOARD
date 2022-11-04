<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 헤더, 사이드바 레이아웃 설정 -->
<%@ include file="layout/header.jsp"%>
<!-- 헤더, 사이드바 레이아웃 설정 끝 -->

<div id="wrapper">
	<div id="content" class="container">
	
		<!-- 게시판 타이틀 -->
		<div class="board-title">|
			<c:if test="${param.category eq 'none'}">자유게시판</c:if>
			<c:if test="${param.category eq 'popular'}">인기게시판</c:if>
			<c:if test="${param.category eq 'secret'}">비밀게시판</c:if>
			<c:if test="${param.category eq 'screenshot'}">스크린샷 게시판</c:if>
			<c:if test="${param.category eq 'question'}">질문과 답변</c:if> 
		</div>
		<!-- 게시판 타이틀 끝 -->
		
		<!-- 정렬 메뉴 및 검색창 -->
		<form action="/board" method="GET" class="form-inline p-2 bd-highlight justify-content-between">
			<div class="dropdown">
				<button type="button" class="btn btn-sort dropdown-toggle" data-toggle="dropdown">정렬 기준</button>
				<div class="dropdown-menu">
					<a class="dropdown-item" href="/board?category=${param.category}&page=${param.page}&sort=id,DESC&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">최신순</a>
					<a class="dropdown-item" href="/board?category=${param.category}&page=${param.page}&sort=count,DESC&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">인기순</a>
					<a class="dropdown-item" href="/board?category=${param.category}&page=${param.page}&sort=recommendCount,DESC&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">추천순</a>
				</div>
			</div>
			<div>
				<select id="select" class="form-control" onchange="selectSearchType()">
					<option value="title">제목</option>
					<option value="nickname">작성자</option>
				</select>
				<input type="hidden" name="category" id="category" value="${param.category}">
				<input type="hidden" name="searchType" id="searchType" value="title">
				<input type="text" name="searchKeyword" id="searchKeyword" class="form-control" placeholder="입력">
				<button type="submit" class="btn btn-search">
					<i class="fa-solid fa-magnifying-glass"></i> 검색
				</button>
			</div>
		</form>
		<!-- 정렬 메뉴 및 검색창 끝 -->
		
		<c:choose>
			<c:when test="${boards.content.size() > 0}">
				<c:choose>
					<c:when test="${param.category eq 'screenshot'}">
						<hr>
						<div class="row justify-content-center">
						
							<!-- 스크린샷 게시판 썸네일 설정 변수 -->
							<script>
								let content, doc, wrap, img, src, div;
								let parser = new DOMParser();
							</script>
							<!-- 스크린샷 게시판 썸네일 설정 변수 끝 -->
							
							<c:forEach var="board" items="${boards.content}">
								<div id="${board.id}" class="card div-screenshot" 
									onclick="location.href='/board/${board.id}/?category=${param.category}&page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}&sortType=${param.sortType}'">
								
									<!-- 스크린샷 게시판 썸네일 설정 -->
									<script>
								    	content = '${board.content}';
									    doc = parser.parseFromString(content, 'text/html');
									    
									    wrap = document.createElement('div');
									    wrap.setAttribute("id", "wrap-${board.id}")
									    wrap.setAttribute("class", "wrap")
									    
									    img = document.createElement('img');
									    img.setAttribute("src", "image/no-image.jpg");
									    if(doc.getElementsByTagName('img')[0] != null) {
									    	src = doc.getElementsByTagName('img')[0].src;
									    	src = src.replace('http://localhost:8000/upload/', 'http://localhost:8000/upload/s_');
									    	img.setAttribute("src", src);
									    }
									    img.setAttribute("class", "thumnail");
									    
									    div = document.getElementById('${board.id}');
									    wrap.appendChild(img);
									    div.appendChild(wrap);
									</script>
									<!-- 스크린샷 게시판 썸네일 설정 끝 -->
									
									<!-- 스크린샷 게시판 카드 -->
									<div class="card-body">
										<div class="flex">
											<div class="card-title screenshot-card-title">${board.title}</div>
											<c:if test="${fn:length(board.replys) > 0}">
												<div class="reply">&nbsp;[${fn:length(board.replys)}]</div>
											</c:if>
										</div>
										<div class="card-text screenshot-bottom">${board.user.nickname}</div>
										<div class="justify-content-between flex">
											<div class="screenshot-bottom"><i class="fa-solid fa-eye"></i> ${board.count}</div>
											<div class="screenshot-bottom"><i class="fa-solid fa-thumbs-up"></i> ${board.recommendCount}</div>
										</div>
									</div>
									<!-- 스크린샷 게시판 카드 끝 -->
									
								</div>
							</c:forEach>
						</div>
					</c:when>
					<c:otherwise>
					
						<!-- 게시글 표 -->
						<table class="table board-table table-hover">
							<thead>
								<tr>
									<th class="board-table-no">번호</th>
									<th class="board-table-title">제목</th>
									<th class="board-table-writer">작성자</th>
									<th class="board-table-date">등록일</th>
									<th class="board-table-view">조회</th>
									<th class="board-table-recommend">추천</th>
								</tr>
							</thead>
							<c:forEach var="board" items="${boards.content}">
								<c:set var="user" value="[${Integer.toString(principal.user.id)}]" />
								<tbody>
									<tr onclick="location.href='/board/${board.id}/?category=${param.category}&page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}&sortType=${param.sortType}'">
										<th>${board.id}</th>
										<th class="board-table-title">${board.title}<c:if test="${fn:length(board.replys) > 0}">
												<span class="reply">[${fn:length(board.replys)}]</span>
											</c:if>
										</th>
										
										<!-- 비밀게시판 분기 -->
										<c:choose>
											<c:when test="${param.category eq 'secret'}">
												<th>익명</th>
											</c:when>
											<c:otherwise>
												<th>${board.user.nickname}</th>
											</c:otherwise>
										</c:choose>
										<!-- 비밀게시판 분기 끝 -->
										
										<th>${board.createDate}</th>
										<th>${board.count}</th>
										<th>${board.recommendCount}</th>
									</tr>
								</tbody>
							</c:forEach>
						</table>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<hr>
				<div class="board-empty" align="center">등록된 게시글이 없습니다.</div>
			</c:otherwise>
		</c:choose>
		
		<br>
		<div align="right">
			<button class="btn btn-write" onclick="location.href='/board/writeForm'">
				<i class="fa-solid fa-pen"></i> 글쓰기
			</button>
		</div>
		
		<!-- 페이징 -->
		<c:set var="startPage" value="${boards.number - boards.number % 5}" />
		<ul class="pagination justify-content-center">
			<li class="page-item <c:if test='${boards.number < 5}'>disabled</c:if>">
				<a class="page-link" href="/board?category=${param.category}&page=${startPage - 5}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"><</a>
			</li>
			<c:forEach var="page" begin="1" end="5">
				<c:if test="${(startPage + page) <= boards.totalPages}">
					<li class="page-item <c:if test='${boards.number eq startPage + page - 1}'>active</c:if>">
						<a class="page-link" href="/board?category=${param.category}&page=${startPage + page - 1}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">${startPage + page}</a>
					</li>
				</c:if>
			</c:forEach>
			<li class="page-item <c:if test='${startPage + 5 > boards.totalPages}'>disabled</c:if>">
				<a class="page-link" href="/board?category=${param.category}&page=${startPage + 5}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">></a>
			</li>
		</ul>
		<!-- 페이징 끝 -->
	</div>

<!-- 스크립트 설정 -->
<script src="/js/board.js"></script>
<script src="/js/user.js"></script>
<!-- 스크립트 설정 끝 -->

<!-- 푸터 레이아웃 설정 -->
<%@ include file="layout/footer.jsp"%>
<!-- 푸터 레이아웃 설정 끝 -->