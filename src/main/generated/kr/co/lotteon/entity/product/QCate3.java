package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCate3 is a Querydsl query type for Cate3
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCate3 extends EntityPathBase<Cate3> {

    private static final long serialVersionUID = -556318526L;

    public static final QCate3 cate31 = new QCate3("cate31");

    public final StringPath c3Name = createString("c3Name");

    public final NumberPath<Integer> cate2 = createNumber("cate2", Integer.class);

    public final NumberPath<Integer> cate3 = createNumber("cate3", Integer.class);

    public QCate3(String variable) {
        super(Cate3.class, forVariable(variable));
    }

    public QCate3(Path<? extends Cate3> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCate3(PathMetadata metadata) {
        super(Cate3.class, metadata);
    }

}

