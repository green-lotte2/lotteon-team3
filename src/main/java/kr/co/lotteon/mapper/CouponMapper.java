package kr.co.lotteon.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponMapper {
    public void updateUseYn(int couponSeq);
}
