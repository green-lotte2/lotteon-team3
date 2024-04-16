package kr.co.lotteon.repository.member;

import kr.co.lotteon.entity.member.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsRepository extends JpaRepository<Terms,Integer> {
}
