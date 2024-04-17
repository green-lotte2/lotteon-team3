package kr.co.lotteon.service.member;

import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.repository.member.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TermsService {

    private final TermsRepository termsRepository;

    //Id(고객 타입) 값으로 DB에 있는 terms 가져오기
    public Terms findByTerms(){
        return termsRepository.findById(1).get();
    }




}
