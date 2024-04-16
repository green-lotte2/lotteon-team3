package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.product.AdminPageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    public Page<Product> adminSelectProducts(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);

    public Page<Product> adminSearchProducts(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);
}
