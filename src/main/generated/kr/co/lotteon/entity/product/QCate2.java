package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCate2 is a Querydsl query type for Cate2
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCate2 extends EntityPathBase<Cate2> {

    private static final long serialVersionUID = -556318527L;

    public static final QCate2 cate21 = new QCate2("cate21");

    public final StringPath c2Name = createString("c2Name");

    public final NumberPath<Integer> cate1 = createNumber("cate1", Integer.class);

    public final NumberPath<Integer> cate2 = createNumber("cate2", Integer.class);

    public QCate2(String variable) {
        super(Cate2.class, forVariable(variable));
    }

    public QCate2(Path<? extends Cate2> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCate2(PathMetadata metadata) {
        super(Cate2.class, metadata);
    }

}

