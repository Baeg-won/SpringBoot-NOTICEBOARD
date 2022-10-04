<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="layout/header.jsp"%>

<div id="outer-container">
	<div id="sidebar" align="center">
		<form id="userProfileImageForm">
			<input type="file" name="profileImageFile" style="display: none;" id="userProfileImageInput" />
		</form>
		<c:choose>
			<c:when test="${not empty principal}">
				<img id="userProfileImage" src="/upload/${principal.user.profile_image_url}" onerror="this.src='/image/profile.jpg'" class="rounded-circle profile"
					onclick="profileImageUpload(${principal.user.id})">
				<div style="font-weight: bold;">${principal.user.nickname}</div>
			</c:when>
			<c:otherwise>
				<img id="userProfileImage" src="/image/profile.jpg" class="rounded-circle profile">
				<div>로그인을 하지 않은 상태입니다.</div>
			</c:otherwise>
		</c:choose>
		<br>
		<ul class="category-ul">
			<li class="category-li" onclick="location.href='/'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 자유게시판</li>
			<li class="category-li" onclick="location.href='/?category=popular'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 인기게시판</li>
			<li class="category-li" onclick="location.href='/?category=secret'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 비밀게시판</li>
			<li class="category-li" onclick="location.href='/?category=screenshot'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 스크린샷 게시판</li>
			<li class="category-li" onclick="location.href='/?category=question'"><i class="fa-solid fa-link"></i>&nbsp;&nbsp; 질문과 답변</li>
		</ul>
	</div>
	<div id="content">
		<form action="/" method="GET" class="form-inline p-2 bd-highlight justify-content-between">
			<div class="dropdown" style="float: left">
				<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">정렬 기준</button>
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
				</select> <input type="hidden" name="searchType" id="searchType" value="title"> <input type="text" name="searchKeyword" id="searchKeyword" class="form-control" placeholder="검색">
				<button type="submit" class="btn btn-primary">
					<i class="fa-solid fa-magnifying-glass"></i> 검색
				</button>
			</div>
		</form>				
		<c:choose>
			<c:when test="${param.category eq 'screenshot'}">
				<hr>
				<div class="row justify-content-center">
					<script>
						let content, erase, img, div, doc;
						var parser = new DOMParser();
					</script>
					<c:forEach var="board" items="${boards.content}">
						<div id="${board.id}" class="card" style="width: 180px; margin: 5px; height: 290px;">
							<script>
						    	content = '${board.content}';
							    doc = parser.parseFromString(content, 'text/html');
							    
							    img = document.createElement('img');
							    img.setAttribute("src", "image/no-image.jpg");
							    if(doc.getElementsByTagName('img')[0] != null) {
							    	img.setAttribute("src", doc.getElementsByTagName('img')[0].src);	
							    }
							    img.setAttribute("width", 178.2);
							    img.setAttribute("height", 170);
							    
							    div = document.getElementById('${board.id}');
							    div.appendChild(img);
							</script>
							<div class="card-body">
								<div class="card-title" style="font-weight: bold;">${board.title}</div>
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
						<c:set var="user" value="[${Integer.toString(principal.user.id)}]" />
						<tbody style="<c:if test='${fn:contains(board.seen, user)}'>color: gray;</c:if>">
							<tr onclick="location.href='/board/${board.id}/?category=${param.category}&page=${param.page}&sort=${param.sort}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}&sortType=${param.sortType}'"
								style="cursor: pointer;">
								<th>${board.id}</th>
								<th>${board.title}<c:if test="${fn:length(board.replys) > 0}">
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
			<button class="btn btn-info" onclick="location.href='/board/writeForm'">
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