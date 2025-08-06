package com.wilderBackend.sections.controller;

import com.wilderBackend.response.JwtResponse;
import com.wilderBackend.response.Response;
import com.wilderBackend.sections.dto.CarouselDTO;
import com.wilderBackend.sections.service.SectionService;
import com.wilderBackend.utils.ExceptionUtils;
import com.wilderBackend.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;

    @GetMapping("/carousel")
    public ResponseEntity<Response> getAllCarousel() {

        try {
            List<CarouselDTO> carouselDTOS = sectionService.getAllCarousel();
            return ResponseUtils.data(carouselDTOS);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }


}
