package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminBoardPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    public List<Tuple> adminSelectBoards(String group);

    public Page<Tuple> selectBoardsByGroup(AdminBoardPageRequestDTO adminBoardPageRequestDTO, Pageable pageable, String group);

    public Page<Tuple> searchBoardsByGroup(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, String group);

    public Page<Tuple> searchBoardsByCate(AdminBoardPageRequestDTO pageRequestDTO, Pageable pageable, String group, String cate);

}
