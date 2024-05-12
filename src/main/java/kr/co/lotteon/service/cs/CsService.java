package kr.co.lotteon.service.cs;


import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.cs.*;
import kr.co.lotteon.entity.cs.*;
import kr.co.lotteon.repository.cs.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CsService {

    private final BoardRepository boardRepository;
    private final BoardTypeRepository typeRepository;
    private final BoardCateRepository boardCateRepository;
    private final ModelMapper modelMapper;
    private final BoardFileRepository fileRepository;
    private final FileService fileService;
    private final CommentRepository commentRepository;

    // 글목록(notice, qna)
    public CsPageResponseDTO findByCate(CsPageRequestDTO csPageRequestDTO) {

        // CsPageRequestDTO에서 Pageable을 생성
        Pageable pageable = csPageRequestDTO.getPageable("bno");

        // boardCateRepository에서 모든 게시판 카테고리를 가져옴(cate별로 다 띄우려면 필요)
        List<BoardCateEntity> boardCateEntitieList = boardCateRepository.findAll();

        //typeRepository에서 모든 게시판 타입을 가져옴(type별로 다 띄우려면 필요)
        List<BoardTypeEntity> boardTypeEntitieList = typeRepository.findAll();

        // 각 카테고리별로 해당하는 타입을 매핑하여 cateMap에 저장합니다. 또한 카테고리명을 cateNameMap에 저장
        Map<String, String> cateNameMap = new HashMap<>();
        Map<String, Map<Integer, String>> cateMap = new HashMap<>();

        // boardCateEntitieList와 boardTypeEntitieList를 사용하여 카테고리와 타입을 매핑하는 부분
        for (BoardCateEntity boardCateEntity : boardCateEntitieList) {
            Map<Integer, String> typeMap = new HashMap<>();

            for (BoardTypeEntity boardEntity : boardTypeEntitieList) {
                if (boardEntity.getCate().equals(boardCateEntity.getCate())) {
                    typeMap.put(boardEntity.getTypeNo(), boardEntity.getTypeName());
                }
            }
            cateNameMap.put(boardCateEntity.getCate(), boardCateEntity.getCateName());
            cateMap.put(boardCateEntity.getCate(), typeMap);
        }

        // 그룹과 카테고리에 해당하는 게시글을 가져옵니다. 이때 페이징 정보도 함께 전달
        Page<BoardEntity> result = boardRepository.findByGroupAndCate(csPageRequestDTO.getGroup(), csPageRequestDTO.getCate(), pageable);

        // 가져온 결과(엔티티)를 DTO로 변환
        List<BoardDTO> dtoList = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BoardDTO.class))
                .toList();

        // 변환된 DTO 리스트를 순회하면서 각 DTO에 해당하는 카테고리명과 타입명을 설정
        for (BoardDTO boardDTO : dtoList) {
            boardDTO.setTypeName(
                    cateMap.get(boardDTO.getCate()).get(boardDTO.getTypeNo())

            );
            boardDTO.setCateName(
                    cateNameMap.get(boardDTO.getCate())
            );
        }

        // 전체 결과의 총 개수를 가져옵니다. 이는 페이징 처리를 위해 필요
        int totalElement = (int) result.getTotalElements();

        // CsPageResponseDTO를 생성하여 결과를 담고 이를 반환(요청된 페이징 정보, DTO로 변환된 게시글 리스트, 그리고 전체 결과의 총 개수가 포함)
        return CsPageResponseDTO.builder()
                .csPageRequestDTO(csPageRequestDTO)
                .dtoList(dtoList)
                .total(totalElement)
                .build();
    }

    // 글보기
    public BoardDTO findByBnoForBoard(int bno) {

        BoardEntity boardEntity = boardRepository.findById(bno).get();

        List<BoardFileDTO> boardFileDTOS = fileRepository.findByBno(bno)
                .stream()
                .map(entity -> modelMapper.map(entity, BoardFileDTO.class))
                .toList();


        BoardDTO dto = boardEntity.toDTO();
        dto.setFileDTOList(boardFileDTOS);

        List<BoardTypeEntity> boardTypeEntities = typeRepository.findByCate(dto.getCate());
        log.info("getCate : " + dto.getCate());
        log.info("getTypeNo : " + dto.getTypeNo());

        Map<Integer, String> typeMap = new HashMap<>();
        for (BoardTypeEntity boardTypeEntity : boardTypeEntities) {
            typeMap.put(boardTypeEntity.getTypeNo(), boardTypeEntity.getTypeName());
        }
        dto.setTypeName(typeMap.get(dto.getTypeNo()));
        log.info("getTypeName : " + dto.getTypeName());

        return dto;

    }

    public List<BoardDTO> findByCateForFaq(String cate, String group) {
        log.info("서비스 group : " +  group);
        List<BoardDTO> dtoList = new ArrayList<>();
        log.info("서비스 dtoList : " +  dtoList);
        List<BoardTypeEntity> boardTypeEntities = typeRepository.findByCate(cate);
        log.info("서비스 boardTypeEntities : " +  dtoList);

        for (BoardTypeEntity boardTypeEntity : boardTypeEntities) {
            // 최신 글이 먼저 나오도록 변경된 부분
            List<BoardEntity> boardEntities = boardRepository.findTop10ByTypeNoOrderByRdateDesc(boardTypeEntity.getTypeNo());
            log.info("서비스2 --------------------------------------: " + boardTypeEntities);
            List<BoardDTO> boardDTOS = boardEntities
                    .stream()
                    .map(entity -> modelMapper.map(entity, BoardDTO.class))
                    .toList();
            for (BoardDTO boardDTO : boardDTOS) {
                dtoList.add(boardDTO);
            }
        }
        List<BoardEntity> boardEntities = boardRepository.findByGroupOrderByRdateDesc(group);

        return boardEntities.stream().map(entity -> modelMapper.map(entity, BoardDTO.class)).collect(Collectors.toList());
    }

    // 인덱스에 notice 리스트출력
    public List<BoardDTO> getNoticeBoard(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rdate").descending().and(Sort.by("bno").descending()));
        List<BoardEntity> boardEntityPage = boardRepository.findByGroupOrderByRdateDescBnoDesc("notice", pageable);
        List<BoardDTO> dtoList = boardEntityPage
                .stream()
                .map(entity -> modelMapper.map(entity, BoardDTO.class))
                .toList();


        List<BoardCateEntity> boardCateEntitieList = boardCateRepository.findAll();
        List<BoardTypeEntity> boardTypeEntitieList = typeRepository.findAll();

        Map<String, Map<Integer, String>> cateMap = new HashMap<>();
        for (BoardCateEntity boardCateEntity : boardCateEntitieList) {
            Map<Integer, String> typeMap = new HashMap<>();
            for (BoardTypeEntity boardEntity : boardTypeEntitieList) {
                if (boardEntity.getCate().equals(boardCateEntity.getCate())) {
                    typeMap.put(boardEntity.getTypeNo(), boardEntity.getTypeName());
                }
            }
            cateMap.put(boardCateEntity.getCate(), typeMap);
        }

        for (BoardDTO boardDTO : dtoList) {
            boardDTO.setTypeName(
                    cateMap.get(boardDTO.getCate()).get(boardDTO.getTypeNo())
            );
        }
        return dtoList;
    }

    // 인덱스에 qna 리스트 출력
    public List<BoardDTO> getQnaBoard(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rdate").descending().and(Sort.by("bno").descending()));
        List<BoardEntity> boardEntityPage = boardRepository.findByGroupOrderByRdateDescBnoDesc("qna", pageable);
        List<BoardDTO> dtoList = boardEntityPage
                .stream()
                .map(entity -> modelMapper.map(entity, BoardDTO.class))
                .toList();

        List<BoardCateEntity> boardCateEntitieList = boardCateRepository.findAll();
        List<BoardTypeEntity> boardTypeEntitieList = typeRepository.findAll();

        Map<String, Map<Integer, String>> cateMap = new HashMap<>();
        for (BoardCateEntity boardCateEntity : boardCateEntitieList) {
            Map<Integer, String> typeMap = new HashMap<>();
            for (BoardTypeEntity boardEntity : boardTypeEntitieList) {
                if (boardEntity.getCate().equals(boardCateEntity.getCate())) {
                    typeMap.put(boardEntity.getTypeNo(), boardEntity.getTypeName());
                }
            }
            cateMap.put(boardCateEntity.getCate(), typeMap);
        }

        for (BoardDTO boardDTO : dtoList) {
            String cate = boardDTO.getCate();
            if (cate != null) {
                Map<Integer, String> typeMap = cateMap.get(cate);
                if (typeMap != null) {
                    Integer typeNo = boardDTO.getTypeNo();
                    if (typeNo != null) {
                        String typeName = typeMap.get(typeNo);
                        boardDTO.setTypeName(typeName);
                    }
                }
            }
        }
        return dtoList;
    }

    // 글 등록 메서드
    public void save(BoardDTO dto) {
        dto.setFile(dto.getFiles().size());

        for (MultipartFile mf : dto.getFiles()) {
            if (mf.getOriginalFilename() == null || mf.getOriginalFilename() == "") {
                dto.setFile(0);
            }
        }
        BoardEntity boardEntity = modelMapper.map(dto, BoardEntity.class);
        BoardEntity savedArticle = boardRepository.save(boardEntity);
        int bno = savedArticle.getBno();
        dto.setBno(bno);

        fileService.fileUpload(dto);
    }

    // 글 수정
    public void modifyBoard(BoardDTO boardDTO) {
        BoardEntity oBoardEntity = boardRepository.findById(boardDTO.getBno()).get();
        BoardDTO oBoardDTO = modelMapper.map(oBoardEntity, BoardDTO.class);

        oBoardDTO.setContent(boardDTO.getContent());
        oBoardDTO.setTitle(boardDTO.getTitle());
        oBoardDTO.setFiles(boardDTO.getFiles());

        int count = fileService.fileUpload(oBoardDTO);

        oBoardDTO.setFile(oBoardDTO.getFile() + count);

        BoardEntity boardEntity = modelMapper.map(oBoardDTO, BoardEntity.class);
        boardRepository.save(boardEntity);

    }

    // 글 삭제
    @Transactional
    public void deleteBoard(int bno) {
        commentRepository.deleteCommentByBno(bno); // 댓글 먼저 삭제

        Optional<BoardEntity> boardDTO = boardRepository.findById(bno);
        if (boardDTO.isPresent()) {
            boardRepository.deleteByBno(bno);
        }
    }

    // hit 증가
    public BoardDTO updateHit(BoardDTO boardDTO) {
        // 게시글 엔터티를 찾습니다.
        BoardEntity boardEntity = boardRepository.findById(boardDTO.getBno()).orElse(null);

        // 만약 게시글 엔터티가 존재한다면 조회수를 업데이트하고 저장합니다.
        if (boardEntity != null) {
            boardEntity.setHit(boardEntity.getHit() + 1);
            boardRepository.save(boardEntity);
        }

        // 엔터티를 DTO로 매핑하여 반환합니다.
        return modelMapper.map(boardEntity, BoardDTO.class);
    }


    // 댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteComment(int cno) {

        log.info("서비스 cno :" + cno);

        Optional<Comment> optComment = commentRepository.findById(cno);

        log.info("서비스 optArticle :" + optComment);

        // 댓글이 아직 있으면
        if (optComment.isPresent()) {
            // 댓글 삭제
            commentRepository.deleteById(cno);
            log.info("서비스 if문 안 cno :" + cno);
            return ResponseEntity.ok().body(optComment.get());
        } else {
            log.info("서비스 if문 밖 cno :" + cno);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
    }


    // 댓글 작성
    @Transactional
    public ResponseEntity<Comment> insertComment(CommentDTO commentDTO){
        // DTO -> Entity
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        // DB insert 후 저장한 객체 반환 //////////
        // DB insert 시 저장한 객체 Pk 반환
        int cno = commentRepository.save(comment).getCno();

        // user join 해서 nick 가져오기
        Tuple saveTuple =  commentRepository.selectCommentAndNick(cno);
        log.info("insertComment saveTuple : " + saveTuple.get(0, Comment.class));
        log.info("insertComment saveTuple : " + saveTuple.get(1, String.class));

        // tuple -> Entity
        Comment saveComment = saveTuple.get(0, Comment.class);
        String nick = saveTuple.get(1, String.class);
        saveComment.setNick(nick);

        log.info("insertComment saveComment : " + saveComment.toString());
        return ResponseEntity.ok().body(saveComment);
    }

    // 댓글 수정
    @Transactional
    public ResponseEntity<?> updateComment(CommentDTO commentDTO){
        Optional<Comment> optComment = commentRepository.findById(commentDTO.getCno());

        if(optComment.isPresent()){
            // 댓글 수정
            Comment comment = optComment.get();
            comment.setContent(commentDTO.getContent());

            Comment modifiedComment = commentRepository.save(comment);
            log.info("updateComment ...4 : "+ modifiedComment);
            // 수정 후 데이터 반환
            return ResponseEntity.ok().body(Collections.singletonMap("data", modifiedComment));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
    }


}