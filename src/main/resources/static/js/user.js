let index_user = {
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
			nickname: $("#nickname").val(),
			email: $("#email").val()
		};

		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			//dataType: "json"
		}).done(function(resp) {
			if (resp.status == 400) {
				alert("회원가입 입력 정보를 다시 확인해주십시오.")

				if (resp.data.hasOwnProperty('valid_username')) {
					$('#valid_username').text(resp.data.valid_username);
					$('#valid_username').css('color', 'red');
				}
				else $('#valid_username').text('');

				if (resp.data.hasOwnProperty('valid_password')) {
					$('#valid_password').text(resp.data.valid_password);
					$('#valid_password').css('color', 'red');
				}
				else $('#valid_password').text('');

				if (resp.data.hasOwnProperty('valid_nickname')) {
					$('#valid_nickname').text(resp.data.valid_nickname);
					$('#valid_nickname').css('color', 'red');
				}
				else $('#valid_nickname').text('');

				if (resp.data.hasOwnProperty('valid_email')) {
					$('#valid_email').text(resp.data.valid_email);
					$('#valid_email').css('color', 'red');
				}
				else $('#valid_email').text('');
			}
			else {
				alert("회원가입이 완료되었습니다.");
				location.href = "/auth/loginForm";
			}
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},

	update: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			nickname: $("#nickname").val()
		};
		
		if(!data.nickname || data.nickname.trim() === "" || !data.password || data.password.trim() === "") {            
			alert("공백 또는 입력하지 않은 부분이 있습니다.");            
			return false;        
		} else if(!/(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}/.test(data.password)) {            
			alert("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");            
			$('#password').focus();            
			return false;        
		} else if(!/^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$/.test(data.nickname)) {            
			alert("닉네임은 특수문자를 제외한 2~10자리여야 합니다.");            
			$('#nickname').focus();            
			return false;        
		}
		
		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			//dataType: "json"
		}).done(function(resp) {
			if(resp.status === 500) {
				alert("이미 사용중인 닉네임 입니다.");
				$("#nickname").focus();
				return false;
			}
			alert("회원정보 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
}

index_user.init();

function profileImageUpload(user_id) {
	$("#userProfileImageInput").click();

	$("#userProfileImageInput").on("change", (e) => {
		let f = e.target.files[0];

		if (!f.type.match("image.*")) {
			alert("이미지를 등록해야 합니다.");
			return;
		}

		// FormData 객체를 이용하면 form 태그의 필드와 그 값을 나타내는 일련의 key/value 쌍을 담을 수 있음
		let userProfileImageForm = $("#userProfileImageForm")[0];
		let formData = new FormData(userProfileImageForm);

		// 서버에 이미지 전송하기
		$.ajax({
			type: "PUT",
			url: `/api/user/${user_id}/profileImageUrl`,
			data: formData,
			contentType: false,	// x-www-form-urlencoded로 파싱되는 것을 방지
			processData: false,	// contentType을 false로 설정할 경우 QueryString이 자동 설정되는 것을 방지
			enctype: "multipart/form-data",
			dataType: "json"
		}).done(resp => {
			// 사진 전송 성공시 이미지 변경
			let reader = new FileReader();
			reader.onload = (e) => {
				$("#userProfileImage").attr("src", e.target.result);
			}
			reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
		}).fail(error => {
			console.log(error);
		});
	});
}