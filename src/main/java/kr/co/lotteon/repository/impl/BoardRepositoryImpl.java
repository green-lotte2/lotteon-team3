package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminBoardPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.entity.cs.QBoardCateEntity;
import kr.co.lotteon.entity.cs.QBoardEntity;
import kr.co.lotteon.entity.cs.QBoardTypeEntity;
import kr.co.lotteon.entity.cs.QComment;
import kr.co.lotteon.entity.member.QMember;
import kr.co.lotteon.repository.custom.BoardRepositoryCustom;
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
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBoardEntity qBoardEntity = QBoardEntity.boardEntity;
    private final QBoardCateEntity qCateEntity = QBoardCateEntity.boardCateEntity;
    private final QBoardTypeEntity qBoardTypeEntity = QBoardTypeEntity.boardTypeEntity;
    private final QMember qMember = QMember.member;

    private final QComment qComment = QComment.comment;

    // 관리자 인덱스 글 목록 조회 (최신순 5개)
    @Override
    public List<Tuple> adminSelectBoards(String group) {

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity.typeName)
                .from(qBoardEntity)
                .where(qBoardEntity.group.eq(group))
                .join(qMember).on(qBoardEntity.uid.eq(qMember.uid))
                .join(qBoardTypeEntity).on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .join(qCateEntity).on(qBoardEntity.cate.eq(qCateEntity.cate))
                .orderBy(qBoardEntity.bno.desc())
                .limit(5)
                .fetchResults();
        return results.getResults();
    }

    // 관리자 게시판관리 글 목록 조회 (인덱스 - 최신순 5개, 게시판 - 10개)
    @Override
    public Page<Tuple> selectBoardsByGroup(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, String group) {

        // board 테이블과 cate, type 테이블을 Join해서 board 목록, cateName, typeName 가져옴
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity.typeName, qCateEntity.cateName, qMember.nick)
                .from(qBoardEntity)
                .where(qBoardEntity.group.eq(group))
                .join(qMember).on(qBoardEntity.uid.eq(qMember.uid))
                .join(qBoardTypeEntity).on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .join(qCateEntity).on(qBoardEntity.cate.eq(qCateEntity.cate))
                .orderBy(qBoardEntity.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> content = results.getResults();
        long total = results.getTotal();
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

    // 관리자 게시판관리 글 목록 검색 조회 (type, keyword)
    @Override
    public Page<Tuple> searchBoardsByGroup(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, String group) {
        log.info("키워드 검색 impl : " + pageRequestDTO.getKeyword());
        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if (type.equals("title")) {
            expression = qBoardEntity.group.eq(group).and(qBoardEntity.title.contains(keyword));
            log.info("제목 검색 : " + expression);

        } else if (type.equals("content")) {
            expression = qBoardEntity.group.eq(group).and(qBoardEntity.content.contains(keyword));
            log.info("내용 검색 : " + expression);

        } else if (type.equals("title_content")) {
            BooleanExpression titleContains = qBoardEntity.group.eq(group).and(qBoardEntity.title.contains(keyword));
            BooleanExpression contentContains = qBoardEntity.group.eq(group).and(qBoardEntity.content.contains(keyword));
            expression = qBoardEntity.group.eq(group).and(titleContains).or(contentContains);
            log.info("제목+내용 검색 : " + expression);

        } else if (type.equals("nick")) {
            expression = qBoardEntity.group.eq(group).and(qMember.nick.contains(keyword));
            log.info("작성자 검색 : " + expression);
        }
        // select * from board where `group`= ? and `type` contains(k) limit 0,10;
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity.typeName, qCateEntity.cateName, qMember.nick)
                .from(qBoardEntity)
                .join(qMember).on(qBoardEntity.uid.eq(qMember.uid))
                .join(qBoardTypeEntity).on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .join(qCateEntity).on(qBoardEntity.cate.eq(qCateEntity.cate))
                .where(expression)
                .orderBy(qBoardEntity.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        log.info("키워드 검색 5 " + results.getResults().toString());
        List<Tuple> content = results.getResults();
        log.info("키워드 검색 6 ");
        long total = results.getTotal();
        log.info("키워드 검색 7 ");
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

    // 관리자 게시판관리 글 목록 카테고리 검색 조회 (type, cate)
    @Override
    public Page<Tuple> searchBoardsByCate(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, String group, String cate) {
        log.info("cate 검색 impl 1 : " + pageRequestDTO.getKeyword());
        log.info("cate 검색 impl 2 group : " + group);
        log.info("cate 검색 impl 3 cate : " + cate);
        // select * from board where `group`= ? and `cate`= ? limit 0,10;
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity.typeName, qCateEntity.cateName, qMember.nick)
                .from(qBoardEntity)
                .join(qMember).on(qBoardEntity.uid.eq(qMember.uid))
                .join(qBoardTypeEntity).on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .join(qCateEntity).on(qBoardEntity.cate.eq(qCateEntity.cate))
                .where(qBoardEntity.group.eq(group).and(qBoardEntity.cate.eq(cate)))
                .orderBy(qBoardEntity.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        log.info("cate 검색 impl 2 : " + cate);

        List<Tuple> content = results.getResults();
        log.info("cate 검색 impl 3 : " + content);
        long total = results.getTotal();
        log.info("cate 검색 impl 4 : " + total);

        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

    // 판매자 상품문의 기본 조회
    public Page<Tuple> selectBoardBySeller(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos) {
        log.info("상품문의 기본 조회 Impl 1 : " + prodNos);

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity.typeName, qCateEntity.cateName, qMember.nick)
                .from(qBoardEntity)
                .join(qMember).on(qBoardEntity.uid.eq(qMember.uid))
                .join(qBoardTypeEntity).on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .join(qCateEntity).on(qBoardEntity.cate.eq(qCateEntity.cate))
                .where(qBoardEntity.group.eq("qna").and(qBoardEntity.prodNo.in(prodNos)))
                .orderBy(qBoardEntity.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> content = results.getResults();
        log.info("상품문의 기본 조회 Impl 2 : " + content);
        long total = results.getTotal();
        log.info("상품문의 기본 조회 Impl 3 : " + total);
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

    // 판매자 상품문의 검색 조회 (type, cate)
    public Page<Tuple> searchBoardBySellerAndCate(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos, String cate) {
        log.info("상품문의 검색 조회 Impl 1 : " + prodNos);
        log.info("상품문의 검색 조회 impl 2 : " + pageRequestDTO.getKeyword());
        log.info("상품문의 검색 조회 impl 3 cate : " + cate);
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity.typeName, qCateEntity.cateName, qMember.nick)
                .from(qBoardEntity)
                .join(qMember).on(qBoardEntity.uid.eq(qMember.uid))
                .join(qBoardTypeEntity).on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .join(qCateEntity).on(qBoardEntity.cate.eq(qCateEntity.cate))
                .where(qBoardEntity.group.eq("qna").and(qBoardEntity.prodNo.in(prodNos)).and(qBoardEntity.cate.eq(cate)))
                .orderBy(qBoardEntity.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> content = results.getResults();
        log.info("상품문의 검색 조회 Impl 2 : " + content);
        long total = results.getTotal();
        log.info("상품문의 검색 조회 Impl 3 : " + total);
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

    // 판매자 상품문의 검색 조회 (type, Keyword)
    public Page<Tuple> searchBoardsBySellerAndKeyword(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos) {
        log.info("키워드 검색 impl 1 : " + pageRequestDTO.getKeyword());
        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanExpression expression = null;

        // 검색 종류에 따른 where절 표현식 생성
        if (type.equals("title")) {
            expression = qBoardEntity.prodNo.in(prodNos).and(qBoardEntity.title.contains(keyword));
            log.info("제목 검색 : " + expression);

        } else if (type.equals("content")) {
            expression = qBoardEntity.prodNo.in(prodNos).and(qBoardEntity.content.contains(keyword));
            log.info("내용 검색 : " + expression);

        } else if (type.equals("title_content")) {
            BooleanExpression titleContains = qBoardEntity.prodNo.in(prodNos).and(qBoardEntity.title.contains(keyword));
            BooleanExpression contentContains = qBoardEntity.prodNo.in(prodNos).and(qBoardEntity.content.contains(keyword));
            expression = qBoardEntity.prodNo.in(prodNos).and(titleContains).or(contentContains);
            log.info("제목+내용 검색 : " + expression);

        } else if (type.equals("nick")) {
            expression = qBoardEntity.prodNo.in(prodNos).and(qMember.nick.contains(keyword));
            log.info("작성자 검색 : " + expression);
        }
        // select * from board where `group`= ? and `type` contains(k) limit 0,10;
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity.typeName, qCateEntity.cateName, qMember.nick)
                .from(qBoardEntity)
                .join(qMember).on(qBoardEntity.uid.eq(qMember.uid))
                .join(qBoardTypeEntity).on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .join(qCateEntity).on(qBoardEntity.cate.eq(qCateEntity.cate))
                .where(expression)
                .orderBy(qBoardEntity.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        log.info("키워드 검색 5 " + results.getResults().toString());
        List<Tuple> content = results.getResults();
        log.info("키워드 검색 6 ");
        long total = results.getTotal();
        log.info("키워드 검색 7 ");
        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

    // 판매자 상품문의 개수 조회
    @Override
    public long countSellerQna(List<Integer> prodNos) {
        return jpaQueryFactory
                .select(qBoardEntity)
                .from(qBoardEntity)
                .where(qBoardEntity.prodNo.in(prodNos).and(qBoardEntity.status.contains("검토중")))
                .fetchCount();

    }

    @Override
    public int countByUidAndStatusIn(String uid, List<String> statusList) {
        return (int) jpaQueryFactory
                .select(qBoardEntity)
                .from(qBoardEntity)
                .where(qBoardEntity.uid.eq(uid)
                        .and(qBoardEntity.status.in(statusList)))
                .fetchCount();
    }

    @Override
    public Page<Tuple> memberSelectBoards(String uid, CsPageRequestDTO csPageRequestDTO, Pageable pageable) {
        log.info("마이페이지 문의내역 목록 조회 Impl 1 : " + csPageRequestDTO);
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qCateEntity.cateName)
                .from(qBoardEntity)
                .where(
                        qBoardEntity.uid.eq(uid),
                        qBoardEntity.group.eq("qna"))
                .join(qCateEntity).on(qCateEntity.cate.eq(qBoardEntity.cate))
                .orderBy(
                        qBoardEntity.rdate.desc(), // rdate를 기준으로 내림차순으로 정렬
                        qBoardEntity.status.desc() // 상태값을 기준으로 내림차순으로 정렬
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> content = results.getResults();

        log.info("마이페이지 문의내역 목록 조회 Impl 2 : " + content);
        long total = results.getTotal();

        log.info("마이페이지 문의내역 목록 조회 Impl 3 : " + total);

        return new PageImpl<>(content, pageable, total);
    }

    // 마이페이지 home 문의하기 최신순 5개출력
    @Override
    public List<Tuple> selectReviewsByUidAndRdate(String uid) {
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qCateEntity.cateName)
                .from(qBoardEntity)
                .where(qBoardEntity.uid.eq(uid),
                        qBoardEntity.group.eq("qna"))
                .join(qCateEntity).on(qCateEntity.cate.eq(qBoardEntity.cate))
                .orderBy(
                        qBoardEntity.rdate.desc() // rdate를 기준으로 내림차순으로 정렬
                )
                .limit(5)
                .fetchResults();
        return results.getResults();

    }

    // 상품 보기 문의, 답변 조회
    public Page<Tuple> selectQna(int prodNo, Pageable pageable) {
        log.info("문의 조회 impl 1 : " + prodNo);
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity, qComment)
                .from(qBoardEntity)
                .join(qBoardTypeEntity).on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .leftJoin(qComment).on(qBoardEntity.bno.eq(qComment.bno))
                .where(qBoardEntity.prodNo.eq(prodNo))
                .orderBy(qBoardEntity.bno.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Tuple> content = results.getResults();
        log.info("문의 조회 impl 3 : " + content);
        long total = results.getTotal();
        log.info("문의 조회 impl 4 : " + total);

        // 페이지 처리용 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

}


