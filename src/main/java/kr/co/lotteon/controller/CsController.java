package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageResponseDTO;

import kr.co.lotteon.entity.cs.BoardCateEntity;
import kr.co.lotteon.service.cs.CsCateService;
import kr.co.lotteon.service.cs.CsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CsController {

    private final CsService csService;
    private final CsCateService csCateService;

    // cs index 페이지 매핑
    @GetMapping(value = {"/cs","/cs/index"})
    public String cs(){
        return "/cs/index";
    }

    @GetMapping(value = "/cs/qna/list")
    public String qnaList(Model model, CsPageRequestDTO csPageRequestDTO) {
        CsPageResponseDTO csPageResponseDTO = csService.findByCate(csPageRequestDTO);

        model.addAttribute(csPageResponseDTO);
        model.addAttribute("cate", csPageRequestDTO.getCate());
        return "/cs/qna/list";
    }

    // QnA 보기 페이지 매핑
    // 공지사항 보기 페이지 매핑
    @GetMapping("/cs/qna/view")
    public String qnaView(Model model, int bno, String cate){
        BoardDTO boardDTO = csService.findByBnoForBoard(bno);

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("cate", cate);

        return "/cs/qna/view";
    }
    // QnA 쓰기 페이지 매핑
    @GetMapping("/cs/qna/write")
    public String qnaWrite(HttpServletRequest request, Model model, String cate) {

        List<BoardCateEntity> cates = csCateService.getCate();
        model.addAttribute("cates", cates);

        return "/cs/qna/write";
    }

    // 공지사항 목록 매핑
    @GetMapping("/cs/notice/list")
    public String noticeList(Model model, CsPageRequestDTO csPageRequestDTO) {
        if (csPageRequestDTO.getCate() == null) {
            csPageRequestDTO.setCate("null");
        }
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
    public String faqList(Model model, String cate, String group) {
        List<BoardDTO> dtoList = csService.findByCateForFaq(cate);
        List<BoardTypeDTO> boardTypeDTOs = csCateService.findByCateTypeDTOS(cate);
        for (BoardTypeDTO boardTypeDTO : boardTypeDTOs) {
            List<BoardDTO> boardDTOS = new ArrayList<>();
            int i = 0;
            for (BoardDTO boardDTO : dtoList) {
                if (boardDTO.getTypeNo() == boardTypeDTO.getTypeNo()) {
                    boardDTO.setIndex(i);
                    i++;
                    boardDTOS.add(boardDTO);
                }
            }
            boardTypeDTO.setBoards(boardDTOS);
        }
        log.info("dtoList size : " + dtoList.size());

        model.addAttribute("dtoList", dtoList);
        model.addAttribute("types", boardTypeDTOs);
        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        return "/cs/faq/list";
    }

    // FAQ 보기 페이지 매핑
    @GetMapping("/cs/faq/view")
    public String faqView(){
        return "/cs/faq/view";
    }

}
