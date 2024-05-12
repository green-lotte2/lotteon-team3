package kr.co.lotteon.repository.product;

import kr.co.lotteon.dto.product.OrderItemDTO;
import kr.co.lotteon.entity.product.OrderItem;
import kr.co.lotteon.repository.custom.OrderItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>, OrderItemRepositoryCustom {
}
