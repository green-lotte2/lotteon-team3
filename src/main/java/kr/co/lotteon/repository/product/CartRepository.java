package kr.co.lotteon.repository.product;

import kr.co.lotteon.dto.product.CartDTO;
import kr.co.lotteon.entity.product.Cart;
import kr.co.lotteon.repository.custom.CartRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>, CartRepositoryCustom {
    public List<Cart> findCartByUidAndProdNo(String uid, int prodNo);
    public List<Cart> findCartByUid(String uid);
}
