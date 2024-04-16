package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 367191322L;

    public static final QReview review = new QReview("review");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> ordItemno = createNumber("ordItemno", Integer.class);

    public final NumberPath<Integer> ordNo = createNumber("ordNo", Integer.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final StringPath regip = createString("regip");

    public final NumberPath<Integer> revNo = createNumber("revNo", Integer.class);

    public final NumberPath<Integer> thumb = createNumber("thumb", Integer.class);

    public final StringPath uid = createString("uid");

    public QReview(String variable) {
        super(Review.class, forVariable(variable));
    }

    public QReview(Path<? extends Review> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReview(PathMetadata metadata) {
        super(Review.class, metadata);
    }

}

