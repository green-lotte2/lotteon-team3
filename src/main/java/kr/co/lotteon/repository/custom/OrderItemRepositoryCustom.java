package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepositoryCustom {

    public List<Tuple> selectOrderForChart();

    public List<Tuple> selectSumByPeriod(List<Integer> prodNos);

    public Tuple selectCountSumByPeriod(LocalDateTime period, List<Integer> prodNos);

    public List<Tuple> selectOrdStatusCount(List<Integer> prodNos);

}
