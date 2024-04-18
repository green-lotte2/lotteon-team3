package kr.co.lotteon.mapper;

import kr.co.lotteon.dto.member.MemberDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    public int selectCountMember(@Param("type") String type, @Param("value") String value);
}
