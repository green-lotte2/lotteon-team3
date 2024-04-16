package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.BoardCateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCateRepository extends JpaRepository<BoardCateEntity, String> {
}
