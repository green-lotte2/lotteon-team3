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

    public final NumberPath<Integer> activation = createNumber("activation", Integer.class);

    public final StringPath backcolor = createString("backcolor");

    public final DatePath<java.time.LocalDate> beginDate = createDate("beginDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> beginTime = createTime("beginTime", java.time.LocalTime.class);

    public final StringPath bnName = createString("bnName");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final StringPath cate = createString("cate");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

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

