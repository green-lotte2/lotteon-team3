package kr.co.lotteon.service.my;

import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageResponseDTO;
import kr.co.lotteon.dto.member.CouponDTO;

import kr.co.lotteon.dto.member.point.PointDTO;
import kr.co.lotteon.dto.member.point.PointPageRequestDTO;
import kr.co.lotteon.dto.member.point.PointPageResponseDTO;
import kr.co.lotteon.dto.product.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.member.Coupon;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.member.Point;

import kr.co.lotteon.repository.cs.BoardRepository;
import kr.co.lotteon.repository.member.MemberRepository;
import kr.co.lotteon.repository.my.CouponRepository;
import kr.co.lotteon.repository.my.PointRepository;
import kr.co.lotteon.repository.product.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
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

    // point 리스트 출력
    public PointPageResponseDTO getPointListByUid(String uid, PointPageRequestDTO pointPageRequestDTO) {
        Pageable pageable = pointPageRequestDTO.getPageable("pointDate");
        Page<Point> pointPage = pointRepository.findByUidOrderByCurrentDateDesc(uid, pageable);

        return new PointPageResponseDTO(
                pointPageRequestDTO,
                pointPage.getContent().stream()
                        .map(Point::toDTO)
                        .collect(Collectors.toList()),
                (int) pointPage.getTotalElements()
        );
    }

    // point 리스트 출력(period 3개)
    public List<PointDTO> getPointByPeriod(String uid, LocalDateTime start, LocalDateTime end) {
        // 기간에 해당하는 포인트 데이터를 가져오는 리포지토리 메서드 호출
        log.info("피리오드 서비스" + uid, start, end);
        List<Point> pointList = pointRepository.findByUidAndPointDateBetweenOrderByPointDateDesc(uid, start, end);

        // 포인트 데이터를 DTO로 변환하여 반환
        return pointList.stream()
                .map(Point::toDTO)
                .collect(Collectors.toList());
    }


    public CsPageResponseDTO QnaList(CsPageRequestDTO csPageRequestDTO){

        log.info("문의 목록 조회 1" + csPageRequestDTO);

        Pageable pageable = csPageRequestDTO.getPageable("bno");

        Page<BoardEntity> boardsPage = boardRepository.memberSelectBoards(csPageRequestDTO, pageable);
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





}
