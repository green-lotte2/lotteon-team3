package kr.co.lotteon.repository.my;

import kr.co.lotteon.entity.member.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {
    Page<Point> findByUid(String uid, Pageable pageable);
}
