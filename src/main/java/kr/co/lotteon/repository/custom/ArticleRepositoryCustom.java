package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.admin.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepositoryCustom {

    public Page<Article> selectArticleForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable,  String cate1);

    public Page<Article> searchArticleForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable, String cate1);
}
