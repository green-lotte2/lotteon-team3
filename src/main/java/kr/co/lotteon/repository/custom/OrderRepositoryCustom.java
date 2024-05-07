package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.entity.product.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderRepositoryCustom {

    // 관리자 차트
    public List<Tuple> selectOrderForChart();

    // 오더 출력
    public List<Tuple> selectOrderFromCart(int cartNo);

}
