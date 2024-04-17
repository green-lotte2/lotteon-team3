package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Integer> {

    public List<BoardFileEntity> findByBno(int bno);

}
