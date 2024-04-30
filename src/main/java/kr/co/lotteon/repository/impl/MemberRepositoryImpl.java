package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.entity.member.QMember;
import kr.co.lotteon.repository.custom.MemberRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    
    private final JPAQueryFactory jpaQueryFactory;
    private final QMember qMember = QMember.member;

    // 월별 가입 count 조회 - 오늘 기준 12개월 전 까지
    @Override
    public List<Tuple> selectMemberForChart(){

        LocalDateTime twelveMonthsAgo = LocalDateTime.now().minusMonths(12);
        log.info("월별 가입 count 조회 Impl 1 : " + twelveMonthsAgo);

        return jpaQueryFactory.select(qMember.rdate.year(), qMember.rdate.month(), qMember.count())
                .from(qMember)
                .where(qMember.rdate.after(twelveMonthsAgo))
                .groupBy(qMember.rdate.year(), qMember.rdate.month())
                .orderBy(qMember.rdate.year().asc(), qMember.rdate.month().asc())
                .fetch();
    }
    // 회원 목록 (현황) 기본 조회
    @Override
    public Page<Member> selectMemberList(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable){
        log.info("회원 목록 (현황) 기본 조회 Impl 1 : " + adminPageRequestDTO);
        QueryResults<Member> results = jpaQueryFactory
                .select(qMember)
                .from(qMember)
                .orderBy(qMember.rdate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        log.info("회원 목록 (현황) 기본 조회 Impl 2 : " + total);
        List<Member> memberList = results.getResults();
        log.info("회원 목록 (현황) 기본 조회 Impl 3 : " + memberList);
        return new PageImpl<>(memberList, pageable, total);
    }
    // 회원 목록 (현황) 검색 조회
    public Page<Member> searchMemberList(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable){
        log.info("회원 목록 (현황) 검색 조회 Impl 1 : " + adminPageRequestDTO);
        String type = adminPageRequestDTO.getType();
        String keyword = adminPageRequestDTO.getKeyword();
        log.info("회원 목록 (현황) 검색 조회 Impl 2 : " + type);
        log.info("회원 목록 (현황) 검색 조회 Impl 3 : " + keyword);

        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if(type.equals("uid")){
            expression = qMember.uid.contains(keyword);
            log.info("uid 검색 : " + expression);

        }else if(type.equals("name")){
            expression = qMember.name.contains(keyword);
            log.info("name 검색 : " + expression);

        }else if(type.equals("nick")){
            expression = qMember.nick.contains(keyword);
            log.info("nick 검색 : " + expression);

        }else if(type.equals("gender")){
            expression = qMember.gender.contains(keyword);
            log.info("gender 검색 : " + expression);

        }else if(type.equals("level")){
            // 입력된 키워드를 정수형으로 변환
            int level = Integer.parseInt(keyword);
            expression = qMember.level.eq(level);
            log.info("level 검색 : " + expression);
        }
        // DB 조회
        QueryResults<Member> results = jpaQueryFactory
                .select(qMember)
                .from(qMember)
                .where(expression)
                .orderBy(qMember.rdate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        long total = results.getTotal();
        log.info("회원 목록 (현황) 검색 조회 Impl 2 : " + total);
        List<Member> memberList = results.getResults();
        log.info("회원 목록 (현황) 검색 조회 Impl 3 : " + memberList);
        return new PageImpl<>(memberList, pageable, total);
    }
}
