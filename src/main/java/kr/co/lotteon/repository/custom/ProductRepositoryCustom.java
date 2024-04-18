package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    public Page<Product> adminSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable);

    public Page<Product> adminSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO, Pageable pageable);

}
