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
			if(resp.status == 400) {
				alert("회원가입 입력 정보를 다시 확인해주십시오.")
				
				if(resp.data.hasOwnProperty('valid_username')){
					$('#valid_username').text(resp.data.valid_username);
					$('#valid_username').css('color', 'red');
				}
				else $('#valid_username').text('');
				
				if(resp.data.hasOwnProperty('valid_password')){
					$('#valid_password').text(resp.data.valid_password);
					$('#valid_password').css('color', 'red');
				}
				else $('#valid_password').text('');
				
				if(resp.data.hasOwnProperty('valid_email')){
					$('#valid_email').text(resp.data.valid_email);
					$('#valid_email').css('color', 'red');
				}
				else $('#valid_email').text('');
			}
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