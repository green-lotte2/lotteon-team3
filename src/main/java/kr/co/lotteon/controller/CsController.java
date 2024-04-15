package kr.co.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CsController {

    // cs index 페이지 매핑
    @GetMapping(value = {"/cs","/cs/index"})
    public String cs(){
        return "/cs/index";
    }
    // QnA list 페이지 매핑
    @GetMapping("/cs/qna/list")
    public String qnaList(){
        return "/cs/qna/list";
    }
    // QnA 보기 페이지 매핑
    @GetMapping("/cs/qna/view")
    public String qnaView(){
        return "/cs/qna/view";
    }
    // QnA 쓰기 페이지 매핑
    @GetMapping("/cs/qna/write")
    public String qnaWrite(){
        return "/cs/qna/write";
    }
    // 공지사항 list 페이지 매핑
    @GetMapping("/cs/notice/list")
    public String noticeList(){
        return "/cs/notice/list";
    }
    // 공지사항 보기 페이지 매핑
    @GetMapping("/cs/qna/view")
    public String noticeView(){
        return "/cs/qna/view";
    }
    // FAQ list 페이지 매핑
    @GetMapping("/cs/faq/list")
    public String faqList(){
        return "/cs/faq/list";
    }
    // FAQ 보기 페이지 매핑
    @GetMapping("/cs/faq/view")
    public String faqView(){
        return "/cs/faq/view";
    }
}
