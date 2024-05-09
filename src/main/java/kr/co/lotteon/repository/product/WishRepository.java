package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Integer> {

    public Boolean existsByUidAndProdNo(String uid, int prodNo);

    public void deleteByUidAndProdNo(String uid, int prodNo);
}
