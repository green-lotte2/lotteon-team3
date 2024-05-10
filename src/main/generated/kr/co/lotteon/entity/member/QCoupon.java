package kr.co.lotteon.entity.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 506320375L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final StringPath couponName = createString("couponName");

    public final NumberPath<Integer> couponSeq = createNumber("couponSeq", Integer.class);

    public final NumberPath<Integer> discountLimit = createNumber("discountLimit", Integer.class);

    public final NumberPath<Integer> discountMoney = createNumber("discountMoney", Integer.class);

    public final NumberPath<Integer> discountPercent = createNumber("discountPercent", Integer.class);

    public final NumberPath<Integer> discountType = createNumber("discountType", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> expireDate = createDateTime("expireDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final StringPath uid = createString("uid");

    public final StringPath useYn = createString("useYn");

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

