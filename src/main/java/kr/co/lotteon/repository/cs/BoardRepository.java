package kr.co.lotteon.repository.cs;

import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.repository.custom.BoardRepositoryCustom;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardRepositoryCustom {

    // 리스트 출력시(그룹, 카테고리로 구분)
    @Query("SELECT b FROM BoardEntity b WHERE b.group = :group AND (b.cate= :cate OR :cate = 'null')")
    public Page<BoardEntity> findByGroupAndCate(String group, String cate, Pageable pageable);

    List<BoardEntity> findTop10ByTypeNoOrderByRdateDesc(int typeNo);

    List<BoardEntity> findByGroupOrderByRdateDesc(String group);

    // 인덱스 - notice, qna 리스트 출력
    public List<BoardEntity> findByGroupOrderByRdateDescBnoDesc(String group, Pageable pageable);

    // 글 삭제
    public void deleteByBno(int bno);

    // 댓글 개수 ++
    @Modifying
    @Query("UPDATE BoardEntity a SET a.reply = a.reply + 1 WHERE a.bno = :bno")
    void incrementReplyByBno(@Param("bno") int bno);

    // 게시글 상태 변경
    @Modifying
    @Query("UPDATE BoardEntity a set a.status = '답변완료' where a.bno = :bno")
    void modifyStatusByBno(@Param("bno") int bno);


    @Query("SELECT b FROM BoardEntity b WHERE b.uid = :uid")
    public List<BoardEntity> findByUid(@Param("uid") String uid);

    @Query("SELECT COUNT(b) FROM BoardEntity b WHERE b.uid = :uid")
    public int countByUid(@Param("uid") String uid);



}
