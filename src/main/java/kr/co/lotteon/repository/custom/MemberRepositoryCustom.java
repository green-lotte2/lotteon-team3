package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;

import java.util.List;

public interface MemberRepositoryCustom {

    // 월별 가입 count 조회
    public List<Tuple> selectMemberForChart();

}
