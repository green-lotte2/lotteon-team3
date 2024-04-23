package kr.co.lotteon.entity.cs;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardFileEntity is a Querydsl query type for BoardFileEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardFileEntity extends EntityPathBase<BoardFileEntity> {

    private static final long serialVersionUID = 107251882L;

    public static final QBoardFileEntity boardFileEntity = new QBoardFileEntity("boardFileEntity");

    public final NumberPath<Integer> bno = createNumber("bno", Integer.class);

    public final NumberPath<Integer> download = createNumber("download", Integer.class);

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Integer> fno = createNumber("fno", Integer.class);

    public final StringPath ofile = createString("ofile");

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final StringPath sfile = createString("sfile");

    public QBoardFileEntity(String variable) {
        super(BoardFileEntity.class, forVariable(variable));
    }

    public QBoardFileEntity(Path<? extends BoardFileEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardFileEntity(PathMetadata metadata) {
        super(BoardFileEntity.class, metadata);
    }

}

