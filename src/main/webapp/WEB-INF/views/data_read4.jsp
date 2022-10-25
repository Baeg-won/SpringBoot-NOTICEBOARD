<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.json.simple.JSONObject"%>

<%
//커넥션 선언
Connection con = null;

try {
	//드라이버 호출, 커넥션 연결
	Class.forName("com.mysql.jdbc.Driver").newInstance();
	con = DriverManager.getConnection("jdbc:mysql://localhost:8000/blog");
	ResultSet rs = null;

	//DB에서 뽑아온 데이터(JSON) 을 담을 객체. 후에 responseObj에 담기는 값
	List boardList = new LinkedList();

	//시간대별  평균값을 산출
	String query = "select date(create_date) as 'cd', count(case when category = 'none' then 0 end) as 'none', count(case when category = 'secret' then 0 end) as 'secret', count(case when category = 'screenshot' then 0 end) as 'screenshot', count(case when category = 'question' then 0 end) as 'question' from board where create_date between '2022-10-01' and '2022-10-31' group by cd";
	PreparedStatement pstm = con.prepareStatement(query);
	rs = pstm.executeQuery(query);

	//ajax에 반환할 JSON 생성
	JSONObject responseObj = new JSONObject();
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
	out.print(responseObj.toString());
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
%>