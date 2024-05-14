package kr.co.lotteon.service.admin.company;

import kr.co.lotteon.dto.admin.AdminArticlePageResponseDTO;
import kr.co.lotteon.dto.admin.AdminPageRequestDTO;
import kr.co.lotteon.dto.admin.ArticleDTO;
import kr.co.lotteon.dto.company.RecruitDTO;
import kr.co.lotteon.dto.company.RecruitPageResponseDTO;
import kr.co.lotteon.entity.admin.Article;
import kr.co.lotteon.entity.admin.Recruit;
import kr.co.lotteon.repository.admin.ArticleRepository;
import kr.co.lotteon.repository.admin.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminCompanyService {

    private final ArticleRepository articleRepository;
    private final RecruitRepository recruitRepository;

    private final ModelMapper modelMapper;

    @Value("${img.upload.path}")
    private String imgUploadPath;

    ///////// 소식과 이야기 Article //////////////
    // 관리자 회사 소개 글 목록
    public AdminArticlePageResponseDTO selectArticle(String cate1, AdminPageRequestDTO adminPageRequestDTO){
        Pageable pageable = adminPageRequestDTO.getPageable("no");
        String keyword = adminPageRequestDTO.getKeyword();
        Page<Article> results = null;
        // 일반 조회일 때
        if(keyword == null || keyword.equals("") || keyword.equals("all")){
            results = articleRepository.selectArticleForAdmin(adminPageRequestDTO, pageable, cate1);

            // 검색 조회일 때
        }else {
            results = articleRepository.searchArticleForAdmin(adminPageRequestDTO, pageable, cate1);
        }

        List<ArticleDTO> dtoList = results.stream()
                .map(result -> modelMapper.map(result, ArticleDTO.class))
                .toList();

        // total 값
        int total = (int) results.getTotalElements();

        // List<OrderListDTO>와 page 정보 리턴
        return AdminArticlePageResponseDTO.builder()
                .adminPageRequestDTO(adminPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }
    // 관리자 회사소개 글 수정
    @Transactional
    public void modifyArticle(ArticleDTO articleDTO){

        Article article = articleRepository.findById(articleDTO.getAno()).get();

        article.setContent(articleDTO.getContent());
        article.setCate2(articleDTO.getCate2());

        articleRepository.save(article);

    }
    // 관리자 회사 소개 글쓰기
    public void insertArticle(MultipartFile thumb336, ArticleDTO articleDTO){
        log.info("회사 소개 글쓰기 Serv 1 : " + thumb336);
        log.info("회사 소개 글쓰기 Serv 2 : " + articleDTO);

        // 이미지 파일 등록 : 해당 디렉토리 없을 경우 자동 생성
        File file = new File(imgUploadPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath();

        // 원본 파일 폴더 자동 생성
        String orgPath = path + "/orgImage";
        File orgFile = new File(orgPath);
        if (!orgFile.exists()) {
            orgFile.mkdir();
        }
        // 이미지 리사이징
        if (!thumb336.isEmpty()) {
            // 리사이징 함수 호출 - 새 이미지 저장
            String sName = imgResizing(thumb336, orgPath, path, 380, 240);
            // 파일 이름 DTO에 저장
            articleDTO.setThumb(sName);
        }

        // DTO -> Entity
        Article article = modelMapper.map(articleDTO, Article.class);
        log.info("회사 소개 글쓰기 Serv 3 : " + article.toString());

        // 상품 정보 DB 저장
        articleRepository.save(article);
    }

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

    /////  이미지 리사이징 //////////////////////////////////////////////////
    public String imgResizing(MultipartFile file, String orgPath, String path, int targetWidth, int targetHeight) {
        String oName = file.getOriginalFilename();
        String ext = oName.substring(oName.lastIndexOf("."));

        String sName = UUID.randomUUID().toString() + ext;

        try {
            // 원본 파일 저장
            file.transferTo(new File(orgPath, sName));

            // 리사이징
            Thumbnails.of(new File(orgPath, sName))
                    .size(targetWidth, targetHeight)
                    .toFile(new File(path, sName));

            log.info("이미지 리사이징 완료: " + sName);

            return sName;
        } catch (IOException e) {
            log.error("이미지 리사이징 실패: " + e.getMessage());
            return null;
        }
    }

    // 이미지 리사이징 함수 - width만
    public String imgResizing(MultipartFile file, String orgPath, String path, int targetWidth) {
        String oName = file.getOriginalFilename();
        String ext = oName.substring(oName.lastIndexOf("."));

        String sName = UUID.randomUUID().toString() + ext;

        try {
            // 원본 파일 저장
            file.transferTo(new File(orgPath, sName));

            // 리사이징
            Thumbnails.of(new File(orgPath, sName))
                    .width(targetWidth)
                    .toFile(new File(path, sName));

            log.info("이미지 리사이징 완료: " + sName);

            return sName;
        } catch (IOException e) {
            log.error("이미지 리사이징 실패: " + e.getMessage());
            return null;
        }
    }
}
