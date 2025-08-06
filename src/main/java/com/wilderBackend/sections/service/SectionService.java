package com.wilderBackend.sections.service;

import com.wilderBackend.sections.dto.SectionDTO;

import java.util.List;

public interface SectionService {
    List<SectionDTO> getAllCarousel();

    List<SectionDTO> getAllBanners();
}
