package kr.co.lotteon.service.cs;

import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardFileDTO;
import kr.co.lotteon.entity.cs.BoardFileEntity;
import kr.co.lotteon.repository.cs.BoardFileRepository;
import kr.co.lotteon.repository.cs.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {

    private final BoardFileRepository fileRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    @Value("${file.upload.path}")               // application.yml에서 값을 가져와 Bean에 주입
    private String fileUploadPath;

    public int fileUpload(BoardDTO boardDTO)  {
        if (fileUploadPath.startsWith("file:")) {
            fileUploadPath =  fileUploadPath.substring("file:".length());
        };

        String path = new File(fileUploadPath).getAbsolutePath();  //실제 업로드 할 시스템상의 경로 구하기
        int bno = boardDTO.getBno();
        int count = 0;

        // 원본 파일 폴더 자동 생성
        String orgPath = path + "/orgImage";
        File orgFile = new File(orgPath);
        if (!orgFile.exists()) {
            orgFile.mkdir();
        }

        for(MultipartFile mf : boardDTO.getFiles()){
            if(mf.getOriginalFilename() !=null && mf.getOriginalFilename() != ""){
                // oName, sName 구하기
                String ofile = mf.getOriginalFilename();
                String ext = ofile.substring(ofile.lastIndexOf(".")); //확장자
                String sfile = UUID.randomUUID().toString()+ ext;

                log.info("ofile : "+ofile);

                // 파일 업로드
                try{
                    //upload directory에 upload가 됨 - 원본파일 저장
                    mf.transferTo(new File(orgPath, sfile));

                    //리사이징
                    Thumbnails.of(new File(orgPath, sfile)) // 원본 파일 (경로, 이름)
                            .size(200, 200) // 원하는 사이즈
                            .toFile(new File(path, sfile)); // 리사이징 파일 (경로, 이름)

                    BoardFileDTO fileDTO = BoardFileDTO.builder()
                            .bno(bno)
                            .ofile(ofile)
                            .sfile(sfile)
                            .build();
                    fileRepository.save(fileDTO.toEntity());
                    count++;
                }catch (IOException e){
                    log.error("fileUpload : "+e.getMessage());
                }
            }
        }
        return count;
    }

    @Transactional
    public ResponseEntity<?> fileDownload(int fno)  {

        // 파일 조회
        kr.co.lotteon.entity.cs.BoardFileEntity file = fileRepository.findById(fno).get();

        try {
            if (fileUploadPath.startsWith("file:")) {
                fileUploadPath =  fileUploadPath.substring("file:".length());
            };

            Path path = Paths.get(fileUploadPath + file.getSfile());
            String contentType = Files.probeContentType(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(
                    ContentDisposition.builder("attachment")
                            .filename(file.getOfile(), StandardCharsets.UTF_8).build());

            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            Resource resource = new InputStreamResource(Files.newInputStream(path));

            // 파일 다운로드 카운트 업데이트
            file.setDownload(file.getDownload() + 1);
            fileRepository.save(file);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        }catch (IOException e){
            log.error("fileDownload : " + e.getMessage());
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> fileDownloadCount(int fno)  {

        // 파일 조회
        kr.co.lotteon.entity.cs.BoardFileEntity file = fileRepository.findById(fno).get();

        // 다운로드 카운트 Json 생성
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("count", file.getDownload());

        return ResponseEntity.ok().body(resultMap);
    }

    // 여러파일삭제(게시글 삭제)
    public void deleteFiles(int bno){

        if (fileUploadPath.startsWith("file:")) {
            fileUploadPath =  fileUploadPath.substring("file:".length());
        };

        String path = new File(fileUploadPath).getAbsolutePath();
        List<kr.co.lotteon.entity.cs.BoardFileEntity> files = fileRepository.findFilesByBno(bno);
        for(kr.co.lotteon.entity.cs.BoardFileEntity file : files){
            String sfile = file.getSfile();
            int fno = file.getFno();
            fileRepository.deleteById(fno);

            File deleteFile = new File(fileUploadPath+File.separator+sfile);
            if(deleteFile.exists()){
                deleteFile.delete();
            }
        }
    }

    @Transactional
    public void deleteFileBysName(String sfile){

        if (fileUploadPath.startsWith("file:")) {
            fileUploadPath =  fileUploadPath.substring("file:".length());
        };

        // 해당 sFile의 파일이 있는지 확인
        BoardFileEntity fileEntity = fileRepository.findBySfile(sfile);

        // 있다면 삭제
        if (fileEntity != null){
            fileRepository.deleteBySfile(fileEntity.getSfile());
        }
    }
}