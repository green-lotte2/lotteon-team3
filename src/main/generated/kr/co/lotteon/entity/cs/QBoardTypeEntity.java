package kr.co.lotteon.entity.cs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardTypeEntity is a Querydsl query type for BoardTypeEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardTypeEntity extends EntityPathBase<BoardTypeEntity> {

    private static final long serialVersionUID = 1177836520L;

    public static final QBoardTypeEntity boardTypeEntity = new QBoardTypeEntity("boardTypeEntity");

    public final StringPath cate = createString("cate");

    public final StringPath typeName = createString("typeName");

    public final NumberPath<Integer> typeNo = createNumber("typeNo", Integer.class);

    public QBoardTypeEntity(String variable) {
        super(BoardTypeEntity.class, forVariable(variable));
    }

    public QBoardTypeEntity(Path<? extends BoardTypeEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardTypeEntity(PathMetadata metadata) {
        super(BoardTypeEntity.class, metadata);
    }

}

