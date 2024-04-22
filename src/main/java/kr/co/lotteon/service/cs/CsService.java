package kr.co.lotteon.service.cs;


import kr.co.lotteon.dto.cs.BoardDTO;
import kr.co.lotteon.dto.cs.BoardFileDTO;
import kr.co.lotteon.dto.cs.CsPageRequestDTO;
import kr.co.lotteon.dto.cs.CsPageResponseDTO;
import kr.co.lotteon.entity.cs.BoardCateEntity;
import kr.co.lotteon.entity.cs.BoardEntity;
import kr.co.lotteon.entity.cs.BoardTypeEntity;
import kr.co.lotteon.repository.cs.BoardCateRepository;
import kr.co.lotteon.repository.cs.BoardFileRepository;
import kr.co.lotteon.repository.cs.BoardTypeRepository;
import kr.co.lotteon.repository.cs.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CsService {

    private final BoardRepository boardRepository;
    private final BoardTypeRepository typeRepository;
    private final BoardCateRepository boardCateRepository;
    private final ModelMapper modelMapper;
    private final BoardFileRepository fileRepository;

    // 글목록(notice, qna)
    public CsPageResponseDTO findByCate(CsPageRequestDTO csPageRequestDTO) {

        Pageable pageable = csPageRequestDTO.getPageable("bno");

        List<BoardCateEntity> boardCateEntitieList = boardCateRepository.findAll();
        List<BoardTypeEntity> boardTypeEntitieList = typeRepository.findAll();

        Map<String, String> cateNameMap = new HashMap<>();
        Map<String, Map<Integer, String>> cateMap = new HashMap<>();

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

        Page<BoardEntity> result = boardRepository.findByGroupAndCate(csPageRequestDTO.getGroup(), csPageRequestDTO.getCate(), pageable);

        List<BoardDTO> dtoList = result.getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, BoardDTO.class))
                .toList();

        for (BoardDTO boardDTO : dtoList) {
            boardDTO.setTypeName(
                    cateMap.get(boardDTO.getCate()).get(boardDTO.getTypeNo())

            );
            boardDTO.setCateName(
                    cateNameMap.get(boardDTO.getCate())
            );
        }

        int totalElement = (int) result.getTotalElements();

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

    public List<BoardDTO> findByCateForFaq(String cate) {
        List<BoardDTO> dtoList = new ArrayList<>();
        List<BoardTypeEntity> boardTypeEntities = typeRepository.findByCate(cate);
        for (BoardTypeEntity boardTypeEntity : boardTypeEntities) {
            // 최신 글이 먼저 나오도록 변경된 부분
            List<BoardEntity> boardEntities = boardRepository.findTop10ByTypeNoOrderByRdateDesc(boardTypeEntity.getTypeNo());
            List<BoardDTO> boardDTOS = boardEntities
                    .stream()
                    .map(entity -> modelMapper.map(entity, BoardDTO.class))
                    .toList();
            for (BoardDTO boardDTO : boardDTOS) {
                dtoList.add(boardDTO);
            }
        }
        return dtoList;
    }

    // 인덱스에 notice 리스트출력
    public List<BoardDTO> getNoticeBoard(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rdate").descending().and(Sort.by("bno").descending()));
        List<BoardEntity> boardEntityPage = boardRepository.findByGroupOrderByRdateDescBnoDesc("notice",  pageable);
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
            boardDTO.setTypeName(
                    cateMap.get(boardDTO.getCate()).get(boardDTO.getTypeNo())
            );
        }
        return dtoList;
    }

}