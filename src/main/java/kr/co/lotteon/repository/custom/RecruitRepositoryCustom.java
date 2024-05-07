package kr.co.lotteon.repository.custom;

import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.company.StoryPageRequestDTO;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.admin.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitRepositoryCustom {

    public Page<Recruit> selectRecruitForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);

    public Page<Recruit> searchRecruitForAdmin(AdminPageRequestDTO adminPageRequestDTO, Pageable pageable);
}
