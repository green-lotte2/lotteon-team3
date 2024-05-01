package kr.co.lotteon.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemMapper {

    public void updateOrdStatus (@Param("ordStatus") String ordStatus, @Param("ordItemno") int ordItemno);
}
