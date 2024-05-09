package kr.co.lotteon.repository.my;

import kr.co.lotteon.dto.member.point.PointDTO;
import kr.co.lotteon.entity.member.Point;
import kr.co.lotteon.repository.custom.PointRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer>, PointRepositoryCustom {
    // point
    Page<Point> findByUidOrderByCurrentDateDesc(String uid, Pageable pageable);
    // point 조회
    Page<Point> findByUidAndCurrentDateBetweenOrderByCurrentDateDesc(String uid, LocalDateTime begin, LocalDateTime end, Pageable pageable);

    public List<Point> findByUid(String uid);
}
