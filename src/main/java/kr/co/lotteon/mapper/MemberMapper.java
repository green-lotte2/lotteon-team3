package kr.co.lotteon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    public int selectCountUser(@Param("Datatype") String type, @Param("value")String value);
}
