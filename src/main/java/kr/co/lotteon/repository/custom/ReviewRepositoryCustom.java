package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.product.CartInfoDTO;

import java.util.List;

public interface ReviewRepositoryCustom {

    // 리뷰 비율 조회
    public double selectReviewAvg(int prodNo);

    // 리뷰 작성 여부 조회
    public int selectReviewCountByUid(String uid,int ordNo,int prodNo);
}
