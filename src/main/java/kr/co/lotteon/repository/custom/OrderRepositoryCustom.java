package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.entity.product.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface OrderRepositoryCustom {

    public List<Tuple> selectOrderForChart();

    public Map<Integer, Long> selectCountAndSum();

}
