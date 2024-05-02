package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.admin.Banner;
import kr.co.lotteon.entity.admin.QArticle;
import kr.co.lotteon.entity.admin.QBanner;
import kr.co.lotteon.repository.custom.ArticleRepositoryCustom;
import kr.co.lotteon.repository.custom.BannerRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QArticle qArticle = QArticle.article;

    // 관리자 회사소개 리스트
    @Override
    public Page<Article> selectArticleForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable, String cate1){

        QueryResults<Article> results = jpaQueryFactory
                .select(qArticle)
                .from(qArticle)
                .where(qArticle.cate1.eq(cate1))
                .orderBy(qArticle.ano.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(results.getResults() , pageable, total);
    }
    // 관리자 회사소개 리스트 - 검색
    @Override
    public Page<Article> searchArticleForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable, String cate1){
        String cate2 = adminPageRequestDTO.getKeyword();

        QueryResults<Article> results = jpaQueryFactory
                .select(qArticle)
                .from(qArticle)
                .where(qArticle.cate1.eq(cate1).and(qArticle.cate2.eq(cate2)))
                .orderBy(qArticle.ano.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(results.getResults() , pageable, total);
    }

}