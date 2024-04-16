package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
