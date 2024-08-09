package org.shooong.push.domain.serviceCenter.notice.service;

import org.shooong.push.domain.serviceCenter.notice.dto.LuckyDrawNoticeDto;
import org.shooong.push.domain.serviceCenter.notice.entity.LuckyDrawNotice;

import java.util.List;

public interface LuckyDrawNoticeService {

    // 조회
    List<LuckyDrawNoticeDto> getAllLuckyDrawAnnouncementList();

    // 상세 조회
    LuckyDrawNoticeDto findLuckyDrawAnnouncementById(Long luckyAnnouncementId);

    // 등록
    LuckyDrawNotice createLuckyDrawAnnouncement(LuckyDrawNoticeDto luckyDrawNoticeDto);

    // 조회-관리자
    List<LuckyDrawNoticeDto> getAllAdminLuckyDrawAnnouncementList();

    // 상세조회-관리자
    LuckyDrawNoticeDto findAdminLuckyDrawAnnouncementById(Long luckyAnnouncementId);

    // 수정
    LuckyDrawNoticeDto updateLuckyDrawAnnouncement(Long luckyAnnouncementId, LuckyDrawNoticeDto luckyDrawNoticeDto);

    // 삭제
    void deleteLuckyDrawAnnouncement(final long luckyDrawAnnouncementId);
}
