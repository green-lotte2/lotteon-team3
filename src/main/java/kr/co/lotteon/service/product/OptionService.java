package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.product.OptionDTO;
import kr.co.lotteon.entity.product.Option;
import kr.co.lotteon.repository.product.OptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor @Slf4j
public class OptionService {
    private final OptionRepository optionRepository;

    // 옵션 네임 가져오기
    public List<String> selectOpName(int prodNo){
        return optionRepository.selectOpName(prodNo);
    }

    // 옵션 불러오기
    public Map<String, List<String>> selectProdOption(int prodNo){
        return optionRepository.selectProdOption(prodNo);
    }

    // 옵션 밸류, 번호 가져오기
    public ResponseEntity<?> selectOpDetail(int prodNo, String opName){
        List<OptionDTO> result = optionRepository.selectOpvalueAndopNo(prodNo, opName);

        if(result != null || !result.isEmpty()){
            log.info("optionservice 1 : "+result.toString());

            return ResponseEntity.status(HttpStatus.OK).body(result);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }
    }
}
