package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsRepository extends JpaRepository<BoardEntity, Integer> {

    // 리스트 출력시(그룹, 카테고리로 구분)
    public Page<BoardEntity> findByGroupAndCate(String group, String cate, Pageable pageable);
}
