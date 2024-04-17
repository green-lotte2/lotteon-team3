package kr.co.lotteon.service.product;

import kr.co.lotteon.repository.product.Cate1Repository;
import kr.co.lotteon.repository.product.Cate2Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service @Slf4j @RequiredArgsConstructor
public class CateService {
    private final Cate1Repository cate1Repository;
    private final Cate2Repository cate2Repository;

    public String getc1Name(int cate1){
        return cate1Repository.findById(cate1).get().getC1Name();
    }

    public String getc2Name(int cate1, int cate2){
        return cate2Repository.findBycate1AndCate2(cate1, cate2).getC2Name();
    }
}
