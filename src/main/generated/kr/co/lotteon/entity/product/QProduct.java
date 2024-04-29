package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1383569261L;

    public static final QProduct product = new QProduct("product");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final StringPath bizType = createString("bizType");

    public final NumberPath<Integer> cate1 = createNumber("cate1", Integer.class);

    public final NumberPath<Integer> cate2 = createNumber("cate2", Integer.class);

    public final NumberPath<Integer> cate3 = createNumber("cate3", Integer.class);

    public final StringPath company = createString("company");

    public final StringPath deleteYn = createString("deleteYn");

    public final NumberPath<Integer> delivery = createNumber("delivery", Integer.class);

    public final StringPath descript = createString("descript");

    public final StringPath detail = createString("detail");

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final StringPath duty = createString("duty");

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final StringPath ip = createString("ip");

    public final StringPath origin = createString("origin");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath prodName = createString("prodName");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> rdate = createDateTime("rdate", java.time.LocalDateTime.class);

    public final StringPath receipt = createString("receipt");

    public final NumberPath<Integer> review = createNumber("review", Integer.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final StringPath seller = createString("seller");

    public final NumberPath<Integer> sold = createNumber("sold", Integer.class);

    public final StringPath status = createString("status");

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public final StringPath thumb1 = createString("thumb1");

    public final StringPath thumb2 = createString("thumb2");

    public final StringPath thumb3 = createString("thumb3");

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

