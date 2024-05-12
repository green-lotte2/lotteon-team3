package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.product.OptionDTO;
import kr.co.lotteon.entity.product.Option;
import kr.co.lotteon.entity.product.QOption;
import kr.co.lotteon.repository.custom.OptionRepositoryCustom;
import kr.co.lotteon.repository.product.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j @RequiredArgsConstructor @Repository
public class OptionRepositoryImpl implements OptionRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;
    private final QOption qOption = QOption.option;

    @Override
    public List<String> selectOpName(int prodNo) {

        return jpaQueryFactory.select(qOption.opName)
                .from(qOption)
                .where(qOption.prodNo.eq(prodNo))
                .groupBy(qOption.opName)
                .fetch();

    }

    // 상품 뷰 - 옵션을 맵형식으로 불러오기
    @Override
    public Map<String, List<String>> selectProdOption(int prodNo) {

        // SELECT opName, group_concat(opValue) from product_option where prodNo =? GROUP BY opName;
        List<Tuple> result = jpaQueryFactory
                .select(qOption.opName, Expressions.stringTemplate("GROUP_CONCAT({0})",qOption.opValue))
                .from(qOption)
                .where(qOption.prodNo.eq(prodNo))
                .groupBy(qOption.opName)
                .fetch();

        log.info("impl 1" + result);

        Map<String, List<String>> resultMap = new HashMap<>();

        for (Tuple tuple : result) {
            String opName = tuple.get(qOption.opName);
            log.info("opName : " + opName);
            String opValue = tuple.get(Expressions.stringTemplate("GROUP_CONCAT({0})", qOption.opValue));
            log.info("opValue : " + opValue);

            // opValue를 배열로 만들기
            List<String> opValueList = Arrays.asList(opValue.split(","));
            log.info("opValue List impl 2" + opValueList);

            // opName이 이미 resultMap에 존재하는지 확인, 없으면 빈 리스트를 새로 생성하여 추가
            resultMap.putIfAbsent(opName, new ArrayList<>());

            resultMap.put(opName, opValueList);
        }
        log.info("impl 3" + resultMap);

        return resultMap;
    }

    @Override
    public List<OptionDTO> selectOpvalueAndopNo(int prodNo, String opName) {
        List<Tuple> result = jpaQueryFactory.select(qOption.opValue, qOption.opNo)
                                    .from(qOption)
                                    .where(qOption.prodNo.eq(prodNo), qOption.opName.eq(opName))
                                    .fetch();

        log.info("impl : " + result);

        return result.stream()
                .map(tuple -> {
                    OptionDTO optionDTO = new OptionDTO();
                    optionDTO.setOpValue(tuple.get(qOption.opValue));
                    optionDTO.setOpNo(tuple.get(qOption.opNo));
                    return optionDTO;
                })
                .collect(Collectors.toList());
    }

    // 관리자 - 상품 수정 옵션 조회
    @Override
    public Map<String, List<Map<String, String>>> adminSelectProdOption(int prodNo) {

        // opName을 그룹화하고, opValue와 opNo를 '_'을 사이에 넣고 concat해서 조회
        // SELECT opName, group_concat(concat(opValue, '_', opNo)) from product_option where prodNo =? GROUP BY opName;
        List<Tuple> result = jpaQueryFactory
                .select(qOption.opName, Expressions.stringTemplate("GROUP_CONCAT(CONCAT({0}, '_', {1}))", qOption.opValue, qOption.opNo))
                .from(qOption)
                .where(qOption.prodNo.eq(prodNo))
                .groupBy(qOption.opName)
                .fetch();

        log.info("상품 수정 옵션  impl 1" + result);

        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();

        for (Tuple tuple : result) {
            String opName = tuple.get(qOption.opName);
            List<Map<String, String>> options = new ArrayList<>();
            String opValueOpNo = tuple.get(Expressions.stringTemplate("GROUP_CONCAT(CONCAT({0}, '_', {1}))", qOption.opValue, qOption.opNo));
            String[] opValueOpNoArray = opValueOpNo.split(",");

            for (String opValueOpNoItem : opValueOpNoArray) {
                String[] parts = opValueOpNoItem.split("_");
                Map<String, String> optionMap = new HashMap<>();
                optionMap.put("opValue", parts[0]);
                optionMap.put("opNo", parts[1]);
                options.add(optionMap);
            }

            resultMap.put(opName, options);
        }
        log.info("상품 수정 옵션 impl 3" + resultMap);

        return resultMap;
    }
    // 판매자 - 주문 현황 옵션 조회 by opNos
    @Override
    public List<Option> selectOptionByOpNos(List<Integer> opNos){
        return jpaQueryFactory
                .selectFrom(qOption)
                .where(qOption.opNo.in(opNos))
                .fetch();
    }

    @Override
    public OptionDTO selectOptionForCart(int opNo) {
        Option result = jpaQueryFactory.selectFrom(qOption)
                                            .where(qOption.opNo.eq(opNo))
                                            .fetchOne();

        log.info("옵션뽑아오기 임플 1: "+result);

        OptionDTO optionDTO = modelMapper.map(result, OptionDTO.class);
        log.info("옵션뽑아오기 임플 2: "+optionDTO);

        return optionDTO;
    }

}
