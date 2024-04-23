package kr.co.lotteon.repository.member;

import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.repository.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,String>, MemberRepositoryCustom {


}
