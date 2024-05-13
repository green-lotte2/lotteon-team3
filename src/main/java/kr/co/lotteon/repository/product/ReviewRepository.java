package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.Cart;
import kr.co.lotteon.entity.product.Review;
import kr.co.lotteon.repository.custom.CartRepositoryCustom;
import kr.co.lotteon.repository.custom.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, ReviewRepositoryCustom {

}
