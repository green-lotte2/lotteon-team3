package kr.co.lotteon.controller;

import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageResponseDTO;

import kr.co.lotteon.service.cs.CsCateService;
import kr.co.lotteon.service.cs.CsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CsController {

    private final CsService csService;

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

    @GetMapping("/cs/notice/list")
    public String noticeList(Model model, CsPageRequestDTO csPageRequestDTO) {
        CsPageResponseDTO csPageResponseDTO = csService.findByCate(csPageRequestDTO);

        model.addAttribute(csPageResponseDTO);
        model.addAttribute("cate", csPageRequestDTO.getCate());
        return "/cs/notice/list";
    }

    // 공지사항 보기 페이지 매핑
    @GetMapping("/cs/notice/view")
    public String noticeView(Model model, int bno, String cate){
        BoardDTO boardDTO = csService.findByBnoForBoard(bno);

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("cate", cate);

        return "/cs/notice/view";
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
