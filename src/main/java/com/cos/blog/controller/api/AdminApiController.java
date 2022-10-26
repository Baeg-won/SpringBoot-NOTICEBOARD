package com.cos.blog.controller.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminApiController {

	@PostMapping("/api/admin/data")
	public ResponseEntity<?> alarmConfirm() {

		Connection con = null;
		JSONObject responseObj = new JSONObject();
		
		try {
			String url = "jdbc:mysql://localhost:3306/blog?serverTimezone=Asia/Seoul";
			String user = "cos";
			String passwd = "cos1234";
		
			// 드라이버 호출, 커넥션 연결
			con = DriverManager.getConnection(url, user, passwd);

			// DB에서 뽑아온 데이터(JSON) 을 담을 객체. 후에 responseObj에 담기는 값
			List<JSONObject> boardList = new LinkedList<JSONObject>();

			String query = "select date(create_date) as 'cd', "
					+ "count(case when category = 'none' then 0 end) as 'none', "
					+ "count(case when category = 'secret' then 0 end) as 'secret', "
					+ "count(case when category = 'screenshot' then 0 end) as 'screenshot', "
					+ "count(case when category = 'question' then 0 end) as 'question' "
					+ "from board where create_date between date_add(now(), interval - 1 week) and now() group by cd "
					+ "order by cd";
			PreparedStatement pstm = con.prepareStatement(query);
			ResultSet rs = pstm.executeQuery(query);

			// ajax에 반환할 JSON 생성
			JSONObject lineObj = null;

			while (rs.next()) {
				String create_date = rs.getString("cd");
				int none = rs.getInt("none");
				int secret = rs.getInt("secret");
				int screenshot = rs.getInt("screenshot");
				int question = rs.getInt("question");

				lineObj = new JSONObject();
				lineObj.put("create_date", create_date);
				lineObj.put("none", none);
				lineObj.put("secret", secret);
				lineObj.put("screenshot", screenshot);
				lineObj.put("question", question);

				boardList.add(lineObj);
			}

			responseObj.put("boardList", boardList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return new ResponseEntity<>(responseObj, HttpStatus.OK);
	}
}
