package kr.co.lotteon.service.admin.company;

import kr.co.lotteon.dto.admin.ArticleDTO;
import kr.co.lotteon.dto.company.RecruitDTO;
import kr.co.lotteon.entity.admin.Recruit;
import kr.co.lotteon.repository.ArticleRepository;
import kr.co.lotteon.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminCompanyService {

    private final ArticleRepository articleRepository;
    private final RecruitRepository recruitRepository;

    private ModelMapper modelMapper;

    ///////// 소식과 이야기 Article //////////////


    // 관리자 회사소개 삭제
    public ResponseEntity<?> deleteArticle(int ano){
        articleRepository.deleteById(ano);
        return ResponseEntity.ok().body("delete article");
    }
    // 회사소개 게시글 상세 조회
    public ArticleDTO selectArticle(int ano){
        return modelMapper.map(articleRepository.findById(ano), ArticleDTO.class);
    }

    ///////// 채용 Recruit //////////////////////
    // 관리자 회사소개 채용 글쓰기
    public void recruitPost(RecruitDTO recruitDTO){
        Recruit recruit = modelMapper.map(recruitDTO, Recruit.class);
        recruitRepository.save(recruit);
        log.info("글쓰기 후 recruit : "+ recruit.getRno());
    }
    // 관리자 회사소개 채용 수정
    @Transactional
    public ResponseEntity<?> recruitUpdate(RecruitDTO recruitDTO){
        Recruit recruit = recruitRepository.findById(recruitDTO.getRno()).get();
        recruit.setEmployment(recruitDTO.getEmployment());
        recruit.setStatus(recruitDTO.getStatus());
        recruitRepository.save(recruit);
        return ResponseEntity.ok().body(recruit);
    }
    // 관리자 회사소개 채용 삭제
    public ResponseEntity<?> recruitDelete(int rno){
        recruitRepository.deleteById(rno);
        return ResponseEntity.ok().body("delete");
    }
}
