package com.wilderBackend.sections.mapper;

import com.wilderBackend.sections.dto.BannerDTO;
import com.wilderBackend.sections.dto.SectionDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class SectionMapper {

    public SectionDTO toDTO(List<BannerDTO> heroCarouselsList, List<BannerDTO> brandCarouselsList) {

        List<SectionDTO.HeroCarousel> heroCarousels = heroCarouselsList.stream()
                .map(dto -> new SectionDTO.HeroCarousel(
                        dto.getId(),
                        dto.getImageTitle(),
                        dto.getImageUrl()
                ))
                .collect(Collectors.toList());

        List<SectionDTO.BrandCarousel> brandCarousels = brandCarouselsList.stream()
                .map(dto -> new SectionDTO.BrandCarousel(
                        dto.getId(),
                        dto.getImageTitle(),
                        dto.getImageUrl()
                ))
                .collect(Collectors.toList());

        return new SectionDTO(heroCarousels, brandCarousels);
    }
}
