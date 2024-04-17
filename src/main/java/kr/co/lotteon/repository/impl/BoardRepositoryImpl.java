package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.cs.QBoardEntity;
import kr.co.lotteon.entity.cs.QBoardTypeEntity;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.entity.member.QMember;
import kr.co.lotteon.repository.custom.BoardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QBoardEntity qBoardEntity = QBoardEntity.boardEntity;
    private final QBoardTypeEntity qBoardTypeEntity = QBoardTypeEntity.boardTypeEntity;

    // 관리자 인덱스 글 목록 조회 (최신순 5개)
    @Override
    public List<Tuple> adminSelectBoards(String group){

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qBoardEntity, qBoardTypeEntity.typeName)
                .from(qBoardEntity)
                .where(qBoardEntity.group.eq(group))
                .join(qBoardTypeEntity)
                .on(qBoardEntity.typeNo.eq(qBoardTypeEntity.typeNo))
                .orderBy(qBoardEntity.bno.desc())
                .limit(5)
                .fetchResults();
        return results.getResults();
    }

    // 관리자 게시판관리 글 목록 조회 (최신순 5개)
    @Override
    public Page<BoardEntity> selectBoardsByGroup(String group) {
        
        return null;
    }
}
