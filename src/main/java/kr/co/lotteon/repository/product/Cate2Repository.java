package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.Cate2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Cate2Repository extends JpaRepository<Cate2, Integer> {

    public List<Cate2> findByCate1(int cate1);
    public Cate2 findBycate1AndCate2(int cate1, int cate2);
}
