package kr.co.lotteon.repository.my;

import kr.co.lotteon.dto.member.point.PointDTO;
import kr.co.lotteon.entity.member.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {

    // point 기본 리스트
    Page<Point> findByUidOrderByCurrentDateDesc(String uid, Pageable pageable);

    List<Point> findByUidAndPointDateBetweenOrderByPointDateDesc(String uid, LocalDateTime start, LocalDateTime end);

}
