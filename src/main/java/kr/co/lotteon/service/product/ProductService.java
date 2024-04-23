package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor @Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    private final SqlSession sqlSession;

    // 기본 상품 목록 조회
    public PageResponseDTO productList(PageRequestDTO pageRequestDTO){
        log.info("기본 상품 목록 조회 1" + pageRequestDTO);

        Pageable pageable = pageRequestDTO.getPageable();

        Page<Product> productsPage = productRepository.productList(pageRequestDTO, pageable);
        log.info("기본 상품 목록 조회 2" + productsPage);

        // Page<Product>를 List<ProductDTO>로 변환
        List<ProductDTO> productDTOS = productsPage.getContent().stream()
                .map(entity-> modelMapper.map(entity, ProductDTO.class))
                .toList();
        log.info("기본 상품 목록 조회 3" + productDTOS);

        int total = (int) productsPage.getTotalElements();

        return PageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(productDTOS)
                .total(total)
                .build();
    }

    // 상품 보기
    public ProductDTO selectByprodNo(int prodNo){
        Product product = productRepository.findById(prodNo).get();
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }

    // 옵션 불러오기
    public Map<String, List<String>> selectProdOption(int prodNo){
        return productRepository.selectProdOption(prodNo);
    }
}
