package kr.co.lotteon.repository.custom;

import kr.co.lotteon.entity.admin.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BannerRepositoryCustom {

    public List<Banner> selectByCateAndAct(String cate);
}
