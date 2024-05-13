package kr.co.lotteon.repository.admin;

import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.entity.admin.Banner;
import kr.co.lotteon.entity.product.Cate1;
import kr.co.lotteon.repository.custom.BannerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Integer> , BannerRepositoryCustom {

    List<Banner> findByCate(String cate);
}
