<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form>
		<div class="form-group">
			<label for="username">Username</label> <input type="text" class="form-control" placeholder="Enter username" id="username">
		</div>
		<p id="valid_username"></p>
		<div class="form-group">
			<label for="password">Password</label> <input type="password" class="form-control" placeholder="Enter password" id="password">
		</div>
		<p id="valid_password"></p>
		<div class="form-group">
			<label for="email">Email address</label> <input type="email" class="form-control" placeholder="Enter email" id="email">
		</div>
		<p id="valid_email"></p>
	</form>
	<button id="btn-join" class="btn btn-primary">Join</button>
</div>
<br>

<%@ include file="../layout/footer.jsp"%>
<script src="/js/user.js"></script>