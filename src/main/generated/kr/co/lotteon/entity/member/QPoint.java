package kr.co.lotteon.entity.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPoint is a Querydsl query type for Point
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPoint extends EntityPathBase<Point> {

    private static final long serialVersionUID = -802956897L;

    public static final QPoint point1 = new QPoint("point1");

    public final DateTimePath<java.time.LocalDateTime> currentDate = createDateTime("currentDate", java.time.LocalDateTime.class);

    public final StringPath descript = createString("descript");

    public final NumberPath<Integer> ordNo = createNumber("ordNo", Integer.class);

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> pointDate = createDateTime("pointDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> pointNo = createNumber("pointNo", Integer.class);

    public final StringPath uid = createString("uid");

    public final StringPath usecase = createString("usecase");

    public QPoint(String variable) {
        super(Point.class, forVariable(variable));
    }

    public QPoint(Path<? extends Point> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPoint(PathMetadata metadata) {
        super(Point.class, metadata);
    }

}

