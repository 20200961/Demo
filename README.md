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

깃허브 충돌때문에 새로 만들었습니다.. ㅠㅠ
