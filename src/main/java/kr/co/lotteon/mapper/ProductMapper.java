package kr.co.lotteon.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    public void updateProductHit(int prodNo);
}
