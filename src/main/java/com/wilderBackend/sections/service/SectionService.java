package com.wilderBackend.sections.service;

import com.wilderBackend.sections.dto.BannerDTO;
import com.wilderBackend.sections.dto.SectionDTO;

import java.util.List;

public interface SectionService {
    SectionDTO getAllCarousel();

    List<BannerDTO> getAllBanners();
}
