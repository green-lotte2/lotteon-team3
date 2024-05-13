package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.product.OptionDTO;
import kr.co.lotteon.entity.product.Option;

import java.util.List;
import java.util.Map;

public interface OptionRepositoryCustom {

    public List<String> selectOpName(int prodNo);

    // 뷰페이지 상품 옵션 띄우기
    public Map<String, List<String>> selectProdOption(int prodNo);

    public List<OptionDTO> selectOpvalueAndopNo(int prodNo, String opName);

    // 관리자 상품 수정 옵션 띄우기
    public Map<String, List<Map<String, String>>> adminSelectProdOption(int prodNo);

    public List<Option> selectOptionByOpNos(List<Integer> opNos);
    
    // 카트 옵션 가져오기
    public OptionDTO selectOptionForCart(int opNo);
}

