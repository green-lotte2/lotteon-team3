package kr.co.lotteon.service.admin;

import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.entity.admin.Banner;
import kr.co.lotteon.repository.admin.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;


    @Value("${img.upload.path}")
    private String imgUploadPath;

    // 활성화 상태인 배너 select
    public List<BannerDTO> selectBanners(String cate){
        List<Banner> banners = bannerRepository.selectByCateAndAct(cate);

        // List<Entity> -> List<DTO>
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    // 관리자 배너 목록
    public List<BannerDTO> bannerList(String cate) {

        // 조회된 Entity List -> DTO List
        return bannerRepository.findByCate(cate).stream()
                .map(banner ->
                        modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    // 관리자 배너 등록
    public void bannerRegister(MultipartFile imgFile, BannerDTO bannerDTO) {

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
        if (!imgFile.isEmpty()) {

            // oName, sName 구하기
            String oFile = imgFile.getOriginalFilename();
            String ext = oFile.substring(oFile.lastIndexOf("."));
            String sFile = UUID.randomUUID().toString() + ext;

            try {
                // 원본 파일 저장
                imgFile.transferTo(new File(orgFile, sFile));

                // 파일 이름 DTO에 저장
                bannerDTO.setThumb(sFile);

                // cate에 맞는 사이즈로 바꾸기
                switch (bannerDTO.getCate()) {
                    case "main-top":
                        // 리사이징 1200 * 80
                        Thumbnails.of(new File(orgPath, sFile)) // 원본 파일 (경로, 이름)
                                .size(1200, 80) // 원하는 사이즈
                                .toFile(new File(path, sFile)); // 리사이징 파일 (경로, 이름)
                        break;
                    case "main-slider":
                        // 리사이징 985 * 447
                        Thumbnails.of(new File(orgPath, sFile)) // 원본 파일 (경로, 이름)
                                .size(985, 447) // 원하는 사이즈
                                .toFile(new File(path, sFile)); // 리사이징 파일 (경로, 이름)
                        break;
                    case "product":
                        // 리사이징 456 * 60
                        Thumbnails.of(new File(orgPath, sFile)) // 원본 파일 (경로, 이름)
                                .size(456, 60) // 원하는 사이즈
                                .toFile(new File(path, sFile)); // 리사이징 파일 (경로, 이름)
                        break;
                    case "login":
                        // 리사이징 425 * 260
                        Thumbnails.of(new File(orgPath, sFile)) // 원본 파일 (경로, 이름)
                                .size(425, 260) // 원하는 사이즈
                                .toFile(new File(path, sFile)); // 리사이징 파일 (경로, 이름)
                        break;
                    case "myPage":
                        // 리사이징 810 * 86
                        Thumbnails.of(new File(orgPath, sFile)) // 원본 파일 (경로, 이름)
                                .size(810, 86) // 원하는 사이즈
                                .toFile(new File(path, sFile)); // 리사이징 파일 (경로, 이름)
                        break;
                    default:
                        break;
                }

                log.info("리사이징 끝");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // banner Table에 저장
        bannerRepository.save(modelMapper.map(bannerDTO, Banner.class));
    }

    // 관리자 배너 활성화 관리
    public ResponseEntity<?> bannerActChange(int bno) {
        Banner banner = bannerRepository.findById(bno).get();

        // 활성화 -> 비활성화
        if (banner.getActivation() == 1) {
            banner.setActivation(0);

            // 비활성화 -> 활성화
        } else if (banner.getActivation() == 0) {
            banner.setActivation(1);
        }

        bannerRepository.save(banner);

        return ResponseEntity.ok().body(banner);
    }

    // 관리자 배너 삭제
    public ResponseEntity<?> bannerDelete(int[] bnoArray) {
        log.info("관리자 배너 삭제 Serv 1 : " + Arrays.toString(bnoArray));

        for (int bno : bnoArray) {
            // 상품 배너 삭제 반복
            bannerRepository.deleteById(bno);
        }
        return ResponseEntity.ok().body("ok");
    }

}
