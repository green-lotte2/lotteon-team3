package kr.co.lotteon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.admin.*;
import kr.co.lotteon.dto.cs.BoardCateDTO;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.dto.cs.CommentDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.cs.Comment;
import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.security.MyUserDetails;
import kr.co.lotteon.service.admin.AdminService;
import kr.co.lotteon.service.cs.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;
    private final CommentService commentService;
    private final ObjectMapper objectMapper;


    // admin index 페이지 매핑 + seller index 페이지 매핑 (return에 if하면 새로고침...?)
    @GetMapping(value = {"/admin","/admin/index"})
    public String admin(Model model){
        // 공지사항 조회
        List<BoardDTO> noticeList = adminService.adminSelectNotices();
        // 고객문의 조회
        List<BoardDTO> qnaList = adminService.adminSelectQnas();
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("qnaList", qnaList);
        return "/admin/index";
    }
    // admin index 페이지 주문수 그래프 조회
    @GetMapping("/admin/orderChart")
    public ResponseEntity<?> orderChart() {
        List<Map<String, Object>> jsonResult = adminService.selectOrderForChart();
        log.info("페이지 그래프 조회 Cont 1: " + jsonResult);
        try {
            // 객체를 JSON으로 변환
            String json = objectMapper.writeValueAsString(jsonResult);
            // JSON 문자열을 ResponseEntity로 반환
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            // JSON 변환에 실패한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON 변환 오류");
        }
    }

    // admin index 페이지 가입자 그래프 조회
    @GetMapping("/admin/memberChart")
    public ResponseEntity<?> memberChart() {
        List<Map<String, Object>> jsonResult = adminService.selectMemberForChart();
        log.info("가입자 그래프 조회 Cont 1: " + jsonResult);
        try {
            // 객체를 JSON으로 변환
            String json = objectMapper.writeValueAsString(jsonResult);
            // JSON 문자열을 ResponseEntity로 반환
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            // JSON 변환에 실패한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON 변환 오류");
        }
    }

    // config banner (관리자 배너 관리) 페이지 매핑
    @GetMapping("/admin/config/banner")
    public String banner(Model model){
        // 배너 cate 별 조회 ㅎㅎ;
        List<BannerDTO> mtBanner = adminService.bannerList("main-top");
        List<BannerDTO> msBanner = adminService.bannerList("main-slider");
        List<BannerDTO> pBanner = adminService.bannerList("product");
        List<BannerDTO> lBanner = adminService.bannerList("login");
        List<BannerDTO> myBanner = adminService.bannerList("myPage");
        model.addAttribute("mtBanner", mtBanner);
        model.addAttribute("msBanner", msBanner);
        model.addAttribute("pBanner", pBanner);
        model.addAttribute("lBanner", lBanner);
        model.addAttribute("myBanner", myBanner);
        return "/admin/config/banner";
    }
    // config register (관리자 배너 등록) 전송
    @PostMapping("/admin/config/register")
    public String bannerRegister(@RequestParam("imgFile") MultipartFile imgFile, BannerDTO bannerDTO){
        log.info("관리자 배너 등록 Cont 1 : " + imgFile);
        log.info("관리자 배너 등록 Cont 2 : " + bannerDTO);
        adminService.bannerRegister(imgFile, bannerDTO);
        return "redirect:/admin/config/banner";
    }

    // 배너 삭제
    @ResponseBody
    @PostMapping("/admin/banner/delete")
    public ResponseEntity<?> bannerDelete(@RequestBody Map<String, int[]> requestData){
        int[] bnoArray = requestData.get("bnoArray");
        log.info("배너 삭제 Cont 1 : " + requestData);
        return adminService.bannerDelete(bnoArray);
    }

    // 배너 활성화 관리
    @GetMapping("/admin/banner/change/{bno}")
    public ResponseEntity<?> bannerActChange(@PathVariable("bno") int bno){
        return adminService.bannerActChange(bno);
    }
    // config info (관리자 기본 환경 정보) 페이지 매핑
    @GetMapping("/admin/config/info")
    public String info(Model model){
        // 관리자 환경설정 기본환경 정보 - 약관 조회
        Terms terms = adminService.findByTerms();
        model.addAttribute("terms", terms);
        return "/admin/config/info";
    }
    // product list (관리자 상품 목록) 페이지 매핑
    @GetMapping("/admin/product/list")
    public String prodList(Model model, AdminProductPageRequestDTO adminProductPageRequestDTO){
        log.info("관리자 상품 목록 Cont 1 : " + adminProductPageRequestDTO);

        AdminProductPageResponseDTO adminPageResponseDTO = null;
        if(adminProductPageRequestDTO.getKeyword() == null) {
            // 일반 상품 목록 조회
            adminPageResponseDTO = adminService.adminSelectProducts(adminProductPageRequestDTO);
        }else {
            // 검색 상품 목록 조회
            log.info("키워드 검색 Cont" + adminProductPageRequestDTO.getKeyword());
            adminPageResponseDTO = adminService.adminSearchProducts(adminProductPageRequestDTO);
        }
        log.info("관리자 상품 목록 Cont 2 : " + adminPageResponseDTO);
        model.addAttribute("adminPageResponseDTO", adminPageResponseDTO);
        return "/admin/product/list";
    }
    // product register (관리자 상품 등록) 페이지 매핑
    @GetMapping("/admin/product/register")
    public String register(Model model){
        // Cate1 전체 조회
        List<Cate1DTO> cate1List = adminService.findAllCate1();
        log.info("관리자 상품 등록 Cont : "+cate1List);
        model.addAttribute("cate1List", cate1List);
        return "/admin/product/register";
    }

    // 관리자 상품 목록 검색 - cate1을 type으로 선택 시 cate1 조회
    @GetMapping("/admin/findCate1")
    @ResponseBody
    public ResponseEntity<?> findCate1s(){
        return adminService.findCate1s();
    }

    // 관리자 상품 등록 - cate1 선택 시 cate2 조회
    @GetMapping("/admin/product/register/{cate1}")
    @ResponseBody
    public ResponseEntity<?> registerCate2(@PathVariable int cate1){
        return adminService.findAllCate2ByCate1(cate1);
    }
    // 관리자 상품 등록 - cate2 선택 시 cate3 조회
    @GetMapping("/admin/product/cate3/{cate2}")
    @ResponseBody
    public ResponseEntity<?> registerCate3(@PathVariable int cate2){
        return adminService.findAllCate3ByCate2(cate2);
    }
    // 관리자 상품 등록 - DB insert
    @RequestMapping(value = "/admin/product/register", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String registerProduct(HttpServletRequest httpServletRequest,
                                  ProductDTO productDTO,
                                  @RequestParam("optionDTOList") String optionDTOListJson,
                                  @RequestParam("thumb190") MultipartFile thumb190,
                                  @RequestParam("thumb230") MultipartFile thumb230,
                                  @RequestParam("thumb456") MultipartFile thumb456,
                                  @RequestParam("detail860") MultipartFile detail860){
        productDTO.setIp(httpServletRequest.getRemoteAddr());
        log.info("관리자 상품 등록 Cont 1 " + optionDTOListJson);


        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MyUserDetails) {
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String sellerId = userDetails.getMember().getName();
            productDTO.setSeller(sellerId);
            log.info("관리자 상품 등록 Cont 1 " + productDTO);
            // 로그인 상태가 아닐 때 == 개발 중 (배포시 삭제 할 것)
        } else if(authentication == null) {
            productDTO.setSeller("developer");
            log.info("관리자 상품 등록 Cont 2 " + productDTO);
        }
        log.info("관리자 상품 등록 Cont " + productDTO);

        ProductDTO saveProd = adminService.insertProduct(optionDTOListJson, productDTO, thumb190, thumb230, thumb456, detail860);
        int prodNo = saveProd.getProdNo();

        return "redirect:/admin/product/view?prodNo="+prodNo;
    }

    // 등록된 상품 보기
    @GetMapping("/admin/product/view")
    public String prodView(Model model, @RequestParam("prodNo") int prodNo){
        ProductDTO productDTO = adminService.prodView(prodNo);
        model.addAttribute("productDTO", productDTO);
        return "/admin/product/view";
    }

    // 등록된 상품 의류 옵션 추가
    @RequestMapping(value = "/admin/option/color/{prodCode}", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResponseEntity<?> colorOptionAdd(@PathVariable("prodCode") int prodCode, @RequestBody List<ColorDTO> colorDTOList) {
        log.info("상품 의류 옵션 추가 Cont 1 : " + prodCode);
        log.info("상품 의류 옵션 추가 Cont 2 : " + colorDTOList);
        return adminService.colorOptionAdd(prodCode, colorDTOList);
    }
    // 등록된 상품 커스텀 옵션 추가
    @RequestMapping(value = "/admin/option", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResponseEntity<?> optionAdd(@RequestBody List<OptionDTO> optionDTOS) {
        log.info("상품 커스텀 옵션 추가 Cont 1 : " + optionDTOS);
        return adminService.optionAdd(optionDTOS);
    }

    // 상품 삭제
    @ResponseBody
    @PostMapping("/admin/product/delete")
    public ResponseEntity prodDelete(@RequestBody Map<String, int[]> requestData){
        int[] prodNoArray = requestData.get("prodNoArray");
        log.info("상품 삭제 Cont 1 : " + requestData);
        return adminService.prodDelete(prodNoArray);
    }
    // 관리자 게시판 목록 페이지 매핑
    @GetMapping("/admin/cs/list")
    public String boardList(Model model, AdminBoardPageRequestDTO adminBoardPageRequestDTO) {

        AdminBoardPageResponseDTO adminBoardPageResponseDTO = adminService.findBoardByGroup(adminBoardPageRequestDTO);
        log.info("관리자 게시판 목록 Cont : " +adminBoardPageResponseDTO);
        model.addAttribute(adminBoardPageResponseDTO);
        model.addAttribute("group", adminBoardPageRequestDTO.getGroup());
        return "/admin/cs/list";
    }
    // 관리자 게시글 쓰기 페이지 매핑
    @GetMapping("/admin/cs/write")
    public String boardDelete(Model model, @RequestParam("group") String group){
        log.info("관리자 게시글 등록 Cont 1 : " + group);
        // cate 조회
        List<BoardCateDTO> cates = adminService.findBoardCate();
        model.addAttribute("group", group);
        model.addAttribute("cates", cates);
        return "/admin/cs/write";
    }
    // 관리자 게시글 Type(말머리) 조회
    @GetMapping("/admin/cs/type/{cate}")
    @ResponseBody
    public ResponseEntity<?> findTypes(@PathVariable("cate") String cate) {
        log.info("관리자 게시글 Type 조회 1 : " + cate);
        return adminService.findBoardType(cate);
    }
    // 관리자 게시글 등록 POST
    @PostMapping("/admin/cs/write")
    public String adminBoardWrite(BoardDTO boardDTO){
        log.info("관리자 게시글 등록 Cont 1 : " +boardDTO);

        adminService.adminBoardWrite(boardDTO);
        return "redirect:/admin/cs/list?group="+boardDTO.getGroup();
    }
    // 관리자 게시글 삭제
    @DeleteMapping("/admin/cs/delete/{bno}")
    public ResponseEntity<?> boardDelete(@PathVariable("bno") int bno){
        log.info("관리자 게시글 삭제 Cont 1 : " + bno);
        return adminService.boardDelete(bno);
    }
    // 관리자 게시판 보기 페이지 매핑
    @GetMapping("/admin/cs/view")
    public String boardView(Model model, String group, int bno, AdminBoardPageRequestDTO adminBoardPageRequestDTO){
        log.info("관리자 게시판 보기 Cont 1 : " + adminBoardPageRequestDTO);
        BoardDTO board = adminService.selectBoard(bno);
        log.info("관리자 게시판 보기 Cont 2 : " + board);
        // pg, type, keyword 값
        AdminBoardPageResponseDTO adminBoardPageResponseDTO = AdminBoardPageResponseDTO.builder()
                .adminBoardPageRequestDTO(adminBoardPageRequestDTO)
                .build();
        log.info("관리자 게시판 보기 Cont 3 : " + adminBoardPageResponseDTO);
        model.addAttribute("group",group );
        model.addAttribute("board",board );
        model.addAttribute("adminBoardPageResponseDTO", adminBoardPageResponseDTO);
        return  "/admin/cs/view";
    }
    // 관리자 게시판 글 수정 페이지 매핑
    @GetMapping("/admin/cs/modify")
    public String boardModify(Model model, AdminBoardPageRequestDTO adminBoardPageRequestDTO, int bno){
        log.info("관리자 게시판 글 수정 Cont 1 : " + adminBoardPageRequestDTO);
        log.info("관리자 게시판 글 수정 Cont 2 : " + bno);

        BoardDTO board = adminService.selectBoard(bno);
        log.info("관리자 게시판 글 수정 Cont 3 : " + board);

        // cate 조회
        List<BoardCateDTO> cates = adminService.findBoardCate();

        // type 조회
        ResponseEntity<?> respType = adminService.findBoardType(board.getCate());
        List<BoardTypeDTO> typeList = (List<BoardTypeDTO>) respType.getBody();

        // pg, type, keyword 값
        AdminBoardPageResponseDTO adminBoardPageResponseDTO = AdminBoardPageResponseDTO.builder()
                .adminBoardPageRequestDTO(adminBoardPageRequestDTO)
                .build();
        log.info("관리자 게시판 글 수정 Cont 4 : " + adminBoardPageResponseDTO);

        model.addAttribute("cates", cates);
        model.addAttribute("group",adminBoardPageRequestDTO.getGroup());
        model.addAttribute("board",board );
        model.addAttribute("typeList",typeList );
        model.addAttribute("adminBoardPageResponseDTO", adminBoardPageResponseDTO);
        return "/admin/cs/modify";
    }
    // 관리자 글 보기 답변 등록
    @PostMapping("/comment")
    public ResponseEntity<Comment> commentWrite(@RequestBody CommentDTO commentDTO) {
        log.info("commentWrite : " + commentDTO);

        ResponseEntity<Comment> commentResponseEntity = commentService.insertComment(commentDTO);
        log.info("commentWrite ...2 : ");
        log.info(commentResponseEntity.getBody().toString());
        return commentResponseEntity;
    }
    // 관리자 글 보기 답변 삭제
    @DeleteMapping("/comment/{cno}")
    public ResponseEntity<?> deleteComment(@PathVariable("cno") int cno){
        return commentService.deleteComment(cno);
    }
    // 관리자 글 보기 답변 수정
    @PutMapping("/comment")
    public ResponseEntity<?> modifyComment(@RequestBody CommentDTO commentDTO){
        log.info("modifyComment : " +commentDTO.toString());
        return commentService.updateComment(commentDTO);
    }
    //// Seller //////////////////////////////////////////////////////////////////////////////////

    // seller index 페이지 매핑
    @GetMapping(value = {"/seller","/seller/index"})
    public String seller(Model model){
        // 공지사항 조회
        List<BoardDTO> noticeList = adminService.adminSelectNotices();
        // 고객문의 조회
        List<BoardDTO> qnaList = adminService.adminSelectQnas();
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("qnaList", qnaList);
        return "/seller/index";
    }
}
