package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    // 월별 가입 count 조회
    public List<Tuple> selectMemberForChart();

    // 회원 목록 기본 조회
    public Page<Member> selectMemberList(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);

    // 회원 목록 검색 조회
    public Page<Member> searchMemberList(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);
    // 판매자 목록 기본 조회
    public Page<Member> selectSellerList(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);

    // 판매자 목록 검색 조회
    public Page<Member> searchSellerList(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);

    // prodNo로 판매자 조회
    public String selectUidByProdNo(int prodNo);

    // 닉네임 조회
    public Optional<Member> selectMemberByUidAndNickname(String uid, String nick);

    // 이메일 조회
    public int countByUidAndEmail(String uid, String email);

    // 휴대폰 조회
    public int countByUidAndHp(String uid,String hp);

    // 판매자 정보 조회
    public Member selectSellerByCompany(String company);



}
