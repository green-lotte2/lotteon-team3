package kr.co.lotteon.mapper;

import kr.co.lotteon.dto.product.ProductDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    public void updateProductHit(int prodNo);

    public void updateProductByProdNo(ProductDTO productDTO);

    public void updateStatusByProdNo(int prodNo, String status);

    public void updateProductStock(int prodNo, int count);
}
