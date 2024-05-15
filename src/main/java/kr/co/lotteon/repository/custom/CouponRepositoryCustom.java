package kr.co.lotteon.repository.custom;

import groovy.lang.Tuple;
import kr.co.lotteon.entity.member.Coupon;

import java.util.List;

public interface CouponRepositoryCustom {
    public List<Coupon> selectsCouponsNotUse (String uid);
}
