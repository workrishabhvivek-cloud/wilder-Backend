package com.wilderBackend.sections.service.impl;

import com.wilderBackend.sections.dto.CarouselDTO;
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
    public List<CarouselDTO> getAllCarousel() {
        // This method should return a list of CarouselDTO objects.
        return sectionRepository.findByImageKeyAndIsActive("carousel", true);
    }
}
