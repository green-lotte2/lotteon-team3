package kr.co.lotteon.repository.admin;

import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.admin.Banner;
import kr.co.lotteon.repository.custom.ArticleRepositoryCustom;
import kr.co.lotteon.repository.custom.BannerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> , ArticleRepositoryCustom {

}
