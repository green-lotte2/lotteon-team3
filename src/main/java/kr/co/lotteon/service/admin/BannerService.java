package kr.co.lotteon.service.admin;

import kr.co.lotteon.dto.admin.BannerDTO;
import kr.co.lotteon.entity.admin.Banner;
import kr.co.lotteon.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;

    // 활성화 상태인 배너 select
    public List<BannerDTO> selectBanners(String cate){
        List<Banner> banners = bannerRepository.selectByCateAndAct(cate);

        // List<Entity> -> List<DTO>
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }
}
