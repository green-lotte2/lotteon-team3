package kr.co.lotteon.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.dto.product.CartInfoDTO;
import kr.co.lotteon.dto.product.PageRequestDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.QProduct;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CartRepositoryCustom {

    public List<String> selectCartCompany(String uid);
    public List<CartInfoDTO> selectCartProduct(String uid);
}
