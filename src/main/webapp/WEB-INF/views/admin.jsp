<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 헤더, 사이드바 레이아웃 설정 -->
<%@ include file="layout/header.jsp"%>
<%@ include file="layout/sidebar.jsp"%>
<!-- 헤더, 사이드바 레이아웃 설정 끝 -->

	<div id="content">
		<c:if test="${category eq 'user'}">
			<div class="board-title">| 회원 관리</div>
			
			<!-- 정렬 및 검색 탭 -->
			<form action="/admin" method="GET" class="form-inline bd-highlight justify-content-between">
				<div></div>
				<div>
					<select id="select" class="form-control" onchange="selectSearchType()">
						<option value="username">아이디</option>
						<option value="nickname">닉네임</option>
					</select>
					<input type="hidden" name="category" id="category" value="${param.category}">
					<input type="hidden" name="searchType" id="searchType" value="username">
					<input type="text" name="searchKeyword" id="searchKeyword" class="form-control" placeholder="입력">
					<button type="submit" class="btn btn-search">
						<i class="fa-solid fa-magnifying-glass"></i> 검색
					</button>
				</div>
			</form>
			<!-- 정렬 및 검색 탭 끝 -->
		
			<!-- 회원 관리 탭 -->
			<table class="table board-table table-hover">
				<thead>
					<tr>
						<th class="board-table-no">번호</th>
						<th class="board-table-writer">아이디</th>
						<th class="board-table-writer">닉네임</th>
						<th class="board-table-date">이메일</th>
						<th class="board-table-date">가입일</th>
						<th class="board-table-no">게시글수</th>
						<th class="board-table-no">댓글수</th>
						<th class="board-table-no">관리</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="user" items="${users.content}">
						<tr>
							<th>${user.id}</th>				
							<th>${user.username}</th>
							<th>${user.nickname}</th>
							<th>${user.email}</th>
							<th>${user.createDate}</th>
							<th>
								<button type="button" class="btn-admin-modal" data-toggle="modal" data-target="#modal" onclick="modalOpen('board', '${user.nickname}', ${user.id})">
									${user.boardCount}
								</button>
							</th>
							<th>
								<button type="button" class="btn-admin-modal" data-toggle="modal" data-target="#modal" onclick="modalOpen('reply', '${user.nickname}', ${user.id})">
									${user.replyCount}
								</button>
							</th>
							<th><button class="btn btn-admin" onclick="userKick(${user.id})">회원 추방</button></th>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<!-- 회원 관리 탭 끝 -->
			
			<!-- 페이징 -->
			<c:set var="startPage" value="${users.number - users.number % 5}" />
			<ul class="pagination justify-content-center">
				<li class="page-item <c:if test='${users.number < 5}'>disabled</c:if>">
					<a class="page-link" href="/admin?category=${param.category}&page=${startPage - 5}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}"><</a>
				</li>
				<c:forEach var="page" begin="1" end="5">
					<c:if test="${(startPage + page) <= users.totalPages}">
						<li class="page-item <c:if test='${users.number eq startPage + page - 1}'>active</c:if>">
							<a class="page-link" href="/admin?category=${param.category}&page=${startPage + page - 1}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">${startPage + page}</a>
						</li>
					</c:if>
				</c:forEach>
				<li class="page-item <c:if test='${startPage + 5 > users.totalPages}'>disabled</c:if>">
					<a class="page-link" href="/admin?category=${param.category}&page=${startPage + 5}&searchType=${param.searchType}&searchKeyword=${param.searchKeyword}">></a>
				</li>
			</ul>
			<!-- 페이징 끝 -->
		</c:if>
		
		<c:if test="${category eq 'summary'}">
			<div id="barchart_material" style="width: 900px; height: 500px;"></div>
		</c:if>
	</div>
</div>

<!-- 게시글 수 모달 -->
<div class="modal fade" id="modal">
  <div class="modal-dialog modal-lg modal-dialog-scrollable">
    <div class="modal-content">

      <!-- Modal Header -->
      <div class="modal-header">
        <h4 class="modal-title" id="modalTitle"></h4>
        <button type="button" class="close" data-dismiss="modal">&times;</button>
      </div>

      <!-- Modal body -->
      <div class="modal-body" id="modalBody">
	      <table class="table board-table table-hover">
				<thead id="modalTableHead"></thead>
				<tbody id="modalTableBody"></tbody>
		  </table>
      </div>

      <!-- Modal footer -->
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="modalClose()">Close</button>
      </div>

    </div>
  </div>
</div>
<!-- 게시글 수 모달 끝 -->

<!-- 스크립트 설정 -->
<script src="/js/board.js"></script>
<script src="/js/user.js"></script>
<script src="/js/admin.js"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script type="text/javascript">
  google.charts.load('current', {'packages':['bar']});
  google.charts.setOnLoadCallback(drawChart);

  function drawChart() {
    var data = google.visualization.arrayToDataTable([
      ['Month', '자유게시판', '인기게시판', '비밀게시판'],
      ['09', 1000, 400, 200],
      ['10', 1170, 460, 250],
      ['11', 660, 1120, 300],
      ['12', 1030, 540, 350]
    ]);

    var options = {
      chart: {
        title: 'Company Performance',
        subtitle: 'Sales, Expenses, and Profit: 2014-2017',
      },
      bars: 'horizontal' // Required for Material Bar Charts.
    };

    var chart = new google.charts.Bar(document.getElementById('barchart_material'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
  }
</script>
<!-- 스크립트 설정 끝 -->

<!-- 푸터 레이아웃 설정 -->
<%@ include file="layout/footer.jsp"%>
<!-- 푸터 레이아웃 설정 끝 -->