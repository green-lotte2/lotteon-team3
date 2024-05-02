package kr.co.lotteon.service.my;

import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageResponseDTO;
import kr.co.lotteon.dto.member.CouponDTO;

import kr.co.lotteon.dto.member.point.PointPageRequestDTO;
import kr.co.lotteon.dto.member.point.PointPageResponseDTO;
import kr.co.lotteon.dto.product.*;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.member.Coupon;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.member.Point;

import kr.co.lotteon.entity.product.Review;
import kr.co.lotteon.repository.cs.BoardRepository;
import kr.co.lotteon.repository.member.MemberRepository;
import kr.co.lotteon.repository.my.CouponRepository;
import kr.co.lotteon.repository.my.PointRepository;
import kr.co.lotteon.repository.product.OrderItemRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final OrderItemRepository orderItemRepository;
    private final BoardRepository boardRepository;
    private final PointRepository pointRepository;
    private final ProductRepository productRepository;

    public List<CouponDTO> findCouponsByUid(String uid){
        log.info("내 쿠폰"+couponRepository.findCouponsByUid(uid));
        List<Coupon> result=couponRepository.findCouponsByUid(uid);
        List<CouponDTO> couponDTOS=result.stream().map(coupons->modelMapper.map(coupons,CouponDTO.class))
                .collect(Collectors.toList());
        for (CouponDTO couponDTO : couponDTOS) {
            couponDTO.changeUseYnString();
        }
        return couponDTOS;
    }

    public int findCouponCountByUidAndUseYn(String uid) {
        return couponRepository.countByUidAndUseYn(uid, "Y");
    }

    public int countOrderItemsByUidAndOrdStatusIn(String uid, List<String> ordStatusList) {
        return orderItemRepository.countByUidAndOrdStatusIn(uid, ordStatusList);
    }

    public int countByUidAndStatusIn(String uid,List<String> statusList) {
        return boardRepository.countByUidAndStatusIn(uid, statusList);
    }

    public List<BoardDTO> findByBoardAndUid(String uid) {

        log.info("내 문의들"+couponRepository.findCouponsByUid(uid));
        List<BoardEntity> result = boardRepository.findByUid(uid);

        return result.stream().map(boards->modelMapper.map(boards,BoardDTO.class))
                .collect(Collectors.toList());
    }

    public int countByUid(String uid){

        return boardRepository.countByUid(uid);
    }


    public PointPageResponseDTO getPointListByUid(String uid, PointPageRequestDTO pointPageRequestDTO) {
        Pageable pageable = pointPageRequestDTO.getPageable("pointDate");
        Page<Point> pointPage = pointRepository.findByUid(uid, pageable);

        return new PointPageResponseDTO(
                pointPageRequestDTO,
                pointPage.getContent().stream()
                        .map(Point::toDTO)
                        .collect(Collectors.toList()),
                (int) pointPage.getTotalElements()
        );
    }



    public CsPageResponseDTO QnaList(String uid,CsPageRequestDTO csPageRequestDTO){

        log.info("문의 목록 조회 1" + csPageRequestDTO);

        Pageable pageable = csPageRequestDTO.getPageable("bno");

        Page<BoardEntity> boardsPage = boardRepository.memberSelectBoards(uid,csPageRequestDTO, pageable);
        log.info("문의 목록 조회 2" + boardsPage);

        // Page<Product>를 List<ProductDTO>로 변환
        List<BoardDTO> boardDTOS = boardsPage.getContent().stream()
                .map(entity-> modelMapper.map(entity, BoardDTO.class))
                .toList();
        log.info("문의 목록 조회 3" + boardDTOS);

        int total = (int) boardsPage.getTotalElements();

        return CsPageResponseDTO.builder()
                .csPageRequestDTO(csPageRequestDTO)
                .dtoList(boardDTOS)
                .total(total)
                .build();
    }

    public ProductReviewPageResponseDTO reviewList(String uid, ProductReviewPageRequestDTO productReviewPageRequestDTO){

        log.info("리뷰 목록 조회 1" + productReviewPageRequestDTO);

        Pageable pageable = productReviewPageRequestDTO.getPageable("rdate");

        Page<Review> reviewPage = productRepository.memberSelectReview(uid,productReviewPageRequestDTO, pageable);
        log.info("리뷰 목록 조회 2" + reviewPage);

        List<ReviewDTO> reviewDTOS = reviewPage.getContent().stream()
                .map(entity-> modelMapper.map(entity, ReviewDTO.class))
                .toList();
        log.info("리뷰 목록 조회 3" + reviewDTOS);

        int total = (int) reviewPage.getTotalElements();

        return ProductReviewPageResponseDTO.builder()
                .productReviewPageRequestDTO(productReviewPageRequestDTO)
                .dtoList(reviewDTOS)
                .total(total)
                .build();


    }





}
