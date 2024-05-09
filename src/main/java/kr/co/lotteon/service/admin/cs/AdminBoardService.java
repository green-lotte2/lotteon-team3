package kr.co.lotteon.service.admin.cs;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.admin.AdminBoardPageRequestDTO;
import kr.co.lotteon.dto.admin.AdminBoardPageResponseDTO;
import kr.co.lotteon.dto.cs.BoardCateDTO;
import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardFileDTO;
import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.entity.cs.BoardCateEntity;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.cs.BoardTypeEntity;
import kr.co.lotteon.repository.cs.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminBoardService {

    private final BoardRepository boardRepository;
    private final BoardCateRepository boardCateRepository;
    private final BoardFileRepository fileRepository;
    private final CommentRepository commentRepository;
    private final BoardTypeRepository typeRepository;

    private final ModelMapper modelMapper;

    @Value("${img.upload.path}")
    private String imgUploadPath;

    // 관리자 게시판 관리 - 기본 게시글 목록 출력
    public AdminBoardPageResponseDTO findBoardByGroup(String cate, AdminBoardPageRequestDTO adminBoardPageRequestDTO) {
        String group = adminBoardPageRequestDTO.getGroup();
        String keyword = adminBoardPageRequestDTO.getKeyword();
        Pageable pageable = adminBoardPageRequestDTO.getPageable("bno");
        log.info("게시판관리 - 목록 Serv 1 : " + adminBoardPageRequestDTO);
        log.info("게시판관리 - 목록 Serv 2 cate : " + cate);

        Page<Tuple> boardEntities = null;

        // 전체 조회
        if ((keyword == null || "".equals(keyword)) && ("".equals(cate) || "all".equals(cate) || cate == null)) {
            // DB 조회
            boardEntities = boardRepository.selectBoardsByGroup(adminBoardPageRequestDTO, pageable, group);
            log.info("게시판관리 - 목록 Serv 3 전체 조회 : " + boardEntities);

            // type이 cate인 검색
        } else if ((keyword == null || "".equals(keyword)) && !"all".equals(cate)) {
            // DB 조회
            boardEntities = boardRepository.searchBoardsByCate(adminBoardPageRequestDTO, pageable, group, cate);
            log.info("게시판관리 - 목록 Serv 4 cate인 검색 : " + boardEntities);

            // 키워드로 검색
        } else if (keyword != null) {
            // DB 조회
            boardEntities = boardRepository.searchBoardsByGroup(adminBoardPageRequestDTO, pageable, group);
            log.info("게시판관리 - 목록 Serv 5 키워드로 검색 : " + boardEntities);
        }


        // Page<Tuple>을 List<BoardDTO>로 변환
        List<BoardDTO> dtoList = boardEntities.getContent().stream()
                .map(tuple -> {
                    log.info("tuple : " + tuple);
                    // Tuple -> Board 테이블 칼럼
                    BoardEntity boardEntity = tuple.get(0, BoardEntity.class);
                    // Tuple -> Join한 typeName 칼럼
                    String typeName = tuple.get(1, String.class);
                    // Tuple -> Join한 cateName 칼럼
                    String cateName = tuple.get(2, String.class);
                    // Tuple -> Join한 cateName 칼럼
                    String nick = tuple.get(3, String.class);
                    // Entity -> DTO
                    BoardDTO boardDTO = modelMapper.map(boardEntity, BoardDTO.class);
                    boardDTO.setTypeName(typeName);
                    boardDTO.setCateName(cateName);
                    boardDTO.setNick(nick);

                    log.info("게시판관리 - 목록 Serv 3 : " + boardDTO);

                    return boardDTO;
                })
                .toList();
        log.info("게시판관리 - 목록 Serv 4 : " + dtoList);

        // total 값
        int total = (int) boardEntities.getTotalElements();

        // List<ProductDTO>와 page 정보 리턴
        return AdminBoardPageResponseDTO.builder()
                .adminBoardPageRequestDTO(adminBoardPageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    // 관리자 게시판 관리 - 게시글 등록 카테고리 조회
    public List<BoardCateDTO> findBoardCate() {

        List<BoardCateEntity> boardCates = boardCateRepository.findAll();
        // 조회된 Entity List -> DTO List
        return boardCates.stream()
                .map(cate -> modelMapper.map(cate, BoardCateDTO.class))
                .collect(Collectors.toList());
    }

    // 관리자 게시판 관리 - 게시글 등록 type(말머리) 조회
    public ResponseEntity<?> findBoardType(String cate) {

        List<BoardTypeEntity> boardTypes = typeRepository.findByCate(cate);
        // 조회된 Entity List -> DTO List
        List<BoardTypeDTO> typeList = boardTypes.stream()
                .map(type -> modelMapper.map(type, BoardTypeDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(typeList);
    }

    // 관리자 게시판 관리 - 게시글 등록
    public void adminBoardWrite(BoardDTO boardDTO) {
        log.info("게시글 등록 Serv 1 : " + boardDTO);
        BoardEntity boardEntity = modelMapper.map(boardDTO, BoardEntity.class);
        boardRepository.save(boardEntity);

        // file 등록 구현되면 할 것

    }
    // 관리자 게시판 관리 - 게시글 수정
    public void adminBoardModify(BoardDTO boardDTO) {
        log.info("게시글 수정 Serv 1 : " + boardDTO);

        BoardEntity boardEntity = boardRepository.findById(boardDTO.getBno()).get();

        boardEntity.setCate(boardDTO.getCate());
        boardEntity.setTypeNo(boardDTO.getTypeNo());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());

        boardRepository.save(boardEntity);

        // file 등록 구현되면 할 것

    }

    // 관리자 게시판 관리 - 게시글 삭제
    @Transactional
    public ResponseEntity<?> boardDelete(int bno) {
        log.info("관리자 게시글 삭제 Serv 1 : " + bno);

        Optional<BoardEntity> boardEntity = boardRepository.findById(bno);
        log.info("관리자 게시글 삭제 Serv 2 : " + bno);
        // 게시글 아직 있으면
        if (boardEntity.isPresent()) {

            log.info("관리자 게시글 삭제 Serv 3 : " + bno);
            // 댓글 삭제
            commentRepository.deleteAllByBno(bno);
            // 게시글 삭제
            boardRepository.deleteById(bno);

            return ResponseEntity.ok().body(boardEntity);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
    }

    // 관리자 게시판 보기
    public BoardDTO selectBoard(int bno) {
        log.info("관리자 게시글 보기 Serv 1 : " + bno);
        // DB 조회
        Optional<BoardEntity> optEntity = boardRepository.findById(bno);
        // Entity
        BoardEntity boardEntity = optEntity.get();
        log.info("관리자 게시글 보기 Serv 2 : " + boardEntity);
        // Entity -> DTO
        BoardDTO boardDTO = modelMapper.map(boardEntity, BoardDTO.class);
        log.info("관리자 게시글 보기 Serv 3 : " + boardDTO);

        // file 조회
        List<BoardFileDTO> boardFileDTOS = fileRepository.findByBno(bno)
                .stream()
                .map(entity -> modelMapper.map(entity, BoardFileDTO.class))
                .toList();

        boardDTO.setFileDTOList(boardFileDTOS);
        return boardDTO;
    }

}
