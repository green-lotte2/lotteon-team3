package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.Comment;
import kr.co.lotteon.repository.custom.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>, CommentRepositoryCustom {

    void deleteCommentByBno(int bno);
}
