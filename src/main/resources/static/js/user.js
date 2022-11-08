let index_user = {
	init: function() {
		$("#btn-join").on("click", () => {
			this.join();
		});

		$("#btn-update").on("click", () => {
			this.update();
		});
		
		$("#btn-find").on("click", () => {
			this.find();
		});
	},

	join: function() {
		LoadingWithMask();
		
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
				if (resp.data.hasOwnProperty('valid_email')) {
					$('#valid_email').text(resp.data.valid_email);
					$('#email').focus();
				} else {
					$('#valid_email').text('');
				}
				
				if (resp.data.hasOwnProperty('valid_nickname')) {
					$('#valid_nickname').text(resp.data.valid_nickname);
					$('#nickname').focus();
				} else {
					$('#valid_nickname').text('');
				}
				
				if (resp.data.hasOwnProperty('valid_password')) {
					$('#valid_password').text(resp.data.valid_password);
					$('#password').focus();
				} else {
					$('#valid_password').text('');
				}
				
				if (resp.data.hasOwnProperty('valid_username')) {
					$('#valid_username').text(resp.data.valid_username);
					$('#username').focus();
				} else {
					$('#valid_username').text('');
				}
				
				closeLoadingWithMask();
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
		}
		
		if(!/(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}/.test(data.password)) {            
			$("#valid_password").text("비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");  
			$('#password').focus();
			return false;        
		}
		if(!/^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$/.test(data.nickname)) {            
			$("#valid_nickname").text("닉네임은 특수문자를 제외한 2~10자리여야 합니다.");  
			$('#nickname').focus();            
			return false;        
		}
		
		$("#valid_password").text('');
		$("#valid_nickname").text('');
		
		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			//dataType: "json"
		}).done(function(resp) {
			if(resp.status === 500) {
				$("#valid_nickname").text("이미 사용중인 닉네임입니다.");
				$("#nickname").focus();
				return false;
			}
			alert("회원정보 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},
	
	find: function() {
		LoadingWithMask();
		
		let data = {
			username: $("#username").val(),
			email: $("#email").val()	
		};
		
		$.ajax({
			type: "POST",
			url: "/auth/find",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8"
		}).done(function(resp) {
			if (resp.status == 400) {
				if (resp.data.hasOwnProperty('valid_email')) {
					$('#valid_email').text(resp.data.valid_email);
					$('#email').focus();
				} else {
					$('#valid_email').text('');
				}
				
				if (resp.data.hasOwnProperty('valid_username')) {
					$('#valid_username').text(resp.data.valid_username);
					$('#username').focus();
				} else {
					$('#valid_username').text('');
				}
				
				closeLoadingWithMask();
			} else {				
				alert("임시 비밀번호가 발송되었습니다.");
				location.href = "/auth/loginForm";
			}
		}).fail(function(error) {
			console.log(error);
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

function LoadingWithMask() {
    //화면의 높이와 너비를 구합니다.
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
     
    //화면에 출력할 마스크를 설정해줍니다.
    var mask    = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var spinner = "<div id='spinner' style='position: absolute; top: 45%; left: 50%; margin: -16px 0 0 -16px; display: none; color: #4dff93;' class='spinner-border'></div>";
 
    //화면에 레이어 추가
    $('body')
        .append(mask)
 
    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' : '0.3'
    }); 
  
    //마스크 표시
    $('#mask').show();
  
    //로딩중 이미지 표시
    $('body').append(spinner);
    $('#spinner').show();
}

function closeLoadingWithMask() {
	$('#mask, #spinner').hide();
	$('#mask, #spinner').empty();
}