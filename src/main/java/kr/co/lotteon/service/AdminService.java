package kr.co.lotteon.service;

import kr.co.lotteon.dto.product.Cate1DTO;
import kr.co.lotteon.dto.product.Cate2DTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Cate1;
import kr.co.lotteon.entity.product.Cate2;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.Cate1Repository;
import kr.co.lotteon.repository.Cate2Repository;
import kr.co.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final ProductRepository productRepository;
    private final Cate1Repository cate1Repository;
    private final Cate2Repository cate2Repository;
    private final ModelMapper modelMapper;

    @Value("${img.upload.path}")
    private String imgUploadPath;

    // 관리자 상품 등록 cate1 조회
    public List<Cate1DTO> findAllCate1(){
        List<Cate1> cate1s = cate1Repository.findAll();
        log.info("관리자 상품 등록 Serv : "+cate1s);
        // 조회된 Entity List -> DTO List
        return cate1s.stream().map(cate1 -> modelMapper.map(cate1, Cate1DTO.class))
                .collect(Collectors.toList());
    }
    // 관리자 상품 등록 cate2 조회
    public ResponseEntity<?> findAllCate2ByCate1(int cate1){
        // 조회된 Entity List -> DTO List
        List<Cate2DTO> cate2List = cate2Repository.findByCate1(cate1).stream()
                .map(cate2 -> modelMapper.map(cate2, Cate2DTO.class))
                .collect(Collectors.toList());;

        return ResponseEntity.ok().body(cate2List);
    }
    // 관리자 상품 등록 - DB insert
    public void insertProduct(ProductDTO productDTO, MultipartFile thumb190, MultipartFile thumb230, MultipartFile thumb456, MultipartFile detail860){

        log.info("관리자 상품 등록 service1 productDTO : " + productDTO.toString());
        log.info("관리자 상품 등록 service2 thumb190 : " + thumb190);
        log.info("관리자 상품 등록 service3 thumb230 : " + thumb230);
        log.info("관리자 상품 등록 service4 thumb456 : " + thumb456);
        log.info("관리자 상품 등록 service5 detail860 : " + detail860);

        // 이미지 파일 등록 : 해당 디렉토리 없을 경우 자동 생성
        File file = new File(imgUploadPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath();

        // 원본 파일 폴더 자동 생성
        String orgPath = path + "/orgImage";
        File orgFile = new File(orgPath);
        if(!orgFile.exists()){
            orgFile.mkdir();
        }
        // 저장
        Product saveProduct = new Product();

        // 이미지 리사이징
        if(!thumb190.isEmpty() && !thumb230.isEmpty() && !thumb456.isEmpty() && !detail860.isEmpty()){
            // oName, sName 구하기
            String oName190 = thumb190.getOriginalFilename();
            String oName230 = thumb230.getOriginalFilename();
            String oName456 = thumb456.getOriginalFilename();
            String oName860 = detail860.getOriginalFilename();

            String ext190 = oName190.substring(oName190.lastIndexOf("."));
            String ext230 = oName230.substring(oName230.lastIndexOf("."));
            String ext456 = oName456.substring(oName456.lastIndexOf("."));
            String ext860 = oName860.substring(oName860.lastIndexOf("."));

            String sName190 = UUID.randomUUID().toString() + ext190;
            String sName230 = UUID.randomUUID().toString() + ext230;
            String sName456 = UUID.randomUUID().toString() + ext456;
            String sName860 = UUID.randomUUID().toString() + ext860;

            log.info("관리자 상품 등록 service6 oName190 : " + oName190);
            log.info("관리자 상품 등록 service7 sName190 : " + sName190);

            try {
                // 원본 파일 저장
                thumb190.transferTo(new File(orgFile, sName190));
                thumb230.transferTo(new File(orgFile, sName230));
                thumb456.transferTo(new File(orgFile, sName456));
                detail860.transferTo(new File(orgFile, sName860));

                // 파일 이름 DTO에 저장
                productDTO.setThumb1(sName190);
                productDTO.setThumb2(sName230);
                productDTO.setThumb3(sName456);
                productDTO.setDetail(sName860);

                // 리사이징
                Thumbnails.of(new File(orgPath, sName190)) // 원본 파일 (경로, 이름)
                        .size(190,190) // 원하는 사이즈
                        .toFile(new File(path, sName190)); // 리사이징 파일 (경로, 이름)
                Thumbnails.of(new File(orgPath, sName230))
                        .size(230,230)
                        .toFile(new File(path, sName230));
                Thumbnails.of(new File(orgPath, sName456))
                        .size(456,456)
                        .toFile(new File(path, sName456));
                Thumbnails.of(new File(orgPath, sName860))
                        .width(860) // 너비 860 * 높이 제한 없음
                        .toFile(new File(path, sName860));
                log.info("리사이징 끝");

                // 상품 정보 DB 저장
                Product product = modelMapper.map(productDTO, Product.class);
                log.info("관리자 상품 등록 service8 product : " + product.toString());
                saveProduct = productRepository.save(product);
                log.info("관리자 상품 등록 service9 savedProduct : " + saveProduct.toString());

            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
