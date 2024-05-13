package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.product.CartInfoDTO;

import java.util.List;

public interface ReviewRepositoryCustom {

    // 리뷰 비율 조회
    public double selectReviewAvg(int prodNo);
}
