package kr.co.lotteon.service;

import kr.co.lotteon.entity.member.Terms;
import kr.co.lotteon.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TermsService {

    private final TermsRepository termsRepository;

    public Terms findByTerms(){
        return termsRepository.findById(1).get();
    }




}
