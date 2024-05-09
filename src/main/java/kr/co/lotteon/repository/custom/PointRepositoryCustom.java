package kr.co.lotteon.repository.custom;

import kr.co.lotteon.entity.member.Point;

import java.util.List;

public interface PointRepositoryCustom {

    public List<Point> selectByUidAndDate(String uid);
}
