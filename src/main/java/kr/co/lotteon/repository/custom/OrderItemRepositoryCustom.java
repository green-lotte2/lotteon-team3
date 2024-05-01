package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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

}
