package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.company.RecruitDTO;
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
    public Page<Recruit> searchRecruitForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable, RecruitDTO recruitDTO){

        BooleanExpression expression = null;

        // 고용 형태만 검색
        if(recruitDTO.getEmployment() != 8 && recruitDTO.getStatus() == 8){
            expression = qRecruit.employment.eq(recruitDTO.getEmployment());
            // 상태만 검색
        }else if(recruitDTO.getEmployment() == 8 && recruitDTO.getStatus() != 8){
            expression = qRecruit.status.eq(recruitDTO.getStatus());
            // 고용 형태 + 상태 검색
        }else{
            expression = qRecruit.status.eq(recruitDTO.getStatus()).and(qRecruit.employment.eq(recruitDTO.getEmployment()));
        }

        QueryResults<Recruit> results = jpaQueryFactory
                .select(qRecruit)
                .from(qRecruit)
                .where(expression)
                .orderBy(qRecruit.rno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(results.getResults() , pageable, total);
    }
}
