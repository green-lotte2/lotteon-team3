package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderItem is a Querydsl query type for OrderItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderItem extends EntityPathBase<OrderItem> {

    private static final long serialVersionUID = -1169866113L;

    public static final QOrderItem orderItem = new QOrderItem("orderItem");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final StringPath opNo = createString("opNo");

    public final DateTimePath<java.time.LocalDateTime> ordDate = createDateTime("ordDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> ordItemno = createNumber("ordItemno", Integer.class);

    public final NumberPath<Integer> ordNo = createNumber("ordNo", Integer.class);

    public final StringPath ordStatus = createString("ordStatus");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final StringPath uid = createString("uid");

    public QOrderItem(String variable) {
        super(OrderItem.class, forVariable(variable));
    }

    public QOrderItem(Path<? extends OrderItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderItem(PathMetadata metadata) {
        super(OrderItem.class, metadata);
    }

}

