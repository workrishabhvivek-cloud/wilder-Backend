package com.wilderBackend.sections.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class SectionDTO {

    private final List<HeroCarousel> heroCarousels;
    private final List<BrandCarousel> brandCarousels;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HeroCarousel {
        private Long id;

        private String description;

        private String imageUrl;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BrandCarousel{
        private Long id;

        private String brandName;

        private String brandImageUrl;
    }
}
