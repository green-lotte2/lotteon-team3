package kr.co.lotteon.entity.cs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardCateEntity is a Querydsl query type for BoardCateEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardCateEntity extends EntityPathBase<BoardCateEntity> {

    private static final long serialVersionUID = -965860291L;

    public static final QBoardCateEntity boardCateEntity = new QBoardCateEntity("boardCateEntity");

    public final StringPath cate = createString("cate");

    public final StringPath cateName = createString("cateName");

    public QBoardCateEntity(String variable) {
        super(BoardCateEntity.class, forVariable(variable));
    }

    public QBoardCateEntity(Path<? extends BoardCateEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardCateEntity(PathMetadata metadata) {
        super(BoardCateEntity.class, metadata);
    }

}

