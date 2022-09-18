<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form>
		<div class="form-group">
			<label for="username">Username</label> <input type="text" value="${principal.user.username}" class="form-control" placeholder="Enter username" id="username" readonly>
		</div>
		<c:choose>
			<c:when test="${empty principal.user.oauth}">
				<div class="form-group">
					<label for="password">Password</label> <input type="password" class="form-control" placeholder="Enter password" id="password">
				</div>
				<p id="valid_password"></p>
				<div class="form-group">
					<label for="nickname">Nickname</label> <input type="text" value="${principal.user.nickname}" class="form-control" placeholder="Enter nickname" id="nickname">
				</div>
				<p id="valid_nickname"></p>
				<div class="form-group">
					<label for="email">Email address</label> <input type="email" value="${principal.user.email}" class="form-control" placeholder="Enter email" id="email" readonly>
				</div>
			</c:when>
			<c:otherwise>
				<div class="form-group">
					<label for="nickname">Nickname</label> <input type="text" value="${principal.user.nickname}" class="form-control" placeholder="Enter nickname" id="nickname" readonly>
				</div>
				<div class="form-group">
					<label for="email">Email address</label> <input type="email" value="${principal.user.email}" class="form-control" placeholder="Enter email" id="email" readonly>
				</div>
			</c:otherwise>
		</c:choose>
	</form>
	<c:choose>
		<c:when test="${empty principal.user.oauth}">
			<button id="btn-update" class="btn btn-primary">완료</button>
		</c:when>
		<c:otherwise>
			<button class="btn btn-primary" onclick="history.back()">뒤로</button>
		</c:otherwise>
	</c:choose>
</div>
<br>

<%@ include file="../layout/footer.jsp"%>
<script src="/js/user.js"></script>