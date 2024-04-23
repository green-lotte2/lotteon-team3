package kr.co.lotteon.repository;

import kr.co.lotteon.entity.admin.Banner;
import kr.co.lotteon.entity.product.Cate1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> {

}
