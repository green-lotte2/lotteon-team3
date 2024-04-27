package kr.co.lotteon.controller;

import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.service.cs.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    @GetMapping("/file/download/{fno}")
    public ResponseEntity<?> fileDownload(@PathVariable("fno") int fno) {
        log.info("fileDownload : " + fno);
        return fileService.fileDownload(fno);
    }

    @GetMapping("/file/downloadCount/{fno}")
    public ResponseEntity<?> fileDownloadCount(@PathVariable("fno") int fno) {
        log.info("fileDownloadCount : " + fno);
        return fileService.fileDownloadCount(fno);
    }

    @GetMapping("/file/delete/{sfile}")
    public ResponseEntity<?> fileDeleteBySfile(@PathVariable("sfile") String sfile){

        log.info("fileDeleteBySfile : " + sfile);
        fileService.deleteFileBysName(sfile);

        return ResponseEntity.ok().build();
    }

    /*
    @GetMapping("/file/delete/${bno}")
    public ResponseEntity<?> fildDelete(int bno){
        fileService.deleteFiles(bno);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file/modify")
    public ResponseEntity<?> fileModify(@RequestBody BoardDTO boardDTO){
        // jsondata로 만들어야 할거 : bno
        fileService.fileUpload(boardDTO);
        return ResponseEntity.ok().build();
    }
     */
}