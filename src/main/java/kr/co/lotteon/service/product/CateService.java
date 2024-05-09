package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.product.Cate1DTO;
import kr.co.lotteon.dto.product.Cate2DTO;
import kr.co.lotteon.dto.product.Cate3DTO;
import kr.co.lotteon.entity.product.Cate1;
import kr.co.lotteon.entity.product.Cate2;
import kr.co.lotteon.entity.product.Cate3;
import kr.co.lotteon.repository.product.Cate1Repository;
import kr.co.lotteon.repository.product.Cate2Repository;
import kr.co.lotteon.repository.product.Cate3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.inject.Qualifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j @RequiredArgsConstructor
public class CateService {
    private final Cate1Repository cate1Repository;
    private final Cate2Repository cate2Repository;
    private final Cate3Repository cate3Repository;
    private final ModelMapper modelMapper;

    // 현재 카테 값만 불러오기
    public String getc1Name(int cate1){
        return cate1Repository.findById(cate1).get().getC1Name();
    }

    public String getc2Name(int cate1, int cate2){
        Cate2 cate2Entity = cate2Repository.findBycate1AndCate2(cate1, cate2);
        if(cate2Entity != null){
            return cate2Entity.getC2Name();
        }else{
            return null;
        }
    }

    public String getc3Name(int cate2, int cate3){
        Cate3 cate3Entity = cate3Repository.findByCate2AndCate3(cate2, cate3);
        if(cate3Entity != null){
            return cate3Entity.getC3Name();
        }else{
            return null;
        }
    }

    // cate1 리스트 불러오기
    @Cacheable(cacheNames = "cate1Cache", cacheManager = "cacheManager1")
    public List<Cate1DTO> getCate1List(){
        log.info("getCate1List...");

        List<Cate1> result = cate1Repository.findAll();

        return result.stream()
                .map(cate1 -> modelMapper.map(cate1, Cate1DTO.class))
                .collect(Collectors.toList());
    }
    // cate2 리스트 불러오기
    @Cacheable(cacheNames = "cate2Cache", cacheManager = "cacheManager2")
    public List<Cate2DTO> getCate2List(){
        log.info("getCate2List...");
        List<Cate2> result = cate2Repository.findAll();

        return result.stream()
                .map(cate2 -> modelMapper.map(cate2, Cate2DTO.class))
                .collect(Collectors.toList());
    }
    // cate3 리스트 불러오기
    @Cacheable(cacheNames = "cate3Cache", cacheManager = "cacheManager3")
    public List<Cate3DTO> getCate3List(){
        log.info("getCate3List...");
        List<Cate3> result = cate3Repository.findAll();

        return result.stream()
                .map(cate3 -> modelMapper.map(cate3, Cate3DTO.class))
                .collect(Collectors.toList());
    }
}
