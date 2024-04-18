package kr.co.lotteon.controller;

import kr.co.lotteon.dto.product.PageRequestDTO;
import kr.co.lotteon.dto.product.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.repository.product.Cate1Repository;
import kr.co.lotteon.service.product.CateService;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    // 상품 카테고리를 불러오기 위한 cateService
    private final CateService cateService;

    // cart 페이지 매핑
    @GetMapping("/product/cart")
    public String cart(){
        return "/product/cart";
    }
    // complete(주문 완료) 페이지 매핑
    @GetMapping("/product/complete")
    public String complete(){
        return "/product/complete";
    }
    // list (상품 목록) 페이지 매핑
    @GetMapping("/product/list")
    public String list(Model model, PageRequestDTO pageRequestDTO){

        // 상품 목록 조회
        PageResponseDTO pageResponseDTO = productService.findByCate1AndCate2(pageRequestDTO);
        log.info("pageResponseDTO : " + pageResponseDTO.toString());

        // 카테고리 불러오기
        String c1Name = cateService.getc1Name(pageRequestDTO.getCate1());
        String c2Name = cateService.getc2Name(pageRequestDTO.getCate1(), pageRequestDTO.getCate2());
        log.info("c1Name : " + c1Name);
        log.info("c2Name : " + c2Name);

        // list페이지에 사용하기 위해 참조
        model.addAttribute(pageResponseDTO);
        model.addAttribute("c1Name",c1Name);
        model.addAttribute("c2Name",c2Name);

        return "/product/list";
    }

    // order 페이지 매핑
    @GetMapping("/product/order")
    public String order(){
        return "/product/order";
    }

    // search (상품 검색) 페이지 매핑
    @GetMapping("/product/search")
    public String search(){
        return "/product/search";
    }

    // view (상품 상세 보기) 페이지 매핑
    @GetMapping("/product/view")
    public String view(Model model, ProductDTO productDTO){

        ProductDTO prod = productService.selectByprodNo(productDTO.getProdNo());
        log.info("productDTO : " + prod.toString());
        // 카테 가져오기
        String c1Name = cateService.getc1Name(productDTO.getCate1());
        String c2Name = cateService.getc2Name(productDTO.getCate1(), productDTO.getCate2());
        
        // 카테 참조
        model.addAttribute("c1Name",c1Name);
        model.addAttribute("c2Name",c2Name);
        
        // productDTO 참조
        model.addAttribute("productDTO", prod);

        return "/product/view";
    }
}
