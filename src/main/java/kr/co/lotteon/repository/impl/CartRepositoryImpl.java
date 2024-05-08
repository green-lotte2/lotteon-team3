package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.lotteon.dto.product.CartInfoDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.QCart;
import kr.co.lotteon.entity.product.QOption;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.repository.custom.CartRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final EntityManager entityManager;
    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;
    private final QProduct qProduct = QProduct.product;
    private final QCart qCart = QCart.cart;
    // 장바구니에 담긴 상품들 회사만 뽑아오기
    @Override
    public List<String> selectCartCompany(String uid) {

        // SELECT GROUP_CONCAT(DISTINCT b.company) FROM product_cart AS a JOIN product AS b ON a.prodNo = b.prodNo GROUP BY b.company
       List<String> result = entityManager.createNativeQuery(
               "SELECT GROUP_CONCAT(DISTINCT p.company) " +
               "FROM product_cart AS c JOIN product AS p ON c.prodNo = p.prodNo " +
               "WHERE c.uid = :uid " +
               "GROUP BY p.company", String.class)
               .setParameter("uid", uid)
               .getResultList();

       log.info("cartImpl " + result);

       return result;
    }

    @Override
    public List<CartInfoDTO> selectCartProduct(String uid) {


        List<Tuple> result = jpaQueryFactory.select(qCart.count, qCart.opNo, qCart.cartNo, qProduct)
                .from(qCart)
                .join(qProduct).on(qCart.prodNo.eq(qProduct.prodNo))
                .where(qCart.uid.eq(uid))
                .fetch();

        log.info("CartImpl {}", result);

        List<CartInfoDTO> resultValue = result.stream()
                .map(tuple -> {
                    Product product = tuple.get(3, Product.class);

                    CartInfoDTO cartInfoDTO = modelMapper.map(product, CartInfoDTO.class);
                    cartInfoDTO.setCount(tuple.get(0, Integer.class));
                    cartInfoDTO.setOpNo(tuple.get(1, String.class));
                    cartInfoDTO.setCartNo(tuple.get(2, Integer.class));

                    return cartInfoDTO;
                })
                .toList();

        log.info("CartImpl2 {}", resultValue);

        return resultValue;

    }
}
