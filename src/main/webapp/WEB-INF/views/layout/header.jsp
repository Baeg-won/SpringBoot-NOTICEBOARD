<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="isAuthenticated()">
	<sec:authentication property="principal" var="principal" />
</sec:authorize>

<!DOCTYPE html>
<html lang="en">
<head>
<title>baeg-won's blog</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
<link href="/css/index.css" rel="stylesheet" type="text/css">
<link href="/css/header.css" rel="stylesheet" type="text/css">
<link href="/css/button.css" rel="stylesheet" type="text/css">
<link href="/css/table.css" rel="stylesheet" type="text/css">
<link href="/css/sidebar.css" rel="stylesheet" type="text/css">
<link href="/css/home.css" rel="stylesheet" type="text/css">
<link href="/css/detail.css" rel="stylesheet" type="text/css">
<link href="/css/admin.css" rel="stylesheet" type="text/css">
<link href="/css/form.css" rel="stylesheet" type="text/css">
<script src="https://kit.fontawesome.com/2804b86193.js" crossorigin="anonymous"></script>
</head>
<body>
	<nav class="navbar navbar-expand-md bg-dark navbar-dark">
		<div id="sidebarCollapse" style="color: white; margin-right: 20px; font-size: 25px; cursor: pointer;"><i class="fa-solid fa-bars"></i></div>
		<a class="navbar-brand" href="/"><i class="fa-solid fa-house"></i> Home</a>
		<div>
			<c:choose>
				<c:when test="${empty principal}">
					<ul class="navbar-nav">
						<li class="nav-item"><a class="nav-link" href="/auth/loginForm"><i class="fa-solid fa-right-to-bracket"></i> Sign in</a></li>
						<li class="nav-item"><a class="nav-link" href="/auth/joinForm"><i class="fa-solid fa-user-plus"></i> Sign up</a></li>
					</ul>
				</c:when>
				<c:otherwise>
					<ul class="navbar-nav">
						<li class="nav-item"><a class="nav-link" href="/user/updateForm"><i class="fa-solid fa-user"></i> Profile</a></li>
						<li class="nav-item"><a class="nav-link" href="/logout"><i class="fa-solid fa-right-from-bracket"></i> Sign out</a></li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
		<c:if test="${fn:length(alarms) > 0}">
			<div class="dropdown">
				<div class="btn btn-alarm bg-dark dropdown" data-toggle="dropdown">
					<i class="fa-solid fa-bell"></i>
					<c:set var="alarm_count" value="0" />
					<c:forEach var="alarm" items="${alarms}">
						<c:if test="${!alarm.alarm_confirm_state}">
							<c:set var="alarm_count" value="${alarm_count + 1}" />
						</c:if>
					</c:forEach>
					<c:if test="${alarm_count > 0}">
						<span class="nav-counter">${alarm_count}</span>
					</c:if>
				</div>
				<div class="dropdown-menu dropdown-menu-right alarm-box">
					<span class="alarm-new">새소식&nbsp;&nbsp;</span><span class="alarm-count">${alarm_count}</span>
					<c:forEach var="alarm" items="${alarms}">
						<div class="dropdown-item alarm" onclick="alarmConfirm(${alarm.id}, ${alarm.board.id})" 
							<c:if test="${alarm.alarm_confirm_state}">, style="background-color: whiteSmoke;"</c:if>
						>
							<table>
								<tr>
									<td rowspan="3" style="padding-right: 15px;">
										<c:choose>
											<c:when test="${not empty alarm.user.profile_image_url}">
												<img class="rounded-circle" src="/upload/${alarm.user.profile_image_url}" onerror="this.src='/image/profile.jpg'" width="50" height="50">
											</c:when>
											<c:otherwise>
												<img class="rounded-circle" src="/image/profile.jpg" width="50" height="50">
											</c:otherwise>
										</c:choose>
									</td>
									<td><div class="alarm-content"><span class="alarm-username">${alarm.user.nickname}</span>님이 댓글을 남겼습니다.</div></td>
									<td rowspan="3" valign="top">${alarm.createDate}</td>
								</tr>
								<tr>
									<td><div class="alarm-content">${alarm.content}</div></td>
								</tr>
								<tr>
									<td class="alarm-content alarm-title">${alarm.board.title}</td>
								</tr>
							</table>
						</div>
					</c:forEach>
				</div>
			</div>
		</c:if>
	</nav>

<%@ include file="sidebar.jsp"%>
<script src="/js/alarm.js"></script>
<script src="/js/sidebar.js"></script>