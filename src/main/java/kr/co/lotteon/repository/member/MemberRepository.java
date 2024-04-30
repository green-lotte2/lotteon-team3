package kr.co.lotteon.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.entity.member.Member;
import kr.co.lotteon.repository.custom.MemberRepositoryCustom;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email); // 중복 가입 확인

    Member findAllByEmail(String email);







}
