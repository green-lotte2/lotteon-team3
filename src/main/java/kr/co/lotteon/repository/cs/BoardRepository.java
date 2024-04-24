package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.repository.custom.BoardRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardRepositoryCustom {

    // 리스트 출력시(그룹, 카테고리로 구분)
    @Query("SELECT b FROM BoardEntity b WHERE b.group = :group AND (b.cate= :cate OR :cate = 'null')")
    public Page<BoardEntity> findByGroupAndCate(String group, String cate, Pageable pageable);

    List<BoardEntity> findTop10ByTypeNoOrderByRdateDesc(int typeNo);

    // 인덱스 - notice, qna 리스트 출력
    public List<BoardEntity> findByGroupOrderByRdateDescBnoDesc(String group, Pageable pageable);

    // 글 삭제
    public void deleteBoardsByParent(int bno);
}
