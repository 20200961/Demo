2주차 연습문제 : <a href="hello2.html">두번째 헬로 페이지</a>로 URL 맵핑
\project\demo\src\main\resources\templates\hello.html

3주차 연습문제 : <a class="btn btn-primary py-3 px-5" href="index.html">되돌아가기</a> 로 구현
\project\demo\src\main\resources\static\about_detail.html

4주차 연습문제 : DemoController.java에서 
@GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        TestDB test = testService.findByName("홍길동");
        model.addAttribute("data4", test);
        TestDB test1 = testService.findByName("아저씨");
        model.addAttribute("data5", test1);
        TestDB test2 = testService.findByName("꾸러기");
        model.addAttribute("data6", test2);
        return "testdb";
    }
로 수정하고 testdb.html에서 각 데이터 베이스가 출력되도록 data4,5,6 을 나누어 출력
\project\demo\src\main\resources\templates\testdb.html

7주차 연습문제
blogcontroller에 다음 코드를 넣어 맵핑을 지정한다.
@PutMapping("/api/board_edit/{id}")
    public String updateBoard(@PathVariable Long id, @ModelAttribute AddBoardRequest request) {
        blogService.update(id, request);
        return "redirect:/board_list";
    }
이후 blogservice.java에
public void update(Long id, AddBoardRequest request) {
        Optional<Board> optionalBoard = blogRepository.findById(id); // 단일글조회
        optionalBoard.ifPresent(board -> { //값이있으면
            board.update(request.getTitle(), request.getContent(),request.getUser()
            ,request.getNewdate(), request.getCount(), request.getLikec()); // 값을수정
            blogRepository.save(board); // Board 객체에저장
        });
    }
update를 작성하여 레포지토리에 저장되게 한다.
blog_edit에서 수정 버튼을 누르면 데이터베이스에 저장후 board_list로 리다이렉트 한다.

8주차 연습문제
- 게시글 ID 대신 글 번호 출력
board_list.html에 다음 코드를 사용하여
<td th:text="${startNum + iterStat.index}"></td>
페이지 기반 글 번호를 사용해서 페이지가 시작되는 순서에 맞게 글번호가 순서대로 나올 수 있게 하였다.

- 게시글 삭제 구현
blogcontroller에 삭제 맵핑을 추가한다
@DeleteMapping("/api/board_delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/board_list";
    }
이후 blogservice에
private final BoardRepository blogRepository; // 리포지토리 선언
public void delete(Long id) {
        blogRepository.deleteById(id);
    }
을 추가하여 BoardRepository 있는 delete 기능을 구현한다.
데이터 베이스에 저장된 지정 id와 관련된 내용을 삭제한다.

9주차 연습문제
import jakarta.validation.constraints.*;을 import한 후
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "이름은 특수문자를 포함할 수 없습니다.")
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z]).{8,}")
    private String password;

    @NotBlank
    @Min(value = 19, message = "나이는 19세 이상이어야 합니다.") // 나이 최소 19
    @Max(value = 90, message = "나이는 90세 이하이어야 합니다.")
    private String age;

    private String mobile;
    
    private String address;
    을 통해 어노테이션을 추가하여 회원가입 조건에 맞게 한다.
    member.java에 moblie과 address의 nullable = "true"로 설정하여 공백이 가능하게 하였다.

10주차 연습문제
- 게시글 추가(글 저장하기) 현재 작성자로 저장
다음 코드를 blogcontroller에 사용하여 현재 로그인한 이메일을 작성자로 저장한다.
@PostMapping("/api/boards") // 글쓰기 게시판 저장
    public String addboards(@ModelAttribute AddBoardRequest request,HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null || email.isEmpty()) {
            return "redirect:/member_login"; // 로그인 페이지로 리다이렉션
        }
        request.setUser(email);
        blogService.save(request);
        return "redirect:/board_list"; // .HTML 연결
    }
이후 board_write에에
<input type="hidden" id="user" name="user" th:value="${email}">
값을 email로 지정해서 작성자에 이메일이 추가된다

- 게시글 내용 보기-> 글의 작성자만 수정/삭제 버튼을 보여준다.
먼저 blogcontroller에 다음 코드로 수정하여 로그인한 사용자의 email을 model에 추가한다.
@GetMapping("/board_view/{id}") // 게시판 링크 지정
    public String board_view(Model model, @PathVariable Long id,HttpSession session) {
        Optional < Board > list = blogService.findById(id); // 선택한 게시판 글
        if (list.isPresent()) {
            model.addAttribute("boards", list.get()); // 존재할 경우 실제 Article 객체를 모델에 추가
        } else {
            // 처리할 로직 추가 (예: 오류 페이지로 리다이렉트, 예외 처리 등)
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/member_login"; // 로그인 페이지로 리다이렉션
        }
    
        model.addAttribute("email", email);

        return "board_view"; // .HTML 연결
    }
이후 blog_view.html에 다음 코드를 사용하여 작성자와 로그인한 사용자의 이메일이 같을때만 수정과 삭제 버튼이 띄워지게 한다
<div th:if="${board.user eq email}">
            <!-- 수정 버튼 -->
            <a class="btn btn-warning" th:href="@{/board_edit/{id}(id=${board.id})}">수정</a>
            <!-- 삭제 버튼 -->
            <form th:action="@{/api/board_delete/{id}(id=${board.id})}" method="post" style="display:inline;">
            <input type="hidden" name="_method" value="delete">
        <button type="submit" class="btn btn-danger">삭제</button>
    </form>
</div>

11주차 연습문제

