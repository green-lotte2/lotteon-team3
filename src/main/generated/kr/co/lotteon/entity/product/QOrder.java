package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -544745140L;

    public static final QOrder order = new QOrder("order1");

    public final NumberPath<Integer> couponSeq = createNumber("couponSeq", Integer.class);

    public final NumberPath<Integer> disCouponPrice = createNumber("disCouponPrice", Integer.class);

    public final NumberPath<Integer> ordComplete = createNumber("ordComplete", Integer.class);

    public final NumberPath<Integer> ordCount = createNumber("ordCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> ordDate = createDateTime("ordDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> ordDelivery = createNumber("ordDelivery", Integer.class);

    public final NumberPath<Integer> ordDiscount = createNumber("ordDiscount", Integer.class);

    public final NumberPath<Integer> ordNo = createNumber("ordNo", Integer.class);

    public final NumberPath<Integer> ordPayment = createNumber("ordPayment", Integer.class);

    public final NumberPath<Integer> ordPrice = createNumber("ordPrice", Integer.class);

    public final NumberPath<Integer> ordTotPrice = createNumber("ordTotPrice", Integer.class);

    public final StringPath ordUid = createString("ordUid");

    public final StringPath ordUser = createString("ordUser");

    public final StringPath recipAddr1 = createString("recipAddr1");

    public final StringPath recipAddr2 = createString("recipAddr2");

    public final StringPath recipHp = createString("recipHp");

    public final StringPath recipName = createString("recipName");

    public final StringPath recipZip = createString("recipZip");

    public final NumberPath<Integer> savePoint = createNumber("savePoint", Integer.class);

    public final NumberPath<Integer> usedPoint = createNumber("usedPoint", Integer.class);

    public QOrder(String variable) {
        super(Order.class, forVariable(variable));
    }

    public QOrder(Path<? extends Order> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrder(PathMetadata metadata) {
        super(Order.class, metadata);
    }

}

