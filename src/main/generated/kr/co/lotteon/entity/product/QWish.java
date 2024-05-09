package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWish is a Querydsl query type for Wish
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWish extends EntityPathBase<Wish> {

    private static final long serialVersionUID = 1645225705L;

    public static final QWish wish = new QWish("wish");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final StringPath uid = createString("uid");

    public final NumberPath<Integer> wishNo = createNumber("wishNo", Integer.class);

    public QWish(String variable) {
        super(Wish.class, forVariable(variable));
    }

    public QWish(Path<? extends Wish> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWish(PathMetadata metadata) {
        super(Wish.class, metadata);
    }

}

