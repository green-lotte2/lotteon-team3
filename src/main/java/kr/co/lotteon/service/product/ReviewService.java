package kr.co.lotteon.service.product;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.product.OptionDTO;
import kr.co.lotteon.dto.product.ProductReviewPageRequestDTO;
import kr.co.lotteon.dto.product.ProductReviewPageResponseDTO;
import kr.co.lotteon.dto.product.ReviewDTO;
import kr.co.lotteon.entity.product.Review;
import kr.co.lotteon.repository.product.OptionRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.repository.product.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ModelMapper modelMapper;

    // 상품 리뷰 조회
    public ProductReviewPageResponseDTO selectProductReview(int prodNo, ProductReviewPageRequestDTO productReviewPageRequestDTO) {

        log.info("상품 리뷰 목록 조회 1" + productReviewPageRequestDTO);
        Pageable pageable = productReviewPageRequestDTO.getPageable("no");
        Page<Tuple> tuples = productRepository.selectProductReview(prodNo, productReviewPageRequestDTO, pageable);
        log.info("상품 리뷰 목록 조회 2" + tuples.getContent());

        List<ReviewDTO> reviewDTOS = tuples.getContent().stream()
                .map(tuple -> {
                    Review review=tuple.get(0,Review.class);
                    String prodName=tuple.get(1,String.class);
                    String nick=tuple.get(2,String.class);
                    String strOpNos = tuple.get(3,String.class);

                    ReviewDTO reviewDTO = modelMapper.map(review,ReviewDTO.class);
                    reviewDTO.setProdName(prodName);
                    reviewDTO.setNick(nick);

                    // option 조회
                    if(strOpNos != null && !strOpNos.equals("")) {

                        // String -> List<Integer>
                        List<Integer> opNos = Arrays.stream(strOpNos.split(","))
                                .map(Integer::parseInt)
                                .collect(Collectors.toList());

                        // optionList 조회
                        List<OptionDTO> options = optionRepository.selectOptionByOpNos(opNos)
                                .stream()
                                .map(entity -> modelMapper.map(entity, OptionDTO.class))
                                .toList();
                        log.info("options : "+ options);

                        reviewDTO.setOptionList(options);
                    }

                    return reviewDTO;
                })
                .toList();
        log.info("상품 리뷰 목록 조회 3" + reviewDTOS);

        int total = (int) tuples.getTotalElements();

        return ProductReviewPageResponseDTO.builder()
                .productReviewPageRequestDTO(productReviewPageRequestDTO)
                .dtoList(reviewDTOS)
                .total(total)
                .build();
    }
    // 상품 리뷰 평균 조회
    public double selectReviewAvg(int prodNo){
        return reviewRepository.selectReviewAvg(prodNo);
    }

    // 구매확정 전 리뷰 조회
    public int selectReviewCountByUid(String uid,int ordNo,int prodNo){
        return reviewRepository.selectReviewCountByUid(uid,ordNo,prodNo);
    }
}
