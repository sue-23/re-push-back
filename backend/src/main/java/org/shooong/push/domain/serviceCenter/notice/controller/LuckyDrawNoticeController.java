package org.shooong.push.domain.serviceCenter.notice.controller;

import org.shooong.push.domain.serviceCenter.notice.dto.LuckyDrawNoticeDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.serviceCenter.notice.entity.LuckyDrawNotice;
import org.shooong.push.domain.serviceCenter.notice.service.LuckyDrawNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class LuckyDrawNoticeController {

    @Autowired
    private LuckyDrawNoticeService luckyDrawNoticeService;

    // 이벤트 공지사항 등록
    @PostMapping("/api/announcementRegistration")
    @ResponseStatus(HttpStatus.CREATED)
    public LuckyDrawNotice createLuckyDrawAnnouncement(
            @RequestBody LuckyDrawNoticeDto luckyDrawNoticeDto,
            @AuthenticationPrincipal UserDTO userDTO) {

        if (!userDTO.isRole()) {
            throw new RuntimeException("Only administrators can register announcements");
        }

        LuckyDrawNotice createLuckyAnnouncement = luckyDrawNoticeService.createLuckyDrawAnnouncement(luckyDrawNoticeDto);
        log.info("새로운 공지사항 생성: {}", createLuckyAnnouncement);
        return createLuckyAnnouncement;
    }

    // 이벤트 공지사항 조회
    @GetMapping("/luckyDrawAnnouncementList")
    public List<LuckyDrawNoticeDto> luckyDrawAnnouncementList(){
        List<LuckyDrawNoticeDto> announcement = luckyDrawNoticeService.getAllLuckyDrawAnnouncementList();
        log.info("조회 완료{}", announcement);
        return announcement;
    }

    // 이벤트 공지사항 상세조회
    @GetMapping("/luckyDrawAnnouncement/{luckyAnnouncementId}")
    public LuckyDrawNoticeDto luckyDrawAnnouncement(@PathVariable Long luckyAnnouncementId){
        LuckyDrawNoticeDto luckyDrawNoticeDto = luckyDrawNoticeService.findLuckyDrawAnnouncementById(luckyAnnouncementId);
        log.info("이벤트 공지사항 상세조회 완료: {}", luckyDrawNoticeDto);
        return luckyDrawNoticeDto;
    }
    // 이벤트 공지사항 조회-관리자
    @GetMapping("/api/admin/luckyDrawAnnouncementList")
    public List<LuckyDrawNoticeDto> adminLuckyDrawAnnouncementList(){
        List<LuckyDrawNoticeDto> announcement = luckyDrawNoticeService.getAllAdminLuckyDrawAnnouncementList();
        log.info("관리자 조회 완료{}", announcement);
        return announcement;
    }

    // 관리자용 이벤트 공지사항 상세조회
    @GetMapping("/api/admin/luckyDrawAnnouncement/{announcementId}")
    public LuckyDrawNoticeDto adminLuckyDrawAnnouncement(@PathVariable Long announcementId){
        LuckyDrawNoticeDto luckyDrawNoticeDto = luckyDrawNoticeService.findAdminLuckyDrawAnnouncementById(announcementId);
        log.info("관리자 이벤트 공지사항 상세조회 완료: {}", luckyDrawNoticeDto);
        return luckyDrawNoticeDto;
    }

    // 이벤트 공지사항 수정
    @PutMapping("/api/modifyAnnouncement/{announcementId}")
    public LuckyDrawNoticeDto updateLuckyDrawAnnouncement(
            @PathVariable Long announcementId,
            @RequestBody LuckyDrawNoticeDto luckyDrawNoticeDto,
            @AuthenticationPrincipal UserDTO userDTO) {

        if (!userDTO.isRole()) {
            throw new RuntimeException("Only administrators can modify announcements");
        }

        return luckyDrawNoticeService.updateLuckyDrawAnnouncement(announcementId, luckyDrawNoticeDto);
    }

    // 이벤트 공지사항 삭제
    @DeleteMapping("/api/deleteAnnouncement/{announcementId}")
    public void deleteLuckyDrawAnnouncement(@PathVariable Long announcementId,
                                            @AuthenticationPrincipal UserDTO userDTO) {

        if (!userDTO.isRole()) {
            throw new RuntimeException("Only administrators can delete announcements");
        }

        luckyDrawNoticeService.deleteLuckyDrawAnnouncement(announcementId);
    }
}
