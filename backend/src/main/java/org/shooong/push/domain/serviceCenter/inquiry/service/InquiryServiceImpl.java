package org.shooong.push.domain.serviceCenter.inquiry.service;

import org.shooong.push.domain.serviceCenter.inquiry.dto.InquiryDto;
import org.shooong.push.domain.serviceCenter.inquiry.dto.InquiryResponseDto;
import org.shooong.push.domain.serviceCenter.inquiry.entity.Inquiry;
import org.shooong.push.domain.serviceCenter.inquiry.entity.InquiryResponse;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.serviceCenter.inquiry.repository.InquiryRepository;
import org.shooong.push.domain.serviceCenter.inquiry.repository.InquiryResponseRepository;
import org.shooong.push.domain.user.users.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class InquiryServiceImpl implements InquiryService{

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InquiryResponseRepository inquiryResponseRepository;

    // 1:1 문의 등록
    @Override
    public Inquiry createInquiry(InquiryDto inquiryDto) {
        Users user = userRepository.findById(inquiryDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Inquiry inquiry = new Inquiry();
        inquiry.setInquiryTitle(inquiryDto.getInquiryTitle());
        inquiry.setInquiryContent(inquiryDto.getInquiryContent());
        inquiry.setUser(user);

        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        log.info("Saved inquiry: {}", savedInquiry);

        return savedInquiry;
    }

    // 1:1 문의 조회
    @Override
    public List<InquiryDto> getAllInquiryList(Long userId) {
        List<Inquiry> inquiryList = inquiryRepository.findByUser_UserId(userId);
        log.info("Found {} inquiries for user {}", inquiryList.size(), userId);

        return inquiryList.stream()
                .map(inquiry -> {
                    InquiryDto dto = new InquiryDto();
                    dto.setInquiryId(inquiry.getInquiryId());
                    dto.setInquiryTitle(inquiry.getInquiryTitle());
                    dto.setInquiryContent(inquiry.getInquiryContent());
                    dto.setCreatedDate(inquiry.getCreateDate());
                    dto.setModifyDate(inquiry.getModifyDate());
                    dto.setUserId(inquiry.getUser().getUserId());

                    List<InquiryResponse> responses = inquiryResponseRepository.findByInquiry_InquiryId(inquiry.getInquiryId());
                    if (responses != null && !responses.isEmpty()) {
                        dto.setResponse(responses.get(0).getResponse());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 1:1 문의 조회 - 관리자용
    @Override
    public List<InquiryDto> getAllInquiryListAdmin() {
        List<Inquiry> inquiryList = inquiryRepository.findAll();
        log.info("Found {} inquiries for admin", inquiryList.size());

        return inquiryList.stream()
                .map(inquiry -> {
                    InquiryDto dto = new InquiryDto();
                    dto.setInquiryId(inquiry.getInquiryId());
                    dto.setInquiryTitle(inquiry.getInquiryTitle());
                    dto.setInquiryContent(inquiry.getInquiryContent());
                    dto.setCreatedDate(inquiry.getCreateDate());
                    dto.setModifyDate(inquiry.getModifyDate());
                    dto.setUserId(inquiry.getUser().getUserId());
                    dto.setNickName(inquiry.getUser().getNickname());

                    List<InquiryResponse> responses = inquiryResponseRepository.findByInquiry_InquiryId(inquiry.getInquiryId());
                    if (responses != null && !responses.isEmpty()) {
                        dto.setResponse(responses.get(0).getResponse());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 1:1 문의 상세조회
    @Override
    public InquiryDto getInquiryById(Long inquiryId, Long userId) {
        Inquiry inquiry = inquiryRepository.findByInquiryIdAndUser_UserId(inquiryId, userId)
                .orElseThrow(() -> new RuntimeException("해당 문의를 찾을 수 없습니다."));

        List<InquiryResponse> responses = inquiryResponseRepository.findByInquiry_InquiryId(inquiryId);

        List<InquiryResponseDto> responseDtos = responses.stream()
                .map(response -> InquiryResponseDto.builder()
                        .response(response.getResponse())
                        .build())
                .collect(Collectors.toList());

        return InquiryDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .createdDate(inquiry.getCreateDate())
                .modifyDate(inquiry.getModifyDate())
                .userId(inquiry.getUser().getUserId())
                .response(responseDtos.toString())
                .build();
    }


    // 1:1 문의 상세조회 - 관리자용
    @Override
    public InquiryDto getInquiryByIdForAdmin(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("해당 문의를 찾을 수 없습니다."));

        List<InquiryResponse> responses = inquiryResponseRepository.findByInquiry_InquiryId(inquiryId);

        List<InquiryResponseDto> responseDtos = responses.stream()
                .map(response -> InquiryResponseDto.builder()
                        .response(response.getResponse())
                        .build())
                .collect(Collectors.toList());

        return InquiryDto.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryContent(inquiry.getInquiryContent())
                .createdDate(inquiry.getCreateDate())
                .modifyDate(inquiry.getModifyDate())
                .userId(inquiry.getUser().getUserId())
                .response(responseDtos.toString())
                .build();
    }
    // 1:1 문의 삭제
    @Override
    public void deleteInquiry(long inquiryId, Long userId) {
    }

    // 1:1 문의 삭제
    @Override
    public void deleteInquiry(Long inquiryId, Long userId) {

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        Long inquiryUserId = inquiry.getUser().getUserId();
        if (!inquiryUserId.equals(userId)) {
            throw new RuntimeException("User is not authorized to delete this inquiry");
        }

        List<InquiryResponse> inquiryResponses = (List<InquiryResponse>) inquiryResponseRepository.findByInquiry_InquiryId(inquiryId);
        inquiryResponseRepository.deleteAll(inquiryResponses);

        inquiryRepository.deleteById(inquiryId);
        log.info("Deleted inquiry with ID: {}", inquiryId);
    }

    // 1대1 문의 답변 등록 - 관리자
    @Override
    public InquiryResponseDto createInquiryResponse(InquiryResponseDto inquiryResponseDto) {
        // User를 ID로 찾기
        Users user = userRepository.findById(inquiryResponseDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Inquiry를 ID로 찾기
        Inquiry inquiry = inquiryRepository.findById(inquiryResponseDto.getInquiryId())
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        // 사용자 권한 검사 - 관리자만 허용
        if (!user.isRole()) {
            throw new RuntimeException("Only administrators can create responses");
        }

        // InquiryResponse 생성
        InquiryResponse inquiryResponse = InquiryResponse.builder()
                .user(user)
                .inquiry(inquiry)
                .response(inquiryResponseDto.getResponse())
                .build();

        // InquiryResponse 저장
        InquiryResponse savedInquiryResponse = inquiryResponseRepository.save(inquiryResponse);
        log.info("답변 작성 완료: {}", savedInquiryResponse);

        // DTO로 변환하여 반환
        return new InquiryResponseDto(
                savedInquiryResponse.getResponseId(),
                savedInquiryResponse.getInquiry().getInquiryId(),
                savedInquiryResponse.getUser().getUserId(),
                savedInquiryResponse.getCreateDate(),
                savedInquiryResponse.getModifyDate(),
                savedInquiryResponse.getResponse()
        );
    }


    // 1:1 문의 답변 삭제
    @Override
    public void deleteInquiryResponse(final long inquiryResponseId) {
        inquiryResponseRepository.deleteById(inquiryResponseId);
    }
}
