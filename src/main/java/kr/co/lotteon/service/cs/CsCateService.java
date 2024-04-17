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

@Log4j2
@RequiredArgsConstructor
@Service
public class CsCateService {

    private final BoardCateRepository boardCateRepository;
    private final BoardTypeRepository boardTypeRepository;
    private final ModelMapper modelMapper;


    public List<BoardCateEntity> getCate(){
        List<BoardCateEntity> boardCateEntities = boardCateRepository.findAll();
        return boardCateEntities;
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