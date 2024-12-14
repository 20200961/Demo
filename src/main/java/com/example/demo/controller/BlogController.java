package com.example.demo.controller;


import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired; // Autowired 임포트 추가
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import com.example.demo.model.domain.Article;
import com.example.demo.model.domain.Board;
// import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.AddBoardRequest;
import com.example.demo.model.service.BlogService; // 최상단 서비스 클래스 임포트

import jakarta.servlet.http.HttpSession;

@Controller // 컨트롤러 어노테이션 명시
public class BlogController {
    @Autowired
    BlogService blogService; // DemoController 클래스 아래 객체 생성

    // @GetMapping("/article_list") // 게시판 링크 지정
    // public String article_list(Model model) {
    //     List<Article> list = blogService.findAll(); // 게시판 리스트
    //     model.addAttribute("articles", list); // 모델에 추가
    //     return "templates/article_list"; // .HTML 연결
    // }

    @GetMapping("/article_edit/{id}") // 게시판링크지정
    public String article_edit(Model model, @PathVariable Long id) {
        Optional < Board > list = blogService.findById(id); // 선택한 게시판 글
        if (list.isPresent()) {
            model.addAttribute("article", list.get()); // 존재하면 Article 객체를 모델에 추가
        } else {
            // 오류 처리 페이지로 리다이렉트 또는 예외 처리
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
        return "article_edit"; // .HTML 연결
    }


    // @PutMapping("/api/article_edit/{id}")
    // public String updateArticle(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
    //     blogService.update(id, request);
    //     return "redirect:/article_list"; 
    // }

    @DeleteMapping("/api/article_delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";
    }
    // @GetMapping("/board_list") // 새로운 게시판 링크 지정
    // public String board_list(Model model) {
    // List<Board> list = blogService.findAll(); // 게시판 전체 리스트
    // model.addAttribute("boards", list); // 모델에 추가
    // return "board_list"; // .HTML 연결
    // }
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
    @DeleteMapping("/api/board_delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/board_list";
    }
    @GetMapping("/board_edit/{id}") // 게시판링크지정
    public String board_edit(Model model, @PathVariable Long id) {
        Optional < Board > list = blogService.findById(id); // 선택한 게시판 글
        if (list.isPresent()) {
            model.addAttribute("board", list.get()); // 존재하면 board 객체를 모델에 추가
        } else {
            // 오류 처리 페이지로 리다이렉트 또는 예외 처리
            return "/error_page/article_error"; // 오류 처리 페이지로 연결
        }
        return "board_edit"; // .HTML 연결
    }
    @PutMapping("/api/board_edit/{id}")
    public String updateBoard(@PathVariable Long id, @ModelAttribute AddBoardRequest request) {
        blogService.update(id, request);
        return "redirect:/board_list";
    }
    @GetMapping("/board_write")
    public String board_write() {
    return "board_write";
    }

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
    @GetMapping("/board_list") // 새로운 게시판 링크 지정
    public String board_list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword, HttpSession session) {
        String userId = (String) session.getAttribute("userId"); // 세션 아이디 존재 확인
        String email = (String) session.getAttribute("email"); // 세션에서 이메일 확인
        if (userId == null) {
            return "redirect:/member_login"; // 로그인 페이지로 리다이렉션
        }
        System.out.println("세션 userId: " + userId); // 서버 IDE 터미널에 세션 값 출력

        int itemsPerPage = 3; // 한 페이지에 표시할 항목 수
        PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
        Page < Board > list; // Page를 반환
        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
        } else {
            list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
        }

        int startNum = (page * itemsPerPage) + 1;
        model.addAttribute("boards", list); // 모델에 추가
        model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
        model.addAttribute("currentPage", page); // 페이지 번호
        model.addAttribute("keyword", keyword); // 키워드
        model.addAttribute("startNum", startNum);
        model.addAttribute("email", email); // 로그인 사용자(이메일)
        return "board_list"; // .HTML 연결
    }
}