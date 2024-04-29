package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;
    private final QCart qCart = QCart.cart;
    private final QProduct qProduct = QProduct.product;
    private final QOption qOption = QOption.option;


    // 장바구니에 담긴 상품들 회사만 뽑아오기
    @Override
    public List<String> selectCartCompany(String uid) {
        // SELECT GROUP_CONCAT(DISTINCT b.company) FROM product_cart AS a JOIN product AS b ON a.prodNo = b.prodNo GROUP BY b.company
       List<Tuple> result = jpaQueryFactory
               .select(Expressions.stringTemplate("GROUP_CONCAT({0})"),qProduct.company)
               .from(qCart)
               .join(qProduct).on(qCart.prodNo.eq(qProduct.prodNo))
               .groupBy(qProduct.company)
               .fetch();

       return result.stream()
               .map(t -> t.get(qProduct.company)).collect(Collectors.toList());
    }
}
