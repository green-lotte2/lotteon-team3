package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.product.AdminPageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    public Page<Product> selectProducts(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);

    public Page<Product> searchProducts(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);

}
