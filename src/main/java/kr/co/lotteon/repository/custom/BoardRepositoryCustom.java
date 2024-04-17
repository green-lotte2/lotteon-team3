package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.product.AdminPageRequestDTO;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    public List<Tuple> adminSelectBoards(String group);

    public Page<BoardEntity> selectBoardsByGroup(String group);

}
