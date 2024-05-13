package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.product.OrderItemDTO;
import kr.co.lotteon.dto.product.OrderItemPageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface OrderItemRepositoryCustom {

    public List<Tuple> selectOrderForChart();

    public List<Tuple> selectSumByPeriod(List<Integer> prodNos);

    public Tuple selectCountSumByPeriod(LocalDateTime period, List<Integer> prodNos);

    public List<Tuple> selectOrdStatusCount(List<Integer> prodNos);

    // 판매자 차트
    public List<Tuple> selectOrderForSeller(List<Integer> prodNos);

    public Page<Tuple> selectOrderList(AdminPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos);

    public Page<Tuple> searchOrderList(AdminPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos);

    // 관리자 주문 현황
    public Page<Tuple> selectOrderListAll(AdminPageRequestDTO pageRequestDTO, Pageable pageable);


    public Page<Tuple> searchOrderListAll(AdminPageRequestDTO pageRequestDTO, Pageable pageable);

    // myInfo 주문 및 배송 수 출력
    public int countByUidAndOrdStatusIn(String uid, List<String> ordStatusList);

    // seller 매출 차트1
    public List<Tuple> selectSales(String sellerId);

    // 마이페이지 home 최근주문내역 최신순 5개 조회
    public LinkedHashMap<Integer, List<OrderItemDTO>> selectOrder(String uid);

    // 마이페이지 home 최근주문내역 최신순 5개 조회
    public List<Tuple> selectOrdersByUid(String uid);

    // 주문 완료 페이지
    public List<Tuple> selectOrderComplete(int ordNo);

    // 전체주문내역 조회
    public Page<Tuple> selectWholeOrdersByUid(String uid, Pageable pageable, OrderItemPageRequestDTO orderItemPageRequestDTO);

    // 전체주문내역 검색조회
    public Page<Tuple> selectOrdersByDate(String uid, Pageable pageable, OrderItemPageRequestDTO orderItemPageRequestDTO);
}

