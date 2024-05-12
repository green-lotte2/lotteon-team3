package kr.co.lotteon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.admin.*;
import kr.co.lotteon.dto.cs.BoardCateDTO;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.CommentDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.cs.Comment;
import kr.co.lotteon.security.MyUserDetails;
import kr.co.lotteon.service.admin.cs.AdminCommentService;
import kr.co.lotteon.service.admin.SellerService;
import kr.co.lotteon.service.admin.product.AdminCateService;
import kr.co.lotteon.service.admin.product.AdminProductService;
import kr.co.lotteon.service.product.ProductService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SellerController {

    private final SellerService sellerService;
    private final AdminCommentService adminCommentService;
    private final ProductService productService;
    private final AdminProductService adminProductService;
    private final AdminCateService adminCateService;

    private final ObjectMapper objectMapper;

    ////////////////  index  ///////////////////////////////////////////////////
    // admin index 페이지 매핑 + seller index 페이지 매핑 (return에 if하면 새로고침...?)
    @GetMapping(value = {"/seller","/seller/index"})
    public String admin(Model model){
        // 공지사항 조회
        List<BoardDTO> noticeList = sellerService.adminSelectNotices();
        // 고객문의 조회
        List<BoardDTO> qnaList = sellerService.adminSelectQnas();


        LocalDateTime period1 = LocalDateTime.now().minusMonths(12);
        LocalDateTime period2 = LocalDateTime.now().minusMonths(1);
        LocalDateTime period3 = LocalDateTime.now().minusWeeks(1);
        OrderCardDTO dto1 = sellerService.selectCountSumByPeriod(period1);
        OrderCardDTO dto2 = sellerService.selectCountSumByPeriod(period2);
        OrderCardDTO dto3 = sellerService.selectCountSumByPeriod(period3);
        Map<String, Long> ordStatus = sellerService.findOrdStatus();
        log.info("ordStatus : " + ordStatus);
        model.addAttribute("deli", ordStatus);
        model.addAttribute("year", dto1);
        model.addAttribute("month", dto2);
        model.addAttribute("week", dto3);

        model.addAttribute("noticeList", noticeList);
        model.addAttribute("qnaList", qnaList);
        return "/seller/index";
    }

    ////////////////  product  ///////////////////////////////////////////////////
    // product list (판매자 상품 목록) 페이지 매핑
    @GetMapping("/seller/product/list")
    public String prodList(Model model, AdminProductPageRequestDTO adminProductPageRequestDTO){
        log.info("판매자 상품 목록 Cont 1 : " + adminProductPageRequestDTO);

        AdminProductPageResponseDTO adminPageResponseDTO = null;
        if(adminProductPageRequestDTO.getKeyword() == null) {
            // 일반 상품 목록 조회
            adminPageResponseDTO = adminProductService.sellerSelectProducts(adminProductPageRequestDTO);
        }else {
            // 검색 상품 목록 조회
            log.info("키워드 검색 Cont" + adminProductPageRequestDTO.getKeyword());
            adminPageResponseDTO = adminProductService.sellerSearchProducts(adminProductPageRequestDTO);
        }
        log.info("판매자 상품 목록 Cont 2 : " + adminPageResponseDTO);
        model.addAttribute("adminPageResponseDTO", adminPageResponseDTO);
        return "/seller/product/list";
    }
    // product register (판매자 상품 등록) 페이지 매핑
    @GetMapping("/seller/product/register")
    public String register(Model model){
        // Cate1 전체 조회
        List<Cate1DTO> cate1List = adminCateService.findAllCate1();
        log.info("판매자 상품 등록 Cont : "+cate1List);
        model.addAttribute("cate1List", cate1List);
        return "/seller/product/register";
    }
    // product modify (판매자 상품 수정) 페이지 매핑 : 상품 코드로 조회
    @GetMapping("/seller/product/modify")
    public String modify(Model model, int prodNo){

        // 상품 상세 조회
        ProductDTO product = productService.selectByprodNo(prodNo);
        log.info("판매자 상품 수정 Cont 1 : "+product);

        // Cate1 전체 조회
        List<Cate1DTO> cate1List = adminCateService.findAllCate1();
        log.info("판매자 상품 수정 Cont 2 : "+cate1List);

        // Cate2 조회
        List<Cate2DTO> cate2List = (List<Cate2DTO>) adminCateService.findAllCate2ByCate1(product.getCate1()).getBody();
        log.info("판매자 상품 수정 Cont 3 : "+cate2List);

        // Cate3 조회
        List<Cate3DTO> cate3List = (List<Cate3DTO>) adminCateService.findAllCate3ByCate2(product.getCate2()).getBody();
        log.info("판매자 상품 수정 Cont 4 : "+cate3List);

        // optionList 조회
        Map<String, List<Map<String, String>>> optionMap = adminProductService.selectProdOption(prodNo);
        log.info("optionList Map : "+optionMap);

        model.addAttribute("product", product);
        model.addAttribute("cate1List", cate1List);
        model.addAttribute("cate2List", cate2List);
        model.addAttribute("cate3List", cate3List);
        model.addAttribute("optionMap", optionMap);
        return "/seller/product/modify";
    }

    // 판매자 상품 목록 검색 - cate1을 type으로 선택 시 cate1 조회
    @GetMapping("/seller/findCate1")
    @ResponseBody
    public ResponseEntity<?> findCate1s(){
        return adminProductService.findCate1s();
    }

    // 판매자 상품 등록 - cate1 선택 시 cate2 조회
    @GetMapping("/seller/product/register/{cate1}")
    @ResponseBody
    public ResponseEntity<?> registerCate2(@PathVariable int cate1){
        return adminCateService.findAllCate2ByCate1(cate1);
    }
    // 판매자 상품 등록 - cate2 선택 시 cate3 조회
    @GetMapping("/seller/product/cate3/{cate2}")
    @ResponseBody
    public ResponseEntity<?> registerCate3(@PathVariable int cate2){
        return adminCateService.findAllCate3ByCate2(cate2);
    }
    // 판매자 상품 등록 - DB insert
    @RequestMapping(value = "/seller/product/register", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String registerProduct(HttpServletRequest httpServletRequest,
                                  ProductDTO productDTO,
                                  @RequestParam("thumb190") MultipartFile thumb190,
                                  @RequestParam("thumb230") MultipartFile thumb230,
                                  @RequestParam("thumb456") MultipartFile thumb456,
                                  @RequestParam("detail860") MultipartFile detail860){
        productDTO.setIp(httpServletRequest.getRemoteAddr());
        log.info("판매자 상품 등록 Cont 1 " + productDTO);


        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String sellerId = userDetails.getMember().getNick();
        productDTO.setSeller(sellerId);
        log.info("판매자 상품 등록 Cont 2 sellerId : " + sellerId);

        log.info("판매자 상품 등록 Cont 3 " + productDTO);

        ProductDTO saveProd = adminProductService.insertProduct(productDTO, thumb190, thumb230, thumb456, detail860);
        int prodNo = saveProd.getProdNo();

        return "redirect:/seller/product/view?prodNo="+prodNo;
    }
    // 판매자 상품 수정 - DB insert
    @RequestMapping(value = "/seller/product/modify", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public String modifyProduct(ProductDTO productDTO,
                                @RequestParam("opNoList") String opNoList,
                                @RequestParam("thumb190") MultipartFile thumb190,
                                @RequestParam("thumb230") MultipartFile thumb230,
                                @RequestParam("thumb456") MultipartFile thumb456,
                                @RequestParam("detail860") MultipartFile detail860){
        log.info("판매자 상품 수정 Cont 1 " + productDTO);

        log.info("판매자 상품 수정 Cont 2 opNoList " + opNoList);

        // 수정 정보 저장
        adminProductService.modifyProduct(productDTO, thumb190, thumb230, thumb456, detail860);

        // 옵션 삭제
        adminProductService.deleteOptions(opNoList);
        return "redirect:/seller/product/list";
    }

    // 판매자 상품 삭제
    // 등록된 상품 보기
    @GetMapping("/seller/product/view")
    public String prodView(Model model, @RequestParam("prodNo") int prodNo){
        ProductDTO productDTO = adminProductService.prodView(prodNo);
        model.addAttribute("productDTO", productDTO);
        return "/seller/product/view";
    }

    // 등록된 상품 커스텀 옵션 추가
    @RequestMapping(value = "/seller/option", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResponseEntity<?> optionAdd(@RequestBody List<OptionDTO> optionDTOS) {
        log.info("상품 커스텀 옵션 추가 Cont 1 : " + optionDTOS);
        return adminProductService.optionAdd(optionDTOS);
    }
    ////////////////  order  /////////////////////////////////////////////////
    // 주문 현황 페이지 매핑
    @GetMapping("/seller/order/list")
    public String orderList(Model model, AdminPageRequestDTO adminPageRequestDTO){
        SellerOrderPageResponseDTO sellerOrderPageResponseDTO = null;
        if(adminPageRequestDTO.getKeyword() == null) {
            // 일반 주문 목록 조회
            sellerOrderPageResponseDTO = sellerService.selectOrderList(adminPageRequestDTO);
        }else {
            // 검색 주문 목록 조회 //////
            log.info("키워드 검색 Cont" + adminPageRequestDTO.getKeyword());
            sellerOrderPageResponseDTO = sellerService.searchOrderList(adminPageRequestDTO);
        }
        model.addAttribute("pageResponseDTO", sellerOrderPageResponseDTO);
        return "/seller/order/list";
    }
    //주문수 그래프 조회
    @GetMapping("/seller/orderChart")
    public ResponseEntity<?> orderChart() {
        List<Map<String, Object>> jsonResult = sellerService.selectOrderForSeller();
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
    // 주문 상태 변경
    @GetMapping("/seller/ordStat/{ordItemno}/{ordStatus}")
    @ResponseBody
    public ResponseEntity<?> orderStatus(@PathVariable("ordItemno") int ordItemno, @PathVariable("ordStatus") String ordStatus){
        log.info("주문 상태 변경 Cont 1: " + ordItemno);
        log.info("주문 상태 변경 Cont 2: " + ordStatus);
        return sellerService.modifyOrdStatus(ordItemno ,ordStatus);
    }
    // 판매자 매출 현황 매핑
    @GetMapping("/seller/order/sales")
    public String orderSales(){

        return "/seller/order/sales";
    }
    // 판매자 매출 현황 차트 조회
    @GetMapping("/seller/sales")
    public ResponseEntity<?> selectSalesChart(){
        List<Map<String, Object>> jsonResult = sellerService.selectSalesChart();
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
    ////////////////  cs  ///////////////////////////////////////////////////
    // 판매자 게시판 목록 페이지 매핑
    @GetMapping("/seller/cs/list")
    public String boardList(Model model, String cate, AdminBoardPageRequestDTO adminBoardPageRequestDTO) {
        String group = adminBoardPageRequestDTO.getGroup();

        log.info("판매자 게시판 목록 Cont 1 : " + cate);
        // 게시글 조회
        AdminBoardPageResponseDTO adminBoardPageResponseDTO = sellerService.findBoardByGroup(cate, adminBoardPageRequestDTO);
        log.info("판매자 게시판 목록 Cont 2 : " +adminBoardPageResponseDTO);

        // 검색용 cate 조회
        List<BoardCateDTO> cates = sellerService.findBoardCate();

        model.addAttribute(adminBoardPageResponseDTO);
        model.addAttribute("group", group);
        model.addAttribute("cates", cates);
        return "/seller/cs/list";
    }
    // 판매자 게시글 Type(말머리) 조회
    @GetMapping("/seller/cs/type/{cate}")
    @ResponseBody
    public ResponseEntity<?> findTypes(@PathVariable("cate") String cate) {
        log.info("판매자 게시글 Type 조회 1 : " + cate);
        return sellerService.findBoardType(cate);
    }
    // 판매자 게시판 보기 페이지 매핑
    @GetMapping("/seller/cs/view")
    public String boardView(Model model, String group, int bno, AdminBoardPageRequestDTO adminBoardPageRequestDTO){
        log.info("판매자 게시판 보기 Cont 1 : " + adminBoardPageRequestDTO);

        // 글 내용 조회
        BoardDTO board = sellerService.selectBoard(bno);
        // 답변 조회
        List<CommentDTO> comments = adminCommentService.commentList(bno);

        log.info("판매자 게시판 보기 Cont 2 : " + board);

        // pg, type, keyword 값
        AdminBoardPageResponseDTO adminBoardPageResponseDTO = AdminBoardPageResponseDTO.builder()
                .adminBoardPageRequestDTO(adminBoardPageRequestDTO)
                .build();
        log.info("판매자 게시판 보기 Cont 3 : " + adminBoardPageResponseDTO);

        model.addAttribute("group",group );
        model.addAttribute("board",board );
        model.addAttribute("comments",comments );
        model.addAttribute("adminBoardPageResponseDTO", adminBoardPageResponseDTO);
        return  "/seller/cs/view";
    }
    // 판매자 글 보기 답변 등록
    @PostMapping("/seller/comment")
    public ResponseEntity<Comment> commentWrite(@RequestBody CommentDTO commentDTO) {
        log.info("commentWrite : " + commentDTO);

        ResponseEntity<Comment> commentResponseEntity = adminCommentService.insertComment(commentDTO);
        log.info("commentWrite ...2 : ");
        log.info(commentResponseEntity.getBody().toString());
        return commentResponseEntity;
    }

    // 판매자 글 보기 답변 삭제
    @DeleteMapping("/seller/comment/{cno}")
    public ResponseEntity<?> deleteComment(@PathVariable("cno") int cno){
        return adminCommentService.deleteComment(cno);
    }
    // 판매자 글 보기 답변 수정
    @PutMapping("/seller/comment")
    public ResponseEntity<?> modifyComment(@RequestBody CommentDTO commentDTO){
        log.info("modifyComment : " +commentDTO.toString());
        return adminCommentService.updateComment(commentDTO);
    }
}
