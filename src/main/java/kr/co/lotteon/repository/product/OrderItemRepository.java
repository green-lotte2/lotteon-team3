package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.OrderItem;
import kr.co.lotteon.repository.custom.OrderItemRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>, OrderItemRepositoryCustom {

    // order 조회
    Page<OrderItem> findByUidAndOrdDateBetweenOrderByOrdDateDesc(String uid, LocalDateTime begin, LocalDateTime end, Pageable pageable);

}
