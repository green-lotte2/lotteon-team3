package kr.co.lotteon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.lang.Tuple;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.dto.member.CouponDTO;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.entity.product.OrderItem;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.Cate1Repository;
import kr.co.lotteon.repository.product.OptionRepository;
import kr.co.lotteon.security.MyUserDetails;
import kr.co.lotteon.service.admin.BannerService;
import kr.co.lotteon.service.member.MemberService;
import kr.co.lotteon.service.my.MyService;
import kr.co.lotteon.service.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Console;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final BannerService bannerService;
    private final CartService cartService;
    // 상품 카테고리를 불러오기 위한 cateService
    private final CateService cateService;
    private final OptionService optionService;
    private final MemberService memberService;
    private final WishService wishService;
    private final ReviewService reviewService;
    private final OptionRepository optionRepository;

    // cart 페이지 매핑
    @GetMapping("/product/cart")
    public String cart(@RequestParam("uid") String uid, Model model){
        
        // 회사별 출력을 위한 회사만 가져오기
        List<String> companies = cartService.selectCartCompany(uid);
        // 장바구니에 담긴 상품 정보 가져오기
        List<CartInfoDTO> cartProducts = cartService.selectCartProduct(uid);

        // 참조
        model.addAttribute("companies", companies);
        model.addAttribute("cartProducts", cartProducts);
        log.info("companies: {}", companies);
        log.info("cartProducts: {} ", cartProducts );

        return "/product/cart";
    }

    // list (상품 목록) 페이지 매핑
    @GetMapping("/product/list")
    public String list(Model model, PageRequestDTO pageRequestDTO, SearchPageRequestDTO searchPageRequestDTO){

        // 상품 목록 조회
        PageResponseDTO pageResponseDTO = null;
        SearchPageResponseDTO searchPageResponseDTO = null;

        if (searchPageRequestDTO.getSearchKeyword() != null) {
            // 검색 글 목록 조회
            searchPageResponseDTO = productService.searchProducts(searchPageRequestDTO);

        }else {
            pageResponseDTO = productService.productList(pageRequestDTO);
        }

        model.addAttribute("pageResponseDTO", pageResponseDTO);
        log.info("아아아" + pageResponseDTO);
        // 카테고리 불러오기
        String c1Name = cateService.getc1Name(pageRequestDTO.getCate1());
        String c2Name = cateService.getc2Name(pageRequestDTO.getCate1(), pageRequestDTO.getCate2());
        String c3Name = cateService.getc3Name(pageRequestDTO.getCate2(), pageRequestDTO.getCate3());
        log.info("c1Name : " + c1Name);
        log.info("c2Name : " + c2Name);
        log.info("c3Name : " + c3Name);

        // 카테 리스트 가져오기
        List<Cate1DTO> cate1DTOS = cateService.getCate1List();
        List<Cate2DTO> cate2DTOS = cateService.getCate2List();
        List<Cate3DTO> cate3DTOS = cateService.getCate3List();
        log.info("cate1DTOS : " + cate1DTOS);
        log.info("cate2DTOS : " + cate2DTOS);
        log.info("cate3DTOS : " + cate3DTOS);

        // list페이지에 사용하기 위해 참조
        //model.addAttribute(pageResponseDTO);
        model.addAttribute("c1Name",c1Name);
        model.addAttribute("c2Name",c2Name);
        model.addAttribute("c3Name",c3Name);

        // 카테 리스트 참조
        model.addAttribute("cate1DTOS", cate1DTOS);
        model.addAttribute("cate2DTOS", cate2DTOS);
        model.addAttribute("cate3DTOS", cate3DTOS);
        return "/product/list";
    }

    // order 페이지 (cart->order)
    @GetMapping("/order")
    public String order(@RequestParam String uid, Model model, @RequestParam int[] cartNo) throws JsonProcessingException {

        List<ProductDTO> productDTOS = productService.selectOrderFromCart(cartNo);
        log.info("컨트롤러 : "+productDTOS);
        MemberDTO memberDTO =memberService.findByUid(uid);

       /* Map<String, List<ProductDTO>> orderProducts = new HashMap<>();
        for(ProductDTO productDTO : productDTOS){
            String company = productDTO.getCompany();
            List<ProductDTO> companyProducts = orderProducts.getOrDefault(company, new ArrayList<>());

            companyProducts.add(productDTO);

            orderProducts.put(company, companyProducts);
        }*/
        ObjectMapper objectMapper = new ObjectMapper();
        String productDTOSJSON = objectMapper.writeValueAsString(productDTOS);
        model.addAttribute("productDTOSJSON", productDTOSJSON);

        model.addAttribute("productDTOS", productDTOS);
        model.addAttribute("memberDTO", memberDTO);

        //log.info("맵:"+orderProducts);
        
        // 쿠폰 가져오기
        List<CouponDTO> couponDTOS = productService.selectsCouponsNotUse(uid);
        log.info("서비스 : " + couponDTOS);

        model.addAttribute("couponDTOS", couponDTOS);

        return "/product/order";
    }

   @GetMapping("/product/order")
    public String order(@RequestParam String uid, int prodNo, int count, String opNo, Model model) throws JsonProcessingException {

       log.info("컨트롤러1"+uid);
       log.info("컨트롤러2"+prodNo);

       ProductDTO productDTOS = productService.prodToOrder(prodNo);

       // 옵션 뽑아내기
       if(opNo != null){
           String [] opNoString = opNo.split(",");
           int[] optionIds = new int[opNoString.length];

           for(int i=0; i<opNoString.length; i++){
               optionIds[i] = Integer.parseInt(opNoString[i].trim());
           }

           List<OptionDTO> options = new ArrayList<>();
           for (int optionNos : optionIds){
               OptionDTO optionDTO = optionRepository.selectOptionForCart(optionNos);

               if(optionDTO != null){
                   options.add(optionDTO);
               }
               productDTOS.setOptionList(options);
           }
           productDTOS.setOpNo(opNo);
       }

       productDTOS.setCount(count);

       log.info("아아아3" + productDTOS);

       MemberDTO memberDTO =memberService.findByUid(uid);
       model.addAttribute("productDTOS", productDTOS);
       model.addAttribute("memberDTO", memberDTO);

       ObjectMapper objectMapper = new ObjectMapper();
       String productDTOSJSON = objectMapper.writeValueAsString(productDTOS);
       model.addAttribute("productDTOSJSON", productDTOSJSON);

       // 쿠폰 가져오기
       List<CouponDTO> couponDTOS = productService.selectsCouponsNotUse(uid);
       log.info("서비스 : " + couponDTOS);

       model.addAttribute("couponDTOS", couponDTOS);

        return "/product/order";
    }

    @ResponseBody
    @PostMapping("/product/order")
    public ResponseEntity<?> order(@RequestBody OrderDTO orderDTO){
        log.info("오더 서비스 : "+orderDTO);
        return productService.saveOrder(orderDTO);
    }

    @ResponseBody
    @PostMapping("/product/orderItem")
    public int orderItem(@RequestBody List<OrderItemDTO> orderItemDTOS,
                         int ordNo,
                         @AuthenticationPrincipal Object principal) {
        Member member = ((MyUserDetails) principal).getMember();
        String uid = member.getUid();
        ObjectMapper objectMapper = new ObjectMapper();

        log.info("주문번호 : " + ordNo);

        return productService.saveOrderItem(orderItemDTOS, uid, ordNo);
    }

    // complete(주문 완료) 페이지 매핑
    @GetMapping("/product/complete")
    public String complete(int ordNo, @AuthenticationPrincipal Object principal , Model model){

        // 주문정보 가져오기
        OrderDTO orderDTO = productService.selectOrder(ordNo);
        model.addAttribute("orderDTO", orderDTO);

        Member member = ((MyUserDetails) principal).getMember();
        String uid = member.getUid();
        MemberDTO memberDTO =memberService.findByUid(uid);
        log.info("주문 완료1" + memberDTO);

        model.addAttribute("memberDTO", memberDTO);

        List<OrderItemDTO> orderItemDTOS = productService.selectOrderComplete(ordNo);
        model.addAttribute("orderItemDTOS", orderItemDTOS);

        log.info("주문 완료2" + orderItemDTOS);
        return "/product/complete";
    }

    // search (상품 검색) 페이지 매핑
    @GetMapping("/product/search")
    public String search(Model model, SearchPageRequestDTO searchPageRequestDTO) {

        String searchKeyword = searchPageRequestDTO.getSearchKeyword();
        String searchType = searchPageRequestDTO.getSearchType();
        
        log.info("컨트롤러 검색 키워드...1" + searchKeyword);
        log.info("컨트롤러 검색 타입...1" + searchType);

        // 상품가격 조회
        if(!(searchPageRequestDTO.getMin() ==0) || !(searchPageRequestDTO.getMax()==0)){
            SearchPageResponseDTO searchPageResponseDTO = productService.searchProductsPrice(searchPageRequestDTO, searchPageRequestDTO.getMin(), searchPageRequestDTO.getMax());
            model.addAttribute("searchPageResponseDTO", searchPageResponseDTO);
            log.info("가격검색 컨트롤러 : " + searchPageResponseDTO);
        }
        // 상품명 조회
        if (searchKeyword != null && !searchKeyword.isEmpty() && "name".equals(searchType)) {
            // 검색어가 존재하는 경우 상품 검색 실행
            SearchPageResponseDTO searchPageResponseDTO = productService.searchProductsProdName(searchPageRequestDTO);
            model.addAttribute("searchPageResponseDTO", searchPageResponseDTO);
            log.info("가격없음 컨트롤러 : " + searchPageResponseDTO);
        }
        // 상품설명 조회
        if (searchKeyword != null && !searchKeyword.isEmpty() && "descript".equals(searchType)) {
            // 검색어가 존재하는 경우 상품 검색 실행
            SearchPageResponseDTO searchPageResponseDTO = productService.searchProductsDescript(searchPageRequestDTO);
            model.addAttribute("searchPageResponseDTO", searchPageResponseDTO);
            log.info("가격없음 컨트롤러 : " + searchPageResponseDTO);
        }
        // 메인 검색
        if (searchKeyword != null && !searchKeyword.isEmpty() && searchType == null) {
            // 검색어가 존재하는 경우 상품 검색 실행
            SearchPageResponseDTO searchPageResponseDTO = productService.searchProducts(searchPageRequestDTO);
            model.addAttribute("searchPageResponseDTO", searchPageResponseDTO);
            log.info("가격없음 컨트롤러 : " + searchPageResponseDTO);
        }

        // 카테 리스트 가져오기
        List<Cate1DTO> cate1DTOS = cateService.getCate1List();
        List<Cate2DTO> cate2DTOS = cateService.getCate2List();
        List<Cate3DTO> cate3DTOS = cateService.getCate3List();

        // 카테 리스트 참조
        model.addAttribute("cate1DTOS", cate1DTOS);
        model.addAttribute("cate2DTOS", cate2DTOS);
        model.addAttribute("cate3DTOS", cate3DTOS);

        return "/product/search";
    }


    // view (상품 상세 보기) 페이지 매핑
    @GetMapping("/product/view")
    public String view(Model model, ProductDTO productDTO,ProductReviewPageRequestDTO productReviewPageRequestDTO){

        // hit + 1
        productService.updateProductHit(productDTO.getProdNo());
        ProductDTO prod = productService.selectByprodNo(productDTO.getProdNo());
        log.info("productDTO : " + prod.toString());
        // productDTO 참조
        model.addAttribute("productDTO", prod);

        // 카테 가져오기
        String c1Name = cateService.getc1Name(productDTO.getCate1());
        String c2Name = cateService.getc2Name(productDTO.getCate1(), productDTO.getCate2());
        String c3Name = cateService.getc3Name( productDTO.getCate2(), productDTO.getCate3());

        // 카테 참조
        model.addAttribute("c1Name",c1Name);
        model.addAttribute("c2Name",c2Name);
        model.addAttribute("c3Name",c3Name);

        // 카테 리스트 가져오기
        List<Cate1DTO> cate1DTOS = cateService.getCate1List();
        List<Cate2DTO> cate2DTOS = cateService.getCate2List();
        List<Cate3DTO> cate3DTOS = cateService.getCate3List();

        // 상품 상세보기 배너
        List<BannerDTO> prodBanners = bannerService.selectBanners("product");

        // 카테 리스트 참조
        model.addAttribute("cate1DTOS", cate1DTOS);
        model.addAttribute("cate2DTOS", cate2DTOS);
        model.addAttribute("cate3DTOS", cate3DTOS);

        // 배너 리스트 참조
        model.addAttribute("prodBanners", prodBanners);

        // 옵션 가져오기
        Map<String, List<String>> prodOptions = optionService.selectProdOption(productDTO.getProdNo());

        log.info("prodOptions : " + prodOptions);

        // 옵션 맵 참조
        model.addAttribute("prodOptions", prodOptions);

        // 옵션 네임 가져오기
        List<String> opNames = optionService.selectOpName(productDTO.getProdNo());
        log.info("opNames : " + opNames);
        model.addAttribute("opNames", opNames);

        // 리뷰 가져오기
        ProductReviewPageResponseDTO pageResponseDTO = reviewService.selectProductReview(productDTO.getProdNo(), productReviewPageRequestDTO);
        log.info("선택한 상품 : "+productDTO.getProdNo());
        log.info("선택한 상품의 리뷰들 "+pageResponseDTO);
        model.addAttribute("pageResponseDTO",pageResponseDTO);

        // Qna 목록 조회
        ProductQnaPageResponseDTO qnaPageDto = productService.selectQna(productDTO.getProdNo());
        model.addAttribute("qnaPageDto", qnaPageDto);

        // review Avg 가져오기
        double result = reviewService.selectReviewAvg(productDTO.getProdNo());
        double avg = Math.round(result * 10) / 10.0;
        model.addAttribute("avg", avg);
        log.info("avg : "+avg);

        // 시큐리티 컨텍스트에서 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자가 있는지 확인
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            log.info("!!! gggg ");
            // 인증된 사용자가 있으면 찜 여부 가져오기
            int wish = wishService.existsWish(productDTO.getProdNo());
            model.addAttribute("wish", wish);
        } else {
            log.info("로그인해야댄 ");
            model.addAttribute("wish", 0);
        }

        return "/product/view";
    }


    @GetMapping("/product/list2")
    public String productList2(String type, Model model){

        List<ProductDTO> productDTOS = new ArrayList<>();
        if(type.equals("hit")){
            productDTOS = productService.hitProductMain();
        }if(type.equals("recommend")){
            productDTOS = productService.recommendProductMain();
        }if(type.equals("recent")){
            productDTOS = productService.recentProductMain();
        }if(type.equals("best")){
            productDTOS = productService.bestProductMain2();
        }if(type.equals("discount")){
            productDTOS = productService.discountProductMain();
        }

        // 카테 리스트 가져오기
        List<Cate1DTO> cate1DTOS = cateService.getCate1List();
        List<Cate2DTO> cate2DTOS = cateService.getCate2List();
        List<Cate3DTO> cate3DTOS = cateService.getCate3List();

        // 카테 리스트 참조
        model.addAttribute("cate1DTOS", cate1DTOS);
        model.addAttribute("cate2DTOS", cate2DTOS);
        model.addAttribute("cate3DTOS", cate3DTOS);

        model.addAttribute("type", type);
        model.addAttribute("productDTOS", productDTOS);

        return "/product/list2";
    }

}
