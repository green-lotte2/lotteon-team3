package kr.co.lotteon.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.cs.*;

import kr.co.lotteon.entity.cs.BoardCateEntity;
import kr.co.lotteon.entity.cs.Comment;
import kr.co.lotteon.repository.cs.BoardRepository;
import kr.co.lotteon.service.admin.NotificationService;
import kr.co.lotteon.service.admin.cs.AdminCommentService;
import kr.co.lotteon.service.cs.CsCateService;
import kr.co.lotteon.service.cs.CsService;
import kr.co.lotteon.service.cs.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final AdminCommentService adminCommentService;
    private final NotificationService notificationService;

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

    /* 문의하기 -------------------------------------------------------------------------------------------------------*/
    // QnA list
    @GetMapping("/cs/qna/list")
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

        // 관리자 답변 조회
        List<CommentDTO> comments = adminCommentService.commentList(bno);

        // 조회수 증가 로직 추가
        boardDTO.setHit(boardDTO.getHit() + 1);
        csService.updateHit(boardDTO);

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        model.addAttribute("comments",comments );

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
        List<BoardTypeDTO> types = csCateService.getType();
        model.addAttribute("types", types);
        log.info("types : " + types);
        return "/cs/qna/write";
    }

    // QnA write(기능)
    @PostMapping("/cs/qna/write")
    public String qnaWrite(HttpServletRequest request, BoardDTO dto, String cate){
        dto.setStatus("검토중");
        csService.save(dto);

        return "redirect:/cs/qna/list?group=qna&cate=" + cate + "&success=200";
    }

    // QnA modify(페이지)
    @GetMapping("/cs/qna/modify")
    public String qnaModifyForm(Model model, int bno, String cate, String group){
        BoardDTO boardDTO = csService.findByBnoForBoard(bno);
        model.addAttribute("boardDTO", boardDTO);

        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        // 2차 분류 선택
        List<BoardTypeDTO> types = csCateService.getType();
        model.addAttribute("types", types);

        return "/cs/qna/modify";
    }

    // QnA modify(기능)
    @PostMapping("/cs/qna/modify")
    public String qnaModify(BoardDTO dto) {

        csService.modifyBoard(dto);
        log.info("csContoller" + dto.toString());
        return "redirect:/cs/qna/view?group=qna&cate="+dto.getCate()+"&bno="+dto.getBno()+"&success=200";
    }

    // QnA delete
    @GetMapping("/cs/qna/delete")
    public String qnaDelete(int bno, String cate, String group) {

        fileService.deleteFiles(bno);
        csService.deleteBoard(bno);

        return "redirect:/cs/qna/list?cate=" + cate + "&group=" + group;
    }

    // QnA 댓글입력
    @PostMapping("/cs/qna/insertComment")
    public ResponseEntity<Comment> qnaCommentWrite(@RequestBody CommentDTO commentDTO) {

        ResponseEntity<Comment> commentResponseEntity = csService.insertComment(commentDTO);
        log.info(commentResponseEntity.getBody().toString());
        return commentResponseEntity;
    }

    // QnA 댓글수정
    @PutMapping("/cs/qna/modifyComment")
    public ResponseEntity<?> qnaModifyComment(@RequestBody CommentDTO commentDTO){
        log.info("modifyComment : " +commentDTO.toString());
        return csService.updateComment(commentDTO);
    }

    // QnA 댓글삭제
    @ResponseBody
    @DeleteMapping("/cs/qna/deleteComment/{cno}") // 기본키인 cno를 매개변수로 받도록 수정
    public ResponseEntity<?> qnaDeleteComment(@PathVariable("cno") int cno) {
        log.info("컨트롤러 cno : " + cno);
        return csService.deleteComment(cno);
    }

    /* 공지사항 -------------------------------------------------------------------------------------------------------*/
    // 공지사항 list
    @GetMapping("/cs/notice/list")
    public String noticeList(Model model, CsPageRequestDTO csPageRequestDTO, String group) {
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

        // 관리자 답변 조회
        List<CommentDTO> comments = adminCommentService.commentList(bno);

        // 조회수 증가 로직 추가
        boardDTO.setHit(boardDTO.getHit() + 1);
        csService.updateHit(boardDTO);

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        model.addAttribute("comments",comments );

        return "/cs/notice/view";
    }

    // QnA 댓글입력
    @PostMapping("/cs/notice/insertComment")
    public ResponseEntity<Comment> noticeCommentWrite(@RequestBody CommentDTO commentDTO) {

        ResponseEntity<Comment> commentResponseEntity = csService.insertComment(commentDTO);
        log.info(commentResponseEntity.getBody().toString());
        return commentResponseEntity;
    }

    // QnA 댓글수정
    @PutMapping("/cs/notice/modifyComment")
    public ResponseEntity<?> noticeModifyComment(@RequestBody CommentDTO commentDTO){
        log.info("modifyComment : " +commentDTO.toString());
        return csService.updateComment(commentDTO);
    }

    // QnA 댓글삭제
    @ResponseBody
    @DeleteMapping("/cs/notice/deleteComment/{cno}") // 기본키인 cno를 매개변수로 받도록 수정
    public ResponseEntity<?> noticeDeleteComment(@PathVariable("cno") int cno) {
        log.info("컨트롤러 cno : " + cno);
        return csService.deleteComment(cno);
    }

    /* 자주묻는 질문 ---------------------------------------------------------------------------------------------------*/
    // FAQ list
    @GetMapping("/cs/faq/list")
    public String faqList(Model model, String cate, String group) {
        log.info("컨트롤러 group : " + group);
        List<BoardDTO> dtoList = csService.findByCateForFaq(cate, group);
        log.info("컨트롤러 dtoList : " + dtoList);

        List<BoardTypeDTO> boardTypeDTOs = csCateService.findByCateTypeDTOS(cate);
        log.info("컨트롤러 boardTypeDTOs : " + boardTypeDTOs);
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
    public String faqView(Model model, int bno, String cate, String group){
        BoardDTO boardDTO = csService.findByBnoForBoard(bno);

        // 관리자 답변 조회
        List<CommentDTO> comments = adminCommentService.commentList(bno);

        // 조회수 증가 로직 추가
        boardDTO.setHit(boardDTO.getHit() + 1);
        csService.updateHit(boardDTO);

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("cate", cate);
        model.addAttribute("group", group);

        model.addAttribute("comments",comments );

        return "/cs/faq/view";
    }

    // QnA 댓글입력
    @PostMapping("/cs/faq/insertComment")
    public ResponseEntity<Comment> faqCommentWrite(@RequestBody CommentDTO commentDTO) {

        ResponseEntity<Comment> commentResponseEntity = csService.insertComment(commentDTO);
        log.info(commentResponseEntity.getBody().toString());
        return commentResponseEntity;
    }

    // QnA 댓글수정
    @PutMapping("/cs/faq/modifyComment")
    public ResponseEntity<?> faqModifyComment(@RequestBody CommentDTO commentDTO){
        log.info("modifyComment : " +commentDTO.toString());
        return csService.updateComment(commentDTO);
    }

    // QnA 댓글삭제
    @ResponseBody
    @DeleteMapping("/cs/faq/deleteComment/{cno}") // 기본키인 cno를 매개변수로 받도록 수정
    public ResponseEntity<?> faqDeleteComment(@PathVariable("cno") int cno) {
        log.info("컨트롤러 cno : " + cno);
        return csService.deleteComment(cno);
    }

    // 상품 문의
    @ResponseBody
    @PostMapping("/cs/prodqna")
    public ResponseEntity<?> prodQna(@RequestBody BoardDTO boardDTO){
        log.info("상품 문의");
        return notificationService.prodQnaSave(boardDTO);
    }


}