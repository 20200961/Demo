package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired; // Autowired 임포트 추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService; // 최상단 서비스 클래스 임포트

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;


@Controller // 컨트롤러 어노테이션 명시
public class MemberController {
    @Autowired
    MemberService memberService;

    @GetMapping("/join_new") // 회원 가입 페이지 연결
    public String join_new() {
        return "join_new"; // .HTML 연결
    }
    @PostMapping("/api/members") // 회원 가입 저장
    public String addmembers(@ModelAttribute AddMemberRequest request) {
        memberService.saveMember(request);
        return "join_end"; // .HTML 연결
    }

@GetMapping("/member_login") // 로그인 페이지 연결
public String member_login() {
    return "login"; // .HTML 연결
}
@PostMapping("/api/login_check") // 로그인(아이디, 패스워드) 체크
public String checkMembers(@ModelAttribute AddMemberRequest request, Model model, HttpSession session) {
    try {
        // 이메일과 비밀번호로 로그인 체크
        Member member = memberService.loginCheck(request.getEmail(), request.getPassword());
        if (member == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        // 세션 설정
        String sessionId = UUID.randomUUID().toString(); // 임의의 고유 ID 생성
        String email = request.getEmail(); // 이메일 가져오기
        session.setAttribute("userId", sessionId); // 세션에 사용자 ID 저장
        session.setAttribute("email", email); // 세션에 이메일 저장
        // 모델에 회원 정보 추가 (필요하면)
        model.addAttribute("member", member);
        return "redirect:/board_list"; // 로그인 성공 후 게시판으로 이동
    } catch (IllegalArgumentException e) {
        // 에러 메시지를 모델에 추가하여 로그인 페이지에 표시
        model.addAttribute("error", e.getMessage());
        return "login"; // 로그인 실패 시 로그인 페이지로 이동
    }
}
@GetMapping("/api/logout") // 로그아웃 버튼 동작
public String member_logout(Model model, HttpServletRequest request2, HttpServletResponse response) {
    try {
        HttpSession session = request2.getSession(false); // 기존 세션 가져오기(존재하지 않으면 null 반환)
        session.invalidate(); // 기존 세션 무효화
        Cookie cookie = new Cookie("JSESSIONID", null); // JSESSIONID is the default session cookie name
        cookie.setPath("/"); // Set the path for the cookie
        cookie.setMaxAge(0); // Set cookie expiration to 0 (removes the cookie)
        response.addCookie(cookie); // Add cookie to the response
        session = request2.getSession(true); // 새로운 세션 생성
        System.out.println("세션 userId: " + session.getAttribute("userId")); // 초기화 후 IDE 터미널에 세션 값 출력
        return "login"; // 로그인 페이지로 리다이렉트
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
        return "login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
    }
}
}