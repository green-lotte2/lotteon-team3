package kr.co.lotteon.service.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.lotteon.dto.admin.AdminProductPageRequestDTO;
import kr.co.lotteon.dto.admin.AdminProductPageResponseDTO;
import kr.co.lotteon.dto.product.ColorDTO;
import kr.co.lotteon.dto.product.OptionDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.product.Cate1;
import kr.co.lotteon.entity.product.Option;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.mapper.ProductMapper;
import kr.co.lotteon.repository.product.Cate1Repository;
import kr.co.lotteon.repository.product.OptionRepository;
import kr.co.lotteon.repository.product.ProductRepository;
import kr.co.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminProductService {

    private final ProductRepository productRepository;
    private final Cate1Repository cate1Repository;
    private final OptionRepository optionRepository;
    private final ProductMapper productMapper;

    private final ModelMapper modelMapper;


    @Value("${img.upload.path}")
    private String imgUploadPath;

    // 관리자 상품 기본 목록 조회
    public AdminProductPageResponseDTO adminSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO) {
        log.info("관리자 상품 목록 조회 Serv 1 : " + adminProductPageRequestDTO);

        Pageable pageable = adminProductPageRequestDTO.getPageable("no");

        // DB 조회
        Page<Product> pageProducts = productRepository.adminSelectProducts(adminProductPageRequestDTO, pageable);
        log.info("관리자 상품 목록 조회 Serv 2 : " + pageProducts);

        // Page<Product>을 List<ProductDTO>로 변환
        List<ProductDTO> dtoList = pageProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        log.info("관리자 상품 목록 조회 Serv 3 : " + dtoList);

        // total 값
        int total = (int) pageProducts.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return AdminProductPageResponseDTO.builder()
                .adminProductPageRequestDTO(adminProductPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();

    }
    // 관리자 상품 검색 목록 조회
    public AdminProductPageResponseDTO adminSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO) {
        log.info("관리자 상품 목록 검색 조회 Serv 1 : " + adminProductPageRequestDTO);
        Pageable pageable = adminProductPageRequestDTO.getPageable("no");

        // DB 조회
        Page<Product> pageProducts = productRepository.adminSearchProducts(adminProductPageRequestDTO, pageable);
        log.info("관리자 상품 목록 검색 조회 Serv 2 : " + pageProducts);

        // Page<Product>을 List<ProductDTO>로 변환
        List<ProductDTO> dtoList = pageProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        log.info("관리자 상품 목록 검색 조회 Serv 3 : " + dtoList);

        // total 값
        int total = (int) pageProducts.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return AdminProductPageResponseDTO.builder()
                .adminProductPageRequestDTO(adminProductPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();

    }
    // 상품 보기
    public ProductDTO selectByprodNo(int prodNo){
        Product product = productRepository.findById(prodNo).get();
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }

    // 관리자 상품 삭제 - json (상품 수정)
    public void prodArrDelete(String prodNoList) {

        // JON 문자열 파싱 -> int 배열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Integer[] prodNoArray = null;
        try {
            prodNoArray = objectMapper.readValue(prodNoList, Integer[].class);
            log.info("관리자 상품 삭제 1 - prodNoArray : " + prodNoArray.toString());

            for (int prodNo : prodNoArray) {
                // 상품 삭제 반복
                log.info("관리자 상품 삭제 2 - prodNo : " + prodNo);
                productRepository.deleteById(prodNo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 관리자 상품 상태 변경
    public ResponseEntity<?> prodStatUpdate(int prodNo, int stat){
        String status = "";

        switch (stat){
            case 0 :
                status = "새상품";
                break;
            case 1 :
                status = "품절";
                break;
            case 2 :
                status = "삭제된 상품";
                break;
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProdNo(prodNo);
        productDTO.setStatus(status);
        productMapper.updateStatusByProdNo(prodNo, status);

        return ResponseEntity.ok().body(productDTO);
    }
    // 관리자 상품 수정 - DB insert
    @Transient
    public ProductDTO modifyProduct(String optionDTOListJson,
                                    ProductDTO productDTO,
                                    MultipartFile thumb190,
                                    MultipartFile thumb230,
                                    MultipartFile thumb456,
                                    MultipartFile detail860) {

        log.info("관리자 상품 수정 service1 productDTO : " + productDTO.toString());
        log.info("관리자 상품 수정 service2 thumb190 : " + thumb190);
        log.info("관리자 상품 수정 service3 thumb230 : " + thumb230);
        log.info("관리자 상품 수정 service4 thumb456 : " + thumb456);
        log.info("관리자 상품 수정 service5 detail860 : " + detail860);
        log.info("관리자 상품 수정 service6 json : " + optionDTOListJson);

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
        Product saveProduct = new Product();

        // 기존 이미지를 삭제 했다면
        if (productDTO.getThumb1() == null || productDTO.getThumb1().isEmpty()) {
            // 리사이징 함수 호출 - 새 이미지 저장
            String sName190 = imgResizing(thumb190, orgPath, path, 190, 190);
            // 파일 이름 DTO에 저장
            productDTO.setThumb1(sName190);
        }
        if (productDTO.getThumb2() == null || productDTO.getThumb2().isEmpty()) {
            String sName230 = imgResizing(thumb230, orgPath, path, 230, 230);
            productDTO.setThumb2(sName230);
        }
        if (productDTO.getThumb3() == null || productDTO.getThumb3().isEmpty()) {
            String sName456 = imgResizing(thumb456, orgPath, path, 456, 456);
            productDTO.setThumb3(sName456);
        }
        if (productDTO.getDetail() == null || productDTO.getDetail().isEmpty()) {
            String sName860 = imgResizing(detail860, orgPath, path, 860);
            productDTO.setDetail(sName860);
        }
        // update 마이 바티스 써야함
        productMapper.updateProductByProdNo(productDTO);

        // JON 문자열 파싱 -> OptionDTO 리스트로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<ColorDTO> optionDTOList = null;
        try {
            ColorDTO[] optionDTOArray = objectMapper.readValue(optionDTOListJson, ColorDTO[].class);
            optionDTOList = Arrays.asList(optionDTOArray);
            log.info(optionDTOList.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // option 정보 Entity에 입력 후 DB 저장
        if (optionDTOList != null) {
            for (ColorDTO option : optionDTOList) {

                // DTO -> Entity : Entity의 영속성 때문에 매번 새로 생성해야함
                Product product = modelMapper.map(productDTO, Product.class);
                log.info("관리자 상품 수정 service8 product : " + product.toString());

                // 옵션 정보 Product Entity에 저장
                log.info("관리자 상품 수정 service9 " + option);

                product.setThumb1(productDTO.getThumb1());
                product.setThumb2(productDTO.getThumb2());
                product.setThumb3(productDTO.getThumb3());
                product.setDetail(productDTO.getDetail());
                log.info("optionDTO List : " + option);
                log.info("option 정보 저장 setThumb2 : " + productDTO.getThumb2());

                // 상품 정보 DB 저장
                saveProduct = productRepository.save(product);
                log.info("관리자 상품 수정 service10 savedProduct : " + saveProduct.toString());
            }
            // option 없는 경우
        } else {
            // DTO -> Entity
            Product product = modelMapper.map(productDTO, Product.class);
            log.info("관리자 상품 등록 service8 product : " + product.toString());
            log.info("관리자 상품 수정 service8 product : " + product.toString());
            // 상품 정보 DB 저장
            saveProduct = productRepository.save(product);
            log.info("관리자 상품 수정 service10 savedProduct : " + saveProduct.toString());

        }
        return modelMapper.map(saveProduct, ProductDTO.class);
    }
    // 관리자 상품 관리 - 등록한 상품 출력
    public ProductDTO prodView(int prodNo) {
        return modelMapper.map(productRepository.findById(prodNo), ProductDTO.class);
    }
    /////  판매자 //////////////////////////////////////////////////

    // 판매자 상품 기본 목록 조회
    public AdminProductPageResponseDTO sellerSelectProducts(AdminProductPageRequestDTO adminProductPageRequestDTO) {
        log.info("판매자 상품 목록 조회 Serv 1 : " + adminProductPageRequestDTO);
        String sellerId = whoAmI();

        Pageable pageable = adminProductPageRequestDTO.getPageable("no");

        // DB 조회
        Page<Product> pageProducts = productRepository.sellerSelectProducts(adminProductPageRequestDTO, pageable, sellerId);
        log.info("판매자 상품 목록 조회 Serv 2 : " + pageProducts);

        // Page<Product>을 List<ProductDTO>로 변환
        List<ProductDTO> dtoList = pageProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        log.info("판매자 상품 목록 조회 Serv 3 : " + dtoList);

        // total 값
        int total = (int) pageProducts.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return AdminProductPageResponseDTO.builder()
                .adminProductPageRequestDTO(adminProductPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();

    }

    // 판매자 상품 목록 검색 - cate1을 type으로 선택 시 cate1 조회
    public ResponseEntity<?> findCate1s() {
        List<Cate1> cate1s = cate1Repository.findAll();
        return ResponseEntity.ok().body(cate1s);
    }

    // 판매자 상품 검색 목록 조회
    public AdminProductPageResponseDTO sellerSearchProducts(AdminProductPageRequestDTO adminProductPageRequestDTO) {
        log.info("판매자 상품 목록 검색 조회 Serv 1 : " + adminProductPageRequestDTO);
        Pageable pageable = adminProductPageRequestDTO.getPageable("no");

        String sellerId = whoAmI();

        // DB 조회
        Page<Product> pageProducts = productRepository.sellerSearchProducts(adminProductPageRequestDTO, pageable, sellerId);
        log.info("판매자 상품 목록 검색 조회 Serv 2 : " + pageProducts);

        // Page<Product>을 List<ProductDTO>로 변환
        List<ProductDTO> dtoList = pageProducts.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        log.info("판매자 상품 목록 검색 조회 Serv 3 : " + dtoList);

        // total 값
        int total = (int) pageProducts.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return AdminProductPageResponseDTO.builder()
                .adminProductPageRequestDTO(adminProductPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();

    }

    // 판매자 상품 등록 - DB insert
    @Transient
    public ProductDTO insertProduct(ProductDTO productDTO,
                                    MultipartFile thumb190,
                                    MultipartFile thumb230,
                                    MultipartFile thumb456,
                                    MultipartFile detail860) {

        log.info("판매자 상품 등록 service1 productDTO : " + productDTO.toString());
        log.info("판매자 상품 등록 service2 thumb190 : " + thumb190);
        log.info("판매자 상품 등록 service3 thumb230 : " + thumb230);
        log.info("판매자 상품 등록 service4 thumb456 : " + thumb456);
        log.info("판매자 상품 등록 service5 detail860 : " + detail860);

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
        if (!thumb190.isEmpty()) {
            // 리사이징 함수 호출 - 새 이미지 저장
            String sName190 = imgResizing(thumb190, orgPath, path, 190, 190);
            // 파일 이름 DTO에 저장
            productDTO.setThumb1(sName190);
        }
        if (!thumb230.isEmpty()) {
            String sName230 = imgResizing(thumb230, orgPath, path, 230, 230);
            productDTO.setThumb2(sName230);
        }
        if (!thumb456.isEmpty()) {
            String sName456 = imgResizing(thumb456, orgPath, path, 456, 456);
            productDTO.setThumb3(sName456);
        }
        if (!detail860.isEmpty()) {
            String sName860 = imgResizing(detail860, orgPath, path, 860);
            productDTO.setDetail(sName860);
        }

        // DTO -> Entity
        Product product = modelMapper.map(productDTO, Product.class);
        log.info("판매자 상품 등록 service8 product : " + product.toString());

        // 상품 정보 DB 저장
        Product saveProduct = productRepository.save(product);
        log.info("판매자 상품 등록 service10 savedProduct : " + saveProduct.toString());

        return modelMapper.map(saveProduct, ProductDTO.class);
    }

    // 판매자 상품 수정 - 옵션 조회
    public Map<String, List<Map<String, String>>> selectProdOption(int prodNo){
        return optionRepository.adminSelectProdOption(prodNo);
    }

    // 판매자 상품 수정 - DB insert
    @Transient
    public void modifyProduct(ProductDTO productDTO,
                              MultipartFile thumb190,
                              MultipartFile thumb230,
                              MultipartFile thumb456,
                              MultipartFile detail860) {

        log.info("판매자 상품 수정 service1 productDTO : " + productDTO.toString());
        log.info("판매자 상품 수정 service2 thumb190 : " + thumb190);
        log.info("판매자 상품 수정 service3 thumb230 : " + thumb230);
        log.info("판매자 상품 수정 service4 thumb456 : " + thumb456);
        log.info("판매자 상품 수정 service5 detail860 : " + detail860);

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

        // 기존 이미지를 삭제 했다면
        if (productDTO.getThumb1() == null) {
            // 리사이징 함수 호출 - 새 이미지 저장
            String sName190 = imgResizing(thumb190, orgPath, path, 190, 190);
            // 파일 이름 DTO에 저장
            productDTO.setThumb1(sName190);
        }
        if (productDTO.getThumb2() == null) {
            String sName230 = imgResizing(thumb230, orgPath, path, 230, 230);
            productDTO.setThumb2(sName230);
        }
        if (productDTO.getThumb3() == null) {
            String sName456 = imgResizing(thumb456, orgPath, path, 456, 456);
            productDTO.setThumb3(sName456);
        }
        if (productDTO.getDetail() == null) {
            String sName860 = imgResizing(detail860, orgPath, path, 860);
            productDTO.setDetail(sName860);
        }
        log.info("판매자 상품 수정 service10 savedProduct : " + productDTO.toString());
        // 상품 정보 DB 저장
        productMapper.updateProductByProdNo(productDTO);
    }

    // 판매자 상품 수정 - 옵션 삭제
    public void deleteOptions(String opNoList){

        // JON 문자열 파싱 -> int 배열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Integer[] opNoArray = null;

        try {
            opNoArray = objectMapper.readValue(opNoList, Integer[].class);
            log.info("판매자 상품 수정 - 옵션 삭제 - opNoArray : " + opNoArray.toString());

            for (int opNo : opNoArray) {
                // 옵션 삭제 반복
                log.info("판매자 상품 수정 - 옵션 삭제- opNo : " + opNo);
                optionRepository.deleteById(opNo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // 판매자 상품 관리 - 등록한 상품 커스텀 옵션 추가
    public ResponseEntity<?> optionAdd(List<OptionDTO> optionDTOS) {
        log.info("상품 의류 옵션 추가 Serv 1 : " + optionDTOS);

        // 옵션 리스트 insert
        for (OptionDTO optionDTO : optionDTOS) {
            Option option = modelMapper.map(optionDTO, Option.class);
            log.info("상품 의류 옵션 추가 Serv 2 : " + option);
            optionRepository.save(option);
        }
        return ResponseEntity.ok().body("option insert");
    }

    // 선택 상품 삭제 -  status -> 삭제된 상품
    public ResponseEntity<?> prodDelete(List<Integer> prodNoList) {
        log.info("판매자 상품 삭제 Serv 1 : " + prodNoList);

        for (int prodNo : prodNoList) {
            // 상품 삭제 반복
            productMapper.updateStatusByProdNo(prodNo, "삭제된 상품");
        }
        return ResponseEntity.ok().body("ok");
    }


    /////  이미지 리사이징 //////////////////////////////////////////////////
    // 이미지 리사이징 함수 - width, height
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

    // 사용자 정보 함수
    public String whoAmI(){
        // 현재 로그인 중인 사용자 정보 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 중일 때 해당 사용자 id를 seller에 입력
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String sellerId = userDetails.getMember().getNick();

        return sellerId;
    }
}
