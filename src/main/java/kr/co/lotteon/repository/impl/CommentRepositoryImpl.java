package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.cs.Comment;
import kr.co.lotteon.entity.cs.QComment;
import kr.co.lotteon.entity.member.QMember;
import kr.co.lotteon.repository.custom.CommentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QComment qComment = QComment.comment;
    private QMember qMember = QMember.member;

    // 댓글 목록 조회
    public List<Tuple> selectComments(int bno){
        
        // SELECT comment.*,member.nick from comment join user on comment.uid = user.uid where comment.ano= ?;
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qComment, qMember.nick)
                .from(qComment)
                .where(qComment.bno.eq(bno))
                .join(qMember)
                .on(qComment.uid.eq(qMember.uid))
                .orderBy(qComment.cno.desc())
                .fetchResults();
        // List<Tuple> 리턴
        return results.getResults();
    }
    // 문의 답변 조회
    public Comment selectComment(int bno){

        return jpaQueryFactory
                .select(qComment)
                .from(qComment)
                .where(qComment.bno.eq(bno))
                .fetchOne();
    }
    // 댓글 작성 후 불러오기
    public Tuple selectCommentAndNick(int cno){

        Tuple results = jpaQueryFactory
                                .select(qComment, qMember.nick)
                                .from(qComment)
                                .where(qComment.cno.eq(cno))
                                .join(qMember)
                                .on(qComment.uid.eq(qMember.uid))
                                .fetchOne();

        return results;
    }
}
