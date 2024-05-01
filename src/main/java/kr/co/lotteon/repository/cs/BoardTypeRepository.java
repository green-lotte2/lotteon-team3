package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.BoardFileEntity;
import kr.co.lotteon.entity.cs.BoardTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardTypeRepository extends JpaRepository<BoardTypeEntity, Integer> {

    public List<BoardTypeEntity> findByCate(String cate);


}
