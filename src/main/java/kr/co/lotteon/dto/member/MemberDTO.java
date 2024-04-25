package kr.co.lotteon.dto.member;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String uid;
    private String pass;
    private String name;
    private String nick;
    private String gender;
    private String hp;
    private String email;
    private int point;
    private int level;
    // 위치기반 약관 동의여부 추가(Y/N)
    private String location;
    private String zip;
    private String addr1;
    private String addr2;
    private String company;
    private String ceo;
    private String bizRegNum;
    private String comRegNum;
    private String tel;
    private String manager;
    private String managerHp;
    private String fax;
    private String regip;
    private LocalDateTime wdate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime rdate;


    }
