package kr.co.lotteon.service.company;

import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.admin.ArticleDTO;
import kr.co.lotteon.dto.company.RecruitDTO;
import kr.co.lotteon.dto.company.RecruitPageResponseDTO;
import kr.co.lotteon.dto.company.StoryPageRequestDTO;
import kr.co.lotteon.dto.company.StoryPageResponseDTO;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.admin.Recruit;
import kr.co.lotteon.repository.admin.ArticleRepository;
import kr.co.lotteon.repository.admin.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final ArticleRepository articleRepository;
    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper;

    // 회사소개 - 소식과 이야기 (9개) 리스트
    public StoryPageResponseDTO selectStory(StoryPageRequestDTO storyPageRequestDTO){
        String cate2 = storyPageRequestDTO.getCate2();
        Pageable pageable = storyPageRequestDTO.getPageable("no");
        Page<Article> articles = null;

        // 전체 조회
        if(cate2 == null || "".equals(cate2) || "all".equals(cate2)) {
            articles = articleRepository.selectStories(storyPageRequestDTO, pageable);
            log.info("소식과 이야기 (9개) 리스트 Serv 1 ");
            // 검색 조회
        }else{
            log.info("소식과 이야기 (9개) 리스트 Serv 2 ");
            articles = articleRepository.searchStories(storyPageRequestDTO, pageable);
        }
        List<ArticleDTO> dtoList = articles.stream()
                .map(entity -> modelMapper.map(entity, ArticleDTO.class))
                .toList();


        // total 값
        int total = (int) articles.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return StoryPageResponseDTO.builder()
                .storyPageRequestDTO(storyPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 관리자 회사 소개 채용 글 목록
    public RecruitPageResponseDTO selectRecruit(AdminPageRequestDTO adminPageRequestDTO, RecruitDTO recruitDTO){
        Pageable pageable = adminPageRequestDTO.getPageable("no");
        String keyword = adminPageRequestDTO.getKeyword();
        Page<Recruit> results = null;
        // 전체 조회이면
        if(recruitDTO.getEmployment() == 8 && recruitDTO.getStatus() == 8){
            results = recruitRepository.selectRecruitForAdmin(adminPageRequestDTO, pageable);
            // 검색
        }else{
            results = recruitRepository.searchRecruitForAdmin(adminPageRequestDTO, pageable, recruitDTO);
        }

        List<RecruitDTO> dtoList = results.stream()
                .map(result -> modelMapper.map(result, RecruitDTO.class))
                .toList();

        // total 값
        int total = (int) results.getTotalElements();

        // List<OrderListDTO>와 page 정보 리턴
        return RecruitPageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
}
