package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageResponseDTO;

import kr.co.lotteon.entity.cs.BoardCateEntity;
import kr.co.lotteon.entity.cs.BoardFileEntity;
import kr.co.lotteon.entity.cs.BoardTypeEntity;
import kr.co.lotteon.repository.cs.BoardRepository;
import kr.co.lotteon.service.cs.CsCateService;
import kr.co.lotteon.service.cs.CsService;
import kr.co.lotteon.service.cs.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CsController {

    private final CsService csService;
    private final CsCateService csCateService;
    private final BoardRepository boardRepository;
    private final FileService fileService;

    // cs index
    @GetMapping(value = {"/cs","/cs/index"})
    public String cs(@RequestParam(name = "page", defaultValue = "0") int page,
                     @RequestParam(name = "size", defaultValue = "5") int size,
                     Model model, String cate){

        List<BoardDTO> noticeBoard = csService.getNoticeBoard(page, size);

        List<BoardDTO> qnaBoard = csService.getQnaBoard(page, size);

        model.addAttribute("noticeBoard", noticeBoard);
        model.addAttribute("qnaBoard", qnaBoard);
        model.addAttribute("cate", cate);
        return "/cs/index";
    }

    // QnA list
    @GetMapping(value = "/cs/qna/list")
    public String qnaList(Model model, CsPageRequestDTO csPageRequestDTO, String group) {
        CsPageResponseDTO csPageResponseDTO = csService.findByCate(csPageRequestDTO);

        model.addAttribute(csPageResponseDTO);
        model.addAttribute("cate", csPageRequestDTO.getCate());
        model.addAttribute("group", group);

        return "/cs/qna/list";
    }

    // QnA view
    @GetMapping("/cs/qna/view")
    public String qnaView(Model model, int bno, String cate, String group){
        BoardDTO boardDTO = csService.findByBnoForBoard(bno);

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        return "/cs/qna/view";
    }
    // QnA write(페이지)
    @GetMapping("/cs/qna/write")
    public String qnaWriteForm(HttpServletRequest request, Model model, String group) {
        model.addAttribute("group", group);

        // 1차 분류 선택
        List<BoardCateEntity> cates = csCateService.getCate();
        model.addAttribute("cates", cates);

        // 2차 분류 선택
        List<BoardTypeEntity> types = csCateService.getType();
        model.addAttribute("types", types);

        return "/cs/qna/write";
    }
    @PostMapping("/cs/qna/write")
    public String qnaWrite(HttpServletRequest request, BoardDTO dto, String cate){
        dto.setStatus("검토중");
        csService.save(dto);

        return "redirect:/cs/qna/list?group=qna&cate=" + cate + "&success=200";
    }

    // QnA 글수정(페이지)
    @GetMapping("/cs/qna/modify")
    public String qnaModifyForm(Model model, int bno, String cate, String group){
        BoardDTO boardDTO = csService.findByBnoForBoard(bno);
        model.addAttribute("boardDTO", boardDTO);

        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        return "/cs/qna/modify";
    }

    // QnA 글삭제
    @GetMapping("/cs/qna/delete")
    public String qnaDelete(int bno, String cate, String group) {

        fileService.deleteFiles(bno);
        csService.deleteBoard(bno);

        return "redirect:/cs/qna/list?cate=" + cate + "&group=" + group;
    }


    // 공지사항 list
    @GetMapping("/cs/notice/list")
    public String noticeList(Model model, CsPageRequestDTO csPageRequestDTO, String group) {
        if (csPageRequestDTO.getCate() == null) {
            csPageRequestDTO.setCate("null");
        }
        CsPageResponseDTO csPageResponseDTO = csService.findByCate(csPageRequestDTO);

        model.addAttribute(csPageResponseDTO);
        model.addAttribute("cate", csPageRequestDTO.getCate());
        model.addAttribute("group", group);
        return "/cs/notice/list";
    }

    // 공지사항 view
    @GetMapping("/cs/notice/view")
    public String noticeView(Model model, int bno, String cate, String group){
        BoardDTO boardDTO = csService.findByBnoForBoard(bno);

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        return "/cs/notice/view";
    }

    // FAQ list
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

        model.addAttribute("dtoList", dtoList);
        model.addAttribute("types", boardTypeDTOs);
        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        return "/cs/faq/list";
    }

    // FAQ view
    @GetMapping("/cs/faq/view")
    public String faqView(Model model ,int bno, String group, String cate) {

        BoardDTO boardDTO = csService.findByBnoForBoard(bno);
        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("cate", cate);
        model.addAttribute("group", group);
        return "/cs/faq/view";
    }
}