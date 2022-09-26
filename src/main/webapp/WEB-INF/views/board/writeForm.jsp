<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<br>
<div class="container">
	<form>
		<div class="form-group">
			<label for="title"><i class="fa-solid fa-pen-nib"></i> 제목</label> <input type="text" class="form-control" placeholder="Enter title" id="title">
		</div>
		<div class="form-group">
			<label for="content"><i class="fa-solid fa-pen-nib"></i> 내용</label>
			<textarea class="form-control summernote" rows="5" id="content"></textarea>
		</div>
	</form>
	<button id="btn-write" class="btn btn-primary"><i class="fa-solid fa-check"></i> 등록</button>
</div>
<br>

<script>
	$('.summernote').summernote({
		tabsize : 2,
		height : 300
	});
</script>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>