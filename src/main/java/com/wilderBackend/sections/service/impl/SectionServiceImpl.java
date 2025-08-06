package com.wilderBackend.sections.service.impl;

import com.wilderBackend.sections.dto.SectionDTO;
import com.wilderBackend.sections.repository.SectionRepository;
import com.wilderBackend.sections.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    @Override
    public List<SectionDTO> getAllCarousel() {
        // This method should return a list of SectionDTO objects.
        return sectionRepository.findByImageKeyAndIsActive("carousel", true);
    }

    @Override
    public List<SectionDTO> getAllBanners() {
        return sectionRepository.findByImageKeyAndIsActive("banner", true);
    }
}
