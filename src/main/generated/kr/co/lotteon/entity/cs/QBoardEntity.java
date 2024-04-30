package kr.co.lotteon.entity.cs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardEntity is a Querydsl query type for BoardEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardEntity extends EntityPathBase<BoardEntity> {

    private static final long serialVersionUID = 847479182L;

    public static final QBoardEntity boardEntity = new QBoardEntity("boardEntity");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final StringPath cate = createString("cate");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> file = createNumber("file", Integer.class);

    public final StringPath group = createString("group");

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> reply = createNumber("reply", Integer.class);

    public final StringPath status = createString("status");

    public final StringPath title = createString("title");

    public final NumberPath<Integer> typeNo = createNumber("typeNo", Integer.class);

    public final StringPath uid = createString("uid");

    public QBoardEntity(String variable) {
        super(BoardEntity.class, forVariable(variable));
    }

    public QBoardEntity(Path<? extends BoardEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardEntity(PathMetadata metadata) {
        super(BoardEntity.class, metadata);
    }

}

