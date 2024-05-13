package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.entity.cs.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    // 댓글 목록 조회
    public List<Tuple> selectComments(int ano);
    public Tuple selectCommentAndNick(int ano);

    public Comment selectComment(int bno);

}
