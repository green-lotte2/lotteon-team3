package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CsRepository extends JpaRepository<BoardEntity, Integer> {

    // 리스트 출력시(그룹, 카테고리로 구분)
    @Query("SELECT b FROM BoardEntity b WHERE b.group = :group AND (b.cate= :cate OR :cate = 'null')")
    public Page<BoardEntity> findByGroupAndCate(String group, String cate, Pageable pageable);
}
