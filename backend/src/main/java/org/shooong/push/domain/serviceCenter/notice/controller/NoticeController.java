package org.shooong.push.domain.serviceCenter.notice.controller;

import org.shooong.push.domain.serviceCenter.notice.dto.LuckyDrawNoticeDto;
import org.shooong.push.domain.serviceCenter.notice.dto.CombinedNoticeDto;
import org.shooong.push.domain.serviceCenter.notice.dto.NoticeDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.serviceCenter.notice.entity.Notice;
import org.shooong.push.domain.serviceCenter.notice.service.NoticeService;
import org.shooong.push.domain.serviceCenter.notice.service.LuckyDrawNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Log4j2
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private LuckyDrawNoticeService luckyDrawNoticeService;

    // 공지사항 등록
    @PostMapping("/api/user/noticeRegistration")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotice(@RequestBody NoticeDto noticeDto,
                             @AuthenticationPrincipal UserDTO userDTO) {
        if (!userDTO.isRole()) {
            throw new RuntimeException("Only administrators can create notices");
        }

        noticeDto.setUserId(userDTO.getUserId());
        Notice createdNotice = noticeService.createNotice(noticeDto);
        log.info("공지사항 생성: {}", createdNotice);
    }

    // 공지사항 조회
    @GetMapping("/noticeList")
    public List<NoticeDto> getAllNoticeLlist(){

        List<NoticeDto> notices = noticeService.getAllNoticeList();
        log.info("공지사항 조회 완료: {}", notices);
        return notices;
    }

    // 공지사항 상세 조회
    @GetMapping("/notice/{noticeId}")
    public NoticeDto getNoticeById(@PathVariable Long noticeId){
        NoticeDto noticeDto = noticeService.getNoticeById(noticeId);
        log.info("공지사항 상세 조회: {}", noticeDto);
        return noticeDto;
    }

    // 공지사항 조회-관리자
    @GetMapping("/api/admin/noticeList")
    public List<NoticeDto> getAllNoticeListForAdmin(){
        List<NoticeDto> notices = noticeService.getAllNoticeListForAdmin();
        return notices;
    }

    // 공지사항 상세조회-관리자
    @GetMapping("/api/admin/notice/{noticeId}")
    public NoticeDto getNoticeByIdForAdmin(@PathVariable Long noticeId){
        NoticeDto noticeDto = noticeService.getNoticeByIdForAdmin(noticeId);
        return noticeDto;
    }

    // 공지사항 수정
    @PutMapping("/api/user/modifyNotice/{noticeId}")
    public NoticeDto updateNotice(@PathVariable Long noticeId,
                                  @RequestBody NoticeDto noticeDto,
                                  @AuthenticationPrincipal UserDTO userDTO) {
        if (!userDTO.isRole()) {
            throw new RuntimeException("Only administrators can update notices");
        }

        return noticeService.updateNotice(noticeId, noticeDto);
    }


    // 공지사항 삭제
    @DeleteMapping("/api/user/deleteNotice/{noticeId}")
    public void deleteNotice(@PathVariable Long noticeId,
                             @AuthenticationPrincipal UserDTO userDTO) {
        if (!userDTO.isRole()) {
            throw new RuntimeException("Only administrators can delete notices");
        }

        noticeService.deleteNotice(noticeId);
    }

    // 공지사항 전체 조회
    @GetMapping("/user/combinedNoticeList")
    public CombinedNoticeDto getCombinedNoticeList() {
        List<NoticeDto> notices = noticeService.getAllNoticeList();
        List<LuckyDrawNoticeDto> luckyDrawAnnouncements = luckyDrawNoticeService.getAllLuckyDrawAnnouncementList();

        CombinedNoticeDto combinedNotice = new CombinedNoticeDto();
        combinedNotice.setNotices(notices);
        combinedNotice.setLuckyDrawAnnouncements(luckyDrawAnnouncements);

        log.info("통합 공지사항 조회 완료: {}", combinedNotice);
        return combinedNotice;
    }

    // 공지사항 전체 조회-관리자
    @GetMapping("/api/admin/user/combinedNoticeList")
    public CombinedNoticeDto getCombinedNoticeListAdmin() {
        List<NoticeDto> notices = noticeService.getAllNoticeList();
        List<LuckyDrawNoticeDto> luckyDrawAnnouncements = luckyDrawNoticeService.getAllLuckyDrawAnnouncementList();

        CombinedNoticeDto combinedNotice = new CombinedNoticeDto();
        combinedNotice.setNotices(notices);
        combinedNotice.setLuckyDrawAnnouncements(luckyDrawAnnouncements);

        log.info("통합 공지사항 조회 완료: {}", combinedNotice);
        return combinedNotice;
    }
}
