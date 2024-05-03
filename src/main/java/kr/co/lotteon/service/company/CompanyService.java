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
    public List<ArticleDTO> selectStory(int start){

        List<Article> articles =  articleRepository.selectStorys(start);
        return articles.stream()
                .map(entity -> modelMapper.map(entity, ArticleDTO.class))
                .toList();
    }
}
