let index_board = {
	init: function() {
		$("#btn-write").on("click", () => {
			this.write();
		});

		$("#btn-delete").on("click", () => {
			this.deleteById();
		});

		$("#btn-update").on("click", () => {
			this.update();
		});

		$("#btn-reply-save").on("click", () => {
			this.replySave();
		});
	},

	write: function() {
		let data = {
			category: $("#category option:selected").val(),
			title: $("#title").val(),
			content: $("#content").val()
		};

		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			//dataType: "json"
		}).done(function(resp) {
			alert("글쓰기가 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	deleteById: function() {
		let id = $("#board_id").text();

		$.ajax({
			type: "DELETE",
			url: "/api/board/" + id
		}).done(function(resp) {
			alert("글 삭제가 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	update: function() {
		let id = $("#board_id").val();
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};

		$.ajax({
			type: "PUT",
			url: "/api/board/" + id,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			//dataType: "json"
		}).done(function(resp) {
			alert("글 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	replySave: function() {
		let id = $("#board_id").val();
		let data = {
			content: $("#reply-content").val()
		};

		$.ajax({
			type: "POST",
			url: `/api/board/${id}/reply`,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			//dataType: "json"
		}).done(function(resp) {
			alert("댓글 작성이 완료되었습니다.");
			location.href = `/board/${id}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	replyDelete: function(board_id, reply_id) {
		$.ajax({
			type: "DELETE",
			url: `/api/board/${board_id}/reply/${reply_id}`
		}).done(function(resp) {
			alert("댓글 삭제가 완료되었습니다.");
			location.href = `/board/${board_id}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	recommend: function(board_id, recommend_state) {
		let recommend = $("#btn-recommend");
		if (!recommend_state) {
			$.ajax({
				type: "POST",
				url: `/api/board/${board_id}/recommend`,
				dataType: "json"
			}).done(resp => {
				recommend.removeClass("btn-outline-success");
				recommend.addClass("btn-success");

				location.reload();
			}).fail(error => {
				console.log(error);
			});
		} else {
			$.ajax({
				type: "DELETE",
				url: `/api/board/${board_id}/recommend`,
				dataType: "json"
			}).done(resp => {
				recommend.removeClass("btn-success");
				recommend.addClass("btn-outline-success");

				location.reload();
			}).fail(error => {
				console.log(error);
			});
		}
	}
}

index_board.init();

function selectSearchType() {
	$("#searchType").val($("#select option:selected").val())
}