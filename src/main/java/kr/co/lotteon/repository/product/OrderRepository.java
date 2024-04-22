package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.Cate1;
import kr.co.lotteon.entity.product.Order;
import kr.co.lotteon.repository.custom.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {

}
