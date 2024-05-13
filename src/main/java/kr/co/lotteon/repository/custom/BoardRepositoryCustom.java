package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminBoardPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.entity.product.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    public List<Tuple> adminSelectBoards(String group);

    public Page<Tuple> selectBoardsByGroup(AdminBoardPageRequestDTO adminBoardPageRequestDTO, Pageable pageable, String group);

    public Page<Tuple> searchBoardsByGroup(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, String group);

    public Page<Tuple> searchBoardsByCate(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, String group, String cate);
    public Page<Tuple> selectQna(int prodNo, Pageable pageable);
    // 판매자 게시글 조회
    public Page<Tuple> selectBoardBySeller(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos );
    public Page<Tuple> searchBoardBySellerAndCate(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos, String cate);
    public Page<Tuple> searchBoardsBySellerAndKeyword(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, List<Integer> prodNos);
    public long countSellerQna(List<Integer> prodNos);
    // myInfo 문의내역 수 출력
    public int countByUidAndStatusIn (String uid, List<String> statusList);

    // 마이페이지 문의내역 조회
    public Page<Tuple> memberSelectBoards(String uid,CsPageRequestDTO csPageRequestDTO, Pageable pageable);

    // 마이페이지 메인 문의내역 최신 5개 조회
    public List<Tuple> selectReviewsByUidAndRdate(String uid);
}
