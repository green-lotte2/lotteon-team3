package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.company.StoryPageRequestDTO;
import kr.co.lotteon.entity.admin.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    public Page<Article> selectArticleForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable,  String cate1);

    public Page<Article> searchArticleForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable, String cate1);

    public Page<Article> selectStories(StoryPageRequestDTO storyPageRequestDTO, Pageable pageable);
    public Page<Article> searchStories(StoryPageRequestDTO storyPageRequestDTO, Pageable pageable);
}
