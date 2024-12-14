package com.example.demo.model.service;

import lombok.*; // 어노테이션 자동 생성
import com.example.demo.model.domain.Board;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Data // getter, setter, toString, equals 등 자동 생성
public class AddBoardRequest {
    private String content;
    private String title;
    private String user;
    private String count;
    private String likec;
    private String newdate;
    
    public Board toEntity() { // Board 객체 생성
        return Board.builder()
            .content(content)
            .title(title)
            .user(user)
            .count(count)
            .likec(likec)
            .newdate(newdate)
            .build();
    }
}