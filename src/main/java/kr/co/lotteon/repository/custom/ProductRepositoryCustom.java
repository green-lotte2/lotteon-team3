package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.dto.product.PageRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Option;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductRepositoryCustom {

    // 관리자 상품 목록 조회
    public Page<Product> adminSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable);

    public Page<Product> adminSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable);

    // 판매자 상품 목록 조회 (where seller = 본인)
    public Page<Product> sellerSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable, String sellerId);
    public Page<Product> sellerSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable, String sellerId);

    // 상품 목록 기본 리스트
    public Page<Product> productList(PageRequestDTO pageRequestDTO, Pageable pageable);


    // ========== 메인페이지 상품리스트 ==========
    
    // 베스트 상품
    public List<ProductDTO> bestProductMain();
    
    // 베스트 상품
    public List<ProductDTO> recentProductMain();
    
    // 할인상품
    public List<ProductDTO> discountProductMain();
    
    // 히트상품
    public List<ProductDTO> hitProductMain();

    // 추천상품
    public List<ProductDTO> recommendProductMain();
// ===========================================
}
