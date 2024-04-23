package kr.co.lotteon.repository.member;

import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.repository.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String>, MemberRepositoryCustom {

public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByEmail(String email); // 중복 가입 확인

}
