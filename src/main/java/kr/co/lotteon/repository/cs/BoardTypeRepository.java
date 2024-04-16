package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.BoardTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardTypeRepository extends JpaRepository<BoardTypeEntity, Integer> {
}
