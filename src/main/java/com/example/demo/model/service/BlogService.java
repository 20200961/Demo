package com.example.demo.model.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.domain.Article;
import com.example.demo.model.repository.BlogRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // 생성자 자동 생성(부분)
public class BlogService {
    @Autowired // 객체 주입 자동화, 생성자 1개면 생략 가능
    private final BlogRepository blogRepository; // 리포지토리 선언
    public List < Article > findAll() { // 게시판 전체 목록 조회
        return blogRepository.findAll();
    }

    public Optional<Article> findById(Long id) { // 게시판특정글조회
        return blogRepository.findById(id);
        }
    public void update(Long id, AddArticleRequest request) {
        Optional<Article> optionalArticle = blogRepository.findById(id); // 단일글조회
        optionalArticle.ifPresent(article -> { //값이있으면
            article.update(request.getTitle(), request.getContent()); // 값을수정
            blogRepository.save(article); // Article 객체에저장
    });
}

    public Article save(AddArticleRequest request){
        // DTO가없는경우이곳에직접구현가능
        // public ResponseEntity<Article> addArticle(@RequestParam String title, @RequestParam String content) {
        // Article article = Article.builder()
        // .title(title)
        // .content(content)
        // .build();
        return blogRepository.save(request.toEntity());
    }
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }
}