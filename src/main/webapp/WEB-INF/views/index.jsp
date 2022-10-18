<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="layout/header.jsp"%>

<div id="outer-container">
	<div class="bg-light" id="sidebar" align="center">
		<form id="userProfileImageForm">
			<input type="file" name="profileImageFile" id="userProfileImageInput" style="display: none;"/>
		</form>
		<c:choose>
			<c:when test="${not empty principal}">
				<img id="userProfileImage" src="/upload/${principal.user.profile_image_url}" onerror="this.src='/image/profile.jpg'" class="rounded-circle profile"
					onclick="profileImageUpload(${principal.user.id})" style="cursor: pointer;">
				<div style="font-weight: bold;">${principal.user.nickname}</div>
			</c:when>
			<c:otherwise>
				<img id="userProfileImage" src="/image/profile.jpg" class="rounded-circle profile">
				<div>로그인을 하지 않은 상태입니다.</div>
			</c:otherwise>
		</c:choose>
		<br>
		<ul class="category-ul">
			<li class="category-li" onclick="location.href='/?category=none'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 자유게시판</li>
			<li class="category-li" onclick="location.href='/?category=popular'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 인기게시판</li>
			<li class="category-li" onclick="location.href='/?category=secret'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 비밀게시판</li>
			<li class="category-li" onclick="location.href='/?category=screenshot'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 스크린샷 게시판</li>
			<li class="category-li" onclick="location.href='/?category=question'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 질문과 답변</li>
		</ul>
	</div>
	<div id="content">
		<div class="board-title">|
			<c:if test="${param.category eq 'none'}">자유게시판</c:if>
			<c:if test="${param.category eq 'popular'}">인기게시판</c:if>
			<c:if test="${param.category eq 'secret'}">비밀게시판</c:if>
			<c:if test="${param.category eq 'screenshot'}">스크린샷 게시판</c:if>
			<c:if test="${param.category eq 'question'}">질문과 답변</c:if> 
		</div>
		<form action="/" method="GET" class="form-inline p-2 bd-highlight justify-content-between">
			<div class="dropdown" style="float: left">
				<button type="button" class="btn btn-sort dropdown-toggle" data-toggle="dropdown">정렬 기준</button>
				<div class="dropdown-menu">
					<a class="dropdown-item" href="/?category=${param.category}&page=${param.page}&sort=id,DESC&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">최신순</a> <a class="dropdown-item"
						href="/?category=${param.category}&page=${param.page}&sort=count,DESC&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">인기순</a> <a class="dropdown-item"
						href="/?category=${param.category}&page=${param.page}&sort=recommendCount,DESC&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">추천순</a>
				</div>
			</div>
			<div style="float: right">
				<select id="select" class="form-control" onchange="selectSearchType()">
					<option value="title">제목</option>
					<option value="nickname">작성자</option>
				</select> <input type="hidden" name="searchType" id="searchType" value="title"> <input type="text" name="searchKeyword" id="searchKeyword" class="form-control" placeholder="입력">
				<button type="submit" class="btn btn-search">
					<i class="fa-solid fa-magnifying-glass"></i> 검색
				</button>
			</div>
		</form>
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
						<div id="${board.id}" class="card div-screenshot" onclick="location.href='/board/${board.id}/?category=${param.category}&page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}&sortType=${param.sortType}'">
						
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
							
							<div class="card-body">
								<div style="display: flex;">
									<div class="card-title screenshot-card-title">${board.title}</div>
									<c:if test="${fn:length(board.replys) > 0}">
										<div style="color: red;">&nbsp;[${fn:length(board.replys)}]</div>
									</c:if>
								</div>
								<div class="card-text" style="font-size: small; margin-bottom: 2px;">${board.user.nickname}</div>
								<div class="justify-content-between" style="display: flex;">
									<div style="font-size: small;"><i class="fa-solid fa-eye"></i> ${board.count}</div>
									<div style="font-size: small;"><i class="fa-solid fa-thumbs-up"></i> ${board.recommendCount}</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</c:when>
			<c:otherwise>
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
										<span style="color: red;">[${fn:length(board.replys)}]</span>
									</c:if>
								</th>
								<c:choose>
									<c:when test="${param.category eq 'secret'}">
										<th>익명</th>
									</c:when>
									<c:otherwise>
										<th>${board.user.nickname}</th>
									</c:otherwise>
								</c:choose>
								<th>${board.createDate}</th>
								<th>${board.count}</th>
								<th>${board.recommendCount}</th>
							</tr>
						</tbody>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
		<br>
		<div align="right">
			<button class="btn btn-write" onclick="location.href='/board/writeForm'">
				<i class="fa-solid fa-pen"></i> 글쓰기
			</button>
		</div>
		<c:set var="startPage" value="${boards.number - boards.number % 5}" />
		<ul class="pagination justify-content-center">
			<li class="page-item <c:if test='${boards.number < 5}'>disabled</c:if>"><a class="page-link"
				href="/?category=${param.category}&page=${startPage - 5}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"><</a></li>
			<c:forEach var="page" begin="1" end="5">
				<c:if test="${(startPage + page) <= boards.totalPages}">
					<li class="page-item <c:if test='${boards.number eq startPage + page - 1}'>active</c:if>"><a class="page-link"
						href="/?category=${param.category}&page=${startPage + page - 1}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">${startPage + page}</a></li>
				</c:if>
			</c:forEach>
			<li class="page-item <c:if test='${startPage + 5 > boards.totalPages}'>disabled</c:if>"><a class="page-link"
				href="/?category=${param.category}&page=${startPage + 5}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">></a></li>
		</ul>
	</div>
</div>

<script src="/js/board.js"></script>
<script src="/js/user.js"></script>
<%@ include file="layout/footer.jsp"%>