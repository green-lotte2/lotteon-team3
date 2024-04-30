package kr.co.lotteon.repository.my;

import kr.co.lotteon.dto.member.CouponDTO;
import kr.co.lotteon.entity.member.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,String> {
    public List<Coupon> findCouponsByUid(String uid);


}
