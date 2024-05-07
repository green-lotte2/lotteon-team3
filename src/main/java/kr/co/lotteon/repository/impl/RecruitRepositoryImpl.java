package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.admin.QRecruit;
import kr.co.lotteon.entity.admin.Recruit;
import kr.co.lotteon.repository.custom.RecruitRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RecruitRepositoryImpl implements RecruitRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QRecruit qRecruit = QRecruit.recruit;

    // 관리자 - 회사소개 채용 조회
    @Override
    public Page<Recruit> selectRecruitForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable){

        QueryResults<Recruit> results = jpaQueryFactory
                .select(qRecruit)
                .from(qRecruit)
                .orderBy(qRecruit.rno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(results.getResults() , pageable, total);
    }

    // 관리자 - 회사소개 채용 검색
    @Override
    public Page<Recruit> searchRecruitForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable){
        QueryResults<Recruit> results = jpaQueryFactory
                .select(qRecruit)
                .from(qRecruit)
                .orderBy(qRecruit.rno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(results.getResults() , pageable, total);
    }
}
