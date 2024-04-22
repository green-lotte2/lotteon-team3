package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.Cate3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Cate3Repository extends JpaRepository<Cate3, Integer> {
    public Cate3 findByCate2AndCate3(int cate2, int cate3);
    public List<Cate3> findByCate2(int cate2);
}
