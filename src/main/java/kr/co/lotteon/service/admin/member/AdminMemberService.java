package kr.co.lotteon.service.admin.member;

import kr.co.lotteon.dto.admin.AdminMemberPageResponseDTO;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.member.MemberDTO;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    // 관리자 회원 목록 (현황) 조회
    public AdminMemberPageResponseDTO selectMembers(AdminPageRequestDTO adminPageRequestDTO) {
        log.info("관리자 회원 목록 Serv 1  ");
        Pageable pageable = adminPageRequestDTO.getPageable("no");

        Page<Member> members = null;
        // 회원 기본 조회
        if (adminPageRequestDTO.getKeyword() == null) {
            // DB 조회
            members = memberRepository.selectMemberList(adminPageRequestDTO, pageable);
            log.info("관리자 회원 기본 목록 Serv 2 : " + members);

            // 회원 검색 조회
        } else {
            // DB 조회
            members = memberRepository.searchMemberList(adminPageRequestDTO, pageable);
            log.info("관리자 회원 검색 목록 Serv 2 : " + members);
        }
        // Page<Entity>을 List<DTO>로 변환
        List<MemberDTO> dtoList = members.getContent().stream()
                .map(member -> modelMapper.map(member, MemberDTO.class))
                .toList();

        log.info("관리자 회원 목록 Serv 3 : " + dtoList);

        // total 값
        int total = (int) members.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return AdminMemberPageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 관리자 판매자 목록 (현황) 조회
    public AdminMemberPageResponseDTO selectSellers(AdminPageRequestDTO adminPageRequestDTO) {
        log.info("관리자 판매자 목록 Serv 1  ");
        Pageable pageable = adminPageRequestDTO.getPageable("no");

        Page<Member> members = null;
        // 회원 기본 조회
        if (adminPageRequestDTO.getKeyword() == null) {
            // DB 조회
            members = memberRepository.selectSellerList(adminPageRequestDTO, pageable);
            log.info("관리자 판매자 기본 목록 Serv 2 : " + members);

            // 회원 검색 조회
        } else {
            // DB 조회
            members = memberRepository.searchSellerList(adminPageRequestDTO, pageable);
            log.info("관리자 판매자 검색 목록 Serv 2 : " + members);
        }
        // Page<Entity>을 List<DTO>로 변환
        List<MemberDTO> dtoList = members.getContent().stream()
                .map(member -> modelMapper.map(member, MemberDTO.class))
                .toList();

        log.info("관리자 판매자 목록 Serv 3 : " + dtoList);

        // total 값
        int total = (int) members.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return AdminMemberPageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 관리자 회원 삭제
    public ResponseEntity<?> deleteMember(String uid){
        if(memberRepository.findById(uid).isPresent()) {
            memberRepository.deleteById(uid);
        }
        return ResponseEntity.ok().body("delete member");
    }

}
