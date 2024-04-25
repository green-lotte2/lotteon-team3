package kr.co.lotteon.service.cs;

import kr.co.lotteon.dto.cs.BoardTypeDTO;
import kr.co.lotteon.entity.cs.BoardCateEntity;
import kr.co.lotteon.entity.cs.BoardTypeEntity;
import kr.co.lotteon.repository.cs.BoardCateRepository;
import kr.co.lotteon.repository.cs.BoardTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class CsCateService {

    private final BoardCateRepository boardCateRepository;
    private final BoardTypeRepository boardTypeRepository;
    private final ModelMapper modelMapper;

    // QnA list - 1차 분류
    public List<BoardCateEntity> getCate(){
        List<BoardCateEntity> boardCateEntities = boardCateRepository.findAll();
        return boardCateEntities;
    }

    // QnA list - 2차 분류
    public List<BoardTypeDTO> getType(){
        List<BoardTypeEntity> boardTypeEntities = boardTypeRepository.findAll();
        return boardTypeEntities.stream()
                .map(type -> modelMapper.map(type, BoardTypeDTO.class))
                .collect(Collectors.toList());
    }

    public List<BoardTypeEntity> findByCate(String cate){
        return boardTypeRepository.findByCate(cate);
    }

    public List<BoardTypeDTO> findByCateTypeDTOS(String cate){
        return boardTypeRepository.findByCate(cate).stream()
                .map(entity -> modelMapper.map(entity, BoardTypeDTO.class ))
                .toList();
    }
}