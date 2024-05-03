package kr.co.lotteon.service.company;

import kr.co.lotteon.dto.admin.ArticleDTO;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final ArticleRepository articleRepository;
    private final ModelMapper modelMapper;
    // 회사소개 - 소식과 이야기 (9개) 리스트
    public List<ArticleDTO> selectStory(int start, String cate2){

        List<Article> articles = null;

        // 전체 조회
        if(cate2 == null || "".equals(cate2) || "all".equals(cate2)) {
            articles = articleRepository.selectStorys(start);
            log.info("소식과 이야기 (9개) 리스트 Serv 1 ");
            // 검색 조회
        }else{
            log.info("소식과 이야기 (9개) 리스트 Serv 2 ");
            articles = articleRepository.searchStorys(start, cate2);
        }
        return articles.stream()
                .map(entity -> modelMapper.map(entity, ArticleDTO.class))
                .toList();
    }
}
