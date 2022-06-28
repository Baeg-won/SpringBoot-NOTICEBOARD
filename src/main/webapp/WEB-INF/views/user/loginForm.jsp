<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form action="/auth/loginProc" method="POST">
		<div class="form-group">
			<label for="username">Username</label> <input type="text" name="username" class="form-control" placeholder="Enter username" id="username">
		</div>
		<div class="form-group">
			<label for="password">Password</label> <input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
		</div>
		<div class="form-group form-check">
			<label class="form-check-label"> <input class="form-check-input" name="remember" type="checkbox"> Remember me
			</label>
		</div>
		<span> 
			<c:if test="${error}">
				<p id="valid" class="alert alert-danger">${exception}</p>
			</c:if>
		</span>
		<button id="btn-login" class="btn btn-primary">Login</button>
		<a href="https://kauth.kakao.com/oauth/authorize?client_id=51f6c7b1350fbf2b147798b81b8649a7&redirect_uri=http://localhost:8000/auth/kakao/callback&response_type=code"> <img height="38px"
			src="/image/kakao_login_btn.png" />
		</a>
	</form>
</div>
<br>

<%@ include file="../layout/footer.jsp"%>