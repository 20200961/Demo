package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired; // Autowired 임포트 추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.domain.TestDB; // TestDB 도메인 임포트 추가
import com.example.demo.model.service.TestService; // 최상단 서비스 클래스 임포트

@Controller // 컨트롤러 어노테이션 명시
public class DemoController{
    @Autowired
    TestService testService; // DemoController 클래스 아래 객체 생성

    @GetMapping("/hello") // 전송 방식 GET
    public String hello(Model model) {
        model.addAttribute("data", " 방갑습니다."); // model 설정
        return "hello"; // hello.html 연결
    }
    @GetMapping("/test1")
    public String thymeleaf_test1(Model model) {
    model.addAttribute("data1", "<h2> 방갑습니다 </h2>");
    model.addAttribute("data2", "태그의 속성 값");
    model.addAttribute("link", 01);
    model.addAttribute("name", "홍길동");
    model.addAttribute("para1", "001");
    model.addAttribute("para2", 002);
    return "temp1";
    }
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
    
}