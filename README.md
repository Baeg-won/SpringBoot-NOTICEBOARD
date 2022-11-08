# SpringBoot Blog Project

### 1. 프로젝트 개요
- 프로젝트 명칭 : Baeg-won Blog
- 프로젝트 소개 : JPA를 활용한 블로그 및 게시판 프로젝트
- 개발 인원 : 1명
- 주요 기능
	- 폼 로그인 / OAuth 2.0 로그인 기능
	- 게시글 작성 및 수정, 삭제 기능
	- 사용자 프로필, 닉네임, 비밀번호 등 회원정보 수정 기능
	- Cookie를 활용한 Remember Me 기능
	- 이메일 인증을 통한 임시 비밀번호 발급 기능
	- 게시글 댓글 작성 및 좋아요 기능
	- 게시글 최신순, 추천순, 인기순 정렬 및 제목, 작성자에 따른 검색 기능
	- 게시글 댓글 작성시 알림 기능

- 백엔드 개발 언어 : Java
- 백엔드 개발 환경
	- Windows
	- STS4
	- SpringBoot
	- Spring Data JPA
	- Spring Security
	- OAuth 2.0

- 프론트 개발 언어 및 환경
	- HTML5
	- jQuery
	- JavaScript
	- Bootstrap4
	- JSTL
- 데이터베이스 : MySQL

<hr>

### 2. 프로젝트 요구사항
- 폼 로그인 / OAuth 2.0 로그인 기능
- 회원가입 시 유효성 검사
- 이메일 인증을 통한 임시 비밀번호 발급 기능
- 게시글 작성 및 수정, 삭제 기능
- 게시글 댓글 작성 및 좋아요 기능
- 게시글 정렬 및 검색 기능
- 댓글 알림 기능
- 사용자 정보 수정 기능


#### 회원 기능
> - 회원가입 시 유효성 검사를 통과해야 한다.
> - 폼 로그인 / OAuth 2.0 로그인 기능(카카오 로그인)을 사용할 수 있어야 한다.
> - 사용자 비밀번호 분실 시 이메일 인증을 통한 임시 비밀번호를 발급할 수 있어야 한다.
> - 프로필 사진, 닉네임, 비밀번호, 이메일 등의 정보를 수정할 수 있어야 한다.

<br>

#### 게시판 기능
> - 게시글 작성 및 수정, 삭제가 가능해야 한다(게시글 작성자 또는 관리자만 가능).
> - 게시글 작성 시 카테고리를 변경할 수 있으며 카테고리 별로 게시글이 출력되어야 한다.
> - 게시글 정렬 및 검색이 가능해야 한다.
> - 게시글 추천 기능이 있으며 게시글에 댓글이 달릴 시 작성자에게 알림이 전송된다.

<br>

#### 관리자 기능
> - 관리자는 관리자 권한을 통해 회원 및 게시글, 댓글 등을 관리할 수 있다.
> - 회원 관리를 통해 해당 사용자가 작성한 게시글 및 댓글을 확인할 수 있으며 사용자를 임의로 추방할 수 있다.
> - 게시글 관리를 통해 최근 일주일간의 통계를 그래프로 확인할 수 있다.
> - 모든 게시글 및 댓글에 대한 수정 및 삭제 권한을 가지고 있다.


<hr>

### 3. DB 설계
![blog_erd](https://user-images.githubusercontent.com/45421117/200116322-64312c36-56ec-44cd-b208-9fcf0b19425c.png)

<hr>

### 4. API 설계
![user_api](https://user-images.githubusercontent.com/45421117/200116331-ed0d41bb-2c99-46ed-be9d-ec7370a28c75.png)
![board_api](https://user-images.githubusercontent.com/45421117/200116338-fe21cd4c-892e-4f30-bf66-8f1f74547ba1.png)
![reply_api](https://user-images.githubusercontent.com/45421117/200116349-d5239ef3-3319-44f2-b917-d637dbf79f4f.png)
![admin_api](https://user-images.githubusercontent.com/45421117/200116356-0d6eed77-d5c2-45a8-8d93-417dbc03c844.png)
<hr>

### 5. 개발 내용
1. [[SpringBoot / Blog Project] 메인페이지 만들기](https://daegwonkim.tistory.com/249)
2. [[SpringBoot / Blog Project] 로그인, 회원가입 페이지 만들기](https://daegwonkim.tistory.com/250)
3. [[SpringBoot / Blog Project] 회원가입 기능 구현하기](https://daegwonkim.tistory.com/252)
4. [[SpringBoot / Blog Project] 로그인 기능 구현하기](https://daegwonkim.tistory.com/255)
5. [[SpringBoot / Blog Project] 스프링 시큐리티 체험해보기](https://daegwonkim.tistory.com/259)
6. [[SpringBoot / Blog Project] 비밀번호 해쉬 후 회원가입 및 로그인하기](https://daegwonkim.tistory.com/260)
7. [[SpringBoot / Blog Project] 게시글 작성, 삭제, 수정, 페이징](https://daegwonkim.tistory.com/263)
8. [[SpringBoot / Blog Project] 회원정보 수정](https://daegwonkim.tistory.com/266)
9. [[SpringBoot / Blog Project] 카카오 로그인](https://daegwonkim.tistory.com/268)
10. [[SpringBoot / Blog Project] 댓글 구현](https://daegwonkim.tistory.com/270)
11. [[SpringBoot / Blog Project] 중간점검 및 수정](https://daegwonkim.tistory.com/319)
12. [[SpringBoot / Blog Project] 로그인 실패 예외처리](https://daegwonkim.tistory.com/326)
13. [[SpringBoot / Blog Project] Remember Me 기능 구현](https://daegwonkim.tistory.com/329)
14. [[SpringBoot / Blog Project] 회원가입시 validation 체크](https://daegwonkim.tistory.com/332)
15. [[SpringBoot / Blog Project] 커스텀 validation을 통한 중복검사 구현](https://daegwonkim.tistory.com/335)
16. [[SpringBoot / Blog Project] 게시글 작성일 및 조회수 출력](https://daegwonkim.tistory.com/338)
17. [[SpringBoot / Blog Project] 게시글 검색 기능 구현](https://daegwonkim.tistory.com/361)
18. [[SpringBoot / Blog Project] 게시글 추천 기능 구현](https://daegwonkim.tistory.com/362)
19. [[SpringBoot / Blog Project] 게시글 정렬 기능 구현](https://daegwonkim.tistory.com/363)
20. [[SpringBoot / Blog Project] 이전글, 다음글 이동 기능 구현](https://daegwonkim.tistory.com/364)
21. [[SpringBoot / Blog Project] 이전에 봤던 글 표시](https://daegwonkim.tistory.com/365)
22. [[SpringBoot / Blog Project] 댓글 작성 알림 기능](https://daegwonkim.tistory.com/366)
23. [[SpringBoot / Blog Project] 사용자 프로필 이미지 추가](https://daegwonkim.tistory.com/367)
24. [[SpringBoot / Blog Project] JPA Specification을 통한 쿼리 조건 다루기](https://daegwonkim.tistory.com/368)
25. [[SpringBoot / Blog Project] 관리자 페이지 - 회원 관리 구현](https://daegwonkim.tistory.com/369)
26. [[SpringBoot / Blog Project] 관리자 페이지 - 게시글 통계 구현](https://daegwonkim.tistory.com/370)
27. [[SpringBoot / Blog Project] 이메일을 통한 임시 비밀번호 발급](https://daegwonkim.tistory.com/371)

<hr>

### 6. 실행 화면
<details>
<summary>메인 페이지</summary>

#### 홈페이지
![home](https://user-images.githubusercontent.com/45421117/200153100-c0f5c46c-0217-49df-aeab-071078654daf.png)

#### 사이드바
![sidebar](https://user-images.githubusercontent.com/45421117/200153189-0eaca0cc-99cd-42c9-a394-99d55d7c638e.png)
</details>

<details>
<summary>사용자 관련</summary>

#### 로그인 페이지
![login](https://user-images.githubusercontent.com/45421117/200153149-7f7fa3c7-81d6-4d07-94e5-91ce7c373166.png)

#### 로그인에 실패한 경우
![login_fail](https://user-images.githubusercontent.com/45421117/200153161-47a7516d-850c-4870-b92f-6c713df81d06.png)

#### 회원가입 페이지
![join](https://user-images.githubusercontent.com/45421117/200153169-dfee9db1-6e5e-46b9-955e-4b888a4f63d0.png)

#### 회원가입에 실패한 경우
![join_fail](https://user-images.githubusercontent.com/45421117/200153177-b42685fc-bfb9-485a-9b07-b63d76b2ce9f.png)

#### 회원정보 수정 페이지
![profile](https://user-images.githubusercontent.com/45421117/200278727-1cf99a29-9907-430a-92a7-d7f87f351b3f.png)

#### 임시 비밀번호 발급 페이지
![find](https://user-images.githubusercontent.com/45421117/200153210-2daade94-337c-48f6-a062-7909fa2c8fcd.png)
</details>

<details>
<summary>게시글 관련</summary>

#### 게시글 목록 페이지
![board](https://user-images.githubusercontent.com/45421117/200153216-483deec3-a0db-462f-9aaa-ceab189bb399.png)

#### 게시글 상세보기 페이지
![board_detail](https://user-images.githubusercontent.com/45421117/200153219-2de5d063-3517-4a34-8f1e-e66d5df460a3.png)

#### 게시글 작성 페이지
![board_write](https://user-images.githubusercontent.com/45421117/200153220-f3301e64-9281-424e-93b8-8b4fc350f436.png)

#### 댓글 알림 기능
![alarm](https://user-images.githubusercontent.com/45421117/200205577-8ab46e2f-bdf0-4e1c-9be1-5fd4ab7f04fb.png)
</details>

<details>
<summary>관리자 관련</summary>

#### 회원 관리 페이지
![admin_user](https://user-images.githubusercontent.com/45421117/200153233-11f7bf85-4edb-4416-a441-222d60f44790.png)

#### 게시글 관리 모달
![admin_board](https://user-images.githubusercontent.com/45421117/200153235-83c3a225-0c0f-4f7d-bc45-b20cba032145.png)

#### 게시글 통계 페이지
![admin_data](https://user-images.githubusercontent.com/45421117/200153239-d214fe3a-9629-4c43-a0e5-e9786366efe8.png)
</details>

<hr>

### 7. 프로젝트 후기
해당 프로젝트는 스프링부트를 접하고 인터넷에서 무료로 제공된 영상을 참고하며 처음 시도해본 프로젝트였습니다.<br>
회원가입 기능부터 로그인 기능, 유효성 검사 및 중복체크, 게시글 작성 및 수정, 삭제 등과 같은 모든 기능들을 처음 구현해보았으며 많은 것을 배울 수 있었습니다.<br>
또한 영상으로 제공되지 않은 기능에 대한 구현을 진행할 때에는 스스로 기능을 구현해보기 위해 각종 사이트를 찾아보는 등 스스로 공부하는 방법에 대해 습득할 수 있었습니다.<br>

사실 처음 진행해본 프로젝트라서, 아무래도 기능 구현에만 신경쓰다보니 효율적인 코드로 작성했다고 말하기는 힘든 부분이 코드 리뷰를 하면서 많이 보였고, 이 부분이 어떤 식으로 동작하는지에 대해 제대로 이해하지 않고 넘어간 부분이 마음에 걸려, 프로젝트를 진행하면서 지속적으로 수정하고 동작 원리 등을 블로그에 정리해볼 예정입니다.<br>

여기까지 프로젝트 설명을 읽어주셔서 감사드리며, 이 프로젝트가 다른 누군가에게 조금이라도 도움이 된다면 기쁠 것 같습니다.<br>

감사합니다.