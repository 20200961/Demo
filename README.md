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
