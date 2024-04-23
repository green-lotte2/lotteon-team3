package kr.co.lotteon.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = -1449304242L;

    public static final QBanner banner = new QBanner("banner");

    public final DateTimePath<java.util.Date> beginDate = createDateTime("beginDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> beginTime = createDateTime("beginTime", java.util.Date.class);

    public final StringPath bnName = createString("bnName");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final StringPath cate = createString("cate");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final DateTimePath<java.util.Date> endTime = createDateTime("endTime", java.util.Date.class);

    public final StringPath link = createString("link");

    public final StringPath thumb = createString("thumb");

    public QBanner(String variable) {
        super(Banner.class, forVariable(variable));
    }

    public QBanner(Path<? extends Banner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBanner(PathMetadata metadata) {
        super(Banner.class, metadata);
    }

}

