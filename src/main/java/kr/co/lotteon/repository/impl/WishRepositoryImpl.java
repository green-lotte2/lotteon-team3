package kr.co.lotteon.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.entity.product.QWish;
import kr.co.lotteon.entity.product.Wish;
import kr.co.lotteon.repository.custom.WishRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WishRepositoryImpl implements WishRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final QWish qWish = QWish.wish;
    private final QProduct qProduct = QProduct.product;

    // 나의 찜 위시리스트 조회
    @Override
    public Page<Tuple> selectWishList(String uid, Pageable pageable){

        QueryResults<Tuple> results = jpaQueryFactory
                .select(qWish, qProduct)
                .from(qWish)
                .join(qProduct).on(qWish.prodNo.eq(qProduct.prodNo))
                .where(qWish.uid.eq(uid))
                .orderBy(qWish.wishNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
