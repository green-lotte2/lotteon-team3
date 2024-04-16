package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCate1 is a Querydsl query type for Cate1
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCate1 extends EntityPathBase<Cate1> {

    private static final long serialVersionUID = -556318528L;

    public static final QCate1 cate11 = new QCate1("cate11");

    public final StringPath c1Name = createString("c1Name");

    public final NumberPath<Integer> cate1 = createNumber("cate1", Integer.class);

    public final StringPath css = createString("css");

    public QCate1(String variable) {
        super(Cate1.class, forVariable(variable));
    }

    public QCate1(Path<? extends Cate1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCate1(PathMetadata metadata) {
        super(Cate1.class, metadata);
    }

}

