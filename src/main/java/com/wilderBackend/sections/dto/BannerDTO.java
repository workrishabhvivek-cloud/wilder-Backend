package com.wilderBackend.sections.dto;

public record BannerDTO(Long id, String imageTitle, String imageUrl) {
    public Long getId()           { return id(); }
    public String getImageTitle(){ return imageTitle(); }
    public String getImageUrl()   { return imageUrl(); }
}
