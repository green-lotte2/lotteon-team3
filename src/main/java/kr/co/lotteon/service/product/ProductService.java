package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.product.PageRequestDTO;
import kr.co.lotteon.dto.product.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    // 상품 목록 조회
    public PageResponseDTO findByCate1AndCate2(PageRequestDTO pageRequestDTO){
        Page<Product> result = productRepository.findByCate1AndCate2(pageRequestDTO.getCate1(),pageRequestDTO.getCate2(), pageRequestDTO.getPageable());
        List<ProductDTO> dtoList = result.getContent()
                                    .stream()
                                    .map(entity -> modelMapper.map(entity, ProductDTO.class))
                                    .toList();

        int totalElements = (int) result.getTotalElements();

        return PageResponseDTO.builder()
                    .pageRequestDTO(pageRequestDTO)
                    .dtoList(dtoList)
                    .total(totalElements)
                    .build();
    }
    
    // 상품 보기
    public ProductDTO selectByprodNo(int prodNo){
        Product product = productRepository.findById(prodNo).get();
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }


}
