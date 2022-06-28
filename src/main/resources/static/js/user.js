let index = {
	init: function() {
		$("#btn-join").on("click", () => {
			this.join();
		});
		
		$("#btn-update").on("click", () => {
			this.update();
		});
	},
	
	join: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};
		
		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			//dataType: "json"
		}).done(function(resp) {
			if(resp.status == 500)
				alert("중복되는 사용자 아이디입니다.");	
			else {
				alert("회원가입이 완료되었습니다.");
				location.href = "/";	
			}
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},
	
	update: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};
		
		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			//dataType: "json"
		}).done(function(resp) {
			alert("회원정보 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
}

index.init();