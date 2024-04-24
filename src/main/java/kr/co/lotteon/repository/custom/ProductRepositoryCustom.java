package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.dto.product.PageRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProductRepositoryCustom {

    public Page<Product> adminSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable);

    public Page<Product> adminSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable);

    // 상품 목록 기본 리스트
    public Page<Product> productList(PageRequestDTO pageRequestDTO, Pageable pageable);

    // 뷰페이지 상품 옵션 띄우기
    public Map<String, List<String>> selectProdOption(int prodNo);

    public Product findProductByProdCode(int prodCode);

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
