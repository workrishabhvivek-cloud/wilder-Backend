package com.wilderBackend.sections.service.impl;

import com.wilderBackend.sections.dto.BannerDTO;
import com.wilderBackend.sections.dto.SectionDTO;
import com.wilderBackend.sections.mapper.SectionMapper;
import com.wilderBackend.sections.repository.SectionBrandRepository;
import com.wilderBackend.sections.repository.SectionRepository;
import com.wilderBackend.sections.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    private final SectionBrandRepository sectionBrandRepository;

    private final SectionMapper sectionMapper;

    @Override
    public SectionDTO getAllCarousel() {
        // This method should return a list of SectionDTO objects.
        List<BannerDTO> heroDTOList = sectionRepository.findByImageKeyAndIsActive("carousel", true);
        List<BannerDTO> brandDTOList = sectionBrandRepository.findByStatus("ACTIVE");

        return sectionMapper.toDTO(heroDTOList, brandDTOList);
    }

    @Override
    public List<BannerDTO> getAllBanners() {
        return sectionRepository.findByImageKeyAndIsActive("banner", true);
    }
}
