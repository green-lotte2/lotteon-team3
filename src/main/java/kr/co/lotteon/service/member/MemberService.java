package kr.co.lotteon.service.member;


import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    
    // 회원 가입 - DB 입력
    public void save(MemberDTO memberDTO){
        // 비밀번호 암호화
        memberDTO.setPass1(passwordEncoder.encode(memberDTO.getPass1())); 
        Member member = modelMapper.map(memberDTO, Member.class);
        memberRepository.save(member); 
    }
}
