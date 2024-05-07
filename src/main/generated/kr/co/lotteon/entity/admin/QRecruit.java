package kr.co.lotteon.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecruit is a Querydsl query type for Recruit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecruit extends EntityPathBase<Recruit> {

    private static final long serialVersionUID = -559109284L;

    public static final QRecruit recruit = new QRecruit("recruit");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> employment = createNumber("employment", Integer.class);

    public final StringPath experience = createString("experience");

    public final StringPath occupation = createString("occupation");

    public final NumberPath<Integer> rno = createNumber("rno", Integer.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public final StringPath title = createString("title");

    public QRecruit(String variable) {
        super(Recruit.class, forVariable(variable));
    }

    public QRecruit(Path<? extends Recruit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecruit(PathMetadata metadata) {
        super(Recruit.class, metadata);
    }

}

