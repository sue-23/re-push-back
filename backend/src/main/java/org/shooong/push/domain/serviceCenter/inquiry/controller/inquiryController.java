package org.shooong.push.domain.serviceCenter.inquiry.controller;

import org.shooong.push.domain.serviceCenter.inquiry.dto.InquiryDto;
import org.shooong.push.domain.serviceCenter.inquiry.dto.InquiryResponseDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.serviceCenter.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class inquiryController {

    @Autowired
    private InquiryService inquiryService;

    // 1:1 문의 등록
    @PostMapping("/api/user/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void createInquiry(@RequestBody InquiryDto inquiryDto,
                              @AuthenticationPrincipal UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        inquiryDto.setUserId(userId);

        inquiryService.createInquiry(inquiryDto);
    }

    // 1:1 문의 조회
    @GetMapping("/api/inquiryList")
    public List<InquiryDto> getAllInquiryList(@AuthenticationPrincipal UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("userDTO is null");
        }
        Long userId = userDTO.getUserId();
        List<InquiryDto> inquiryList = inquiryService.getAllInquiryList(userId);
        log.info("조회 완료 {}", inquiryList);
        return inquiryList;
    }

    // 1:1 문의 조회 - 관리자용
    @GetMapping("/api/admin/inquiryList")
    public List<InquiryDto> getAllInquiryListAdmin(@AuthenticationPrincipal UserDTO userDTO) {
        if (userDTO == null || !userDTO.isRole()) {
            throw new IllegalArgumentException("Access denied");
        }
        List<InquiryDto> inquiryList = inquiryService.getAllInquiryListAdmin();
        log.info("관리자 조회 완료 {}", inquiryList);
        return inquiryList;
    }


    // 1:1 문의 상세조회
    @GetMapping("/api/{inquiryId}")
    public InquiryDto getInquiry(@PathVariable Long inquiryId,
                                 @AuthenticationPrincipal UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("userDTO is null");
        }
        Long userId = userDTO.getUserId();
        InquiryDto inquiryDto = inquiryService.getInquiryById(inquiryId, userId);
        log.info("1:1 문의 상세 조회: {}", inquiryDto);
        return inquiryDto;
    }

    // 1:1 문의 상세조회 - 관리자용
    @GetMapping("/api/admin/{inquiryId}")
    public InquiryDto getInquiryForAdmin(@PathVariable Long inquiryId,
                                         @AuthenticationPrincipal UserDTO userDTO) {
        if (userDTO == null || !userDTO.isRole()) {
            throw new IllegalArgumentException("Access denied");
        }
        InquiryDto inquiryDto = inquiryService.getInquiryByIdForAdmin(inquiryId);
        log.info("관리자용 1:1 문의 상세 조회: {}", inquiryDto);
        return inquiryDto;
    }

    // 1:1 문의 삭제
    @DeleteMapping("/api/{inquiryId}/delete")
    public void deleteInquiry(@PathVariable Long inquiryId,
                              @AuthenticationPrincipal UserDTO userDTO) {
        Long userId = userDTO.getUserId();
        inquiryService.deleteInquiry(inquiryId, userId);
    }

    // 1대1 문의 답변 등록 - 관리자
    @PostMapping("/api/inquiryResponseRegistration/{inquiryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public InquiryResponseDto createInquiryResponse(@PathVariable Long inquiryId,
                                                    @RequestBody InquiryResponseDto inquiryResponseDto,
                                                    @AuthenticationPrincipal UserDTO userDTO) throws AccessDeniedException {

        if (userDTO == null || !userDTO.isRole()) {
            throw new AccessDeniedException("Only administrators can create responses");
        }

        inquiryResponseDto.setUserId(userDTO.getUserId());
        inquiryResponseDto.setInquiryId(inquiryId);
        InquiryResponseDto createdInquiryResponse = inquiryService.createInquiryResponse(inquiryResponseDto);
        log.info("답변 등록 완료: {}", createdInquiryResponse);
        return createdInquiryResponse;
    }


    // 1:1 문의 답변 삭제 - 관리자용
    @DeleteMapping("/api/admin/delete/{responseId}")
    public void deleteInquiryResponse(@PathVariable Long responseId) {
        inquiryService.deleteInquiryResponse(responseId);
    }
}
