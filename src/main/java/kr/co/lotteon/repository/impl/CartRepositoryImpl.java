package kr.co.lotteon.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.product.CartInfoDTO;
import kr.co.lotteon.entity.product.QCart;
import kr.co.lotteon.entity.product.QOption;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.repository.custom.CartRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;
    private final QCart qCart = QCart.cart;
    private final QProduct qProduct = QProduct.product;
    private final QOption qOption = QOption.option;

    @Override
    public Map<String, List<CartInfoDTO>> productCartList(String uid) {
        log.info("카트 조회 impl1");

        return null;
    }
}
