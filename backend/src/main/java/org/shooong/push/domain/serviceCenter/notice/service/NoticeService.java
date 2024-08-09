package org.shooong.push.domain.serviceCenter.notice.service;



import org.shooong.push.domain.serviceCenter.notice.dto.NoticeDto;
import org.shooong.push.domain.serviceCenter.notice.entity.Notice;

import java.util.List;

public interface NoticeService {

    // 공지사항 문의 조회
    List<NoticeDto> getAllNoticeList();

    // 공지사항 문의 상세조회
    NoticeDto getNoticeById(Long noticeId);

    // 공지사항 문회 등록-관리자
    Notice createNotice(NoticeDto noticeDTO);

    // 공지사항 문의 수정-관리자
    NoticeDto updateNotice(Long noticeId, NoticeDto noticeDTO);

    // 공지사항 문의 삭제-관리자
    void deleteNotice(final long noticeId);

    // 공지사항 문의 조회-괸리자
    List<NoticeDto> getAllNoticeListForAdmin();

    // 공지사항 문의 상세조회-관리자
    NoticeDto getNoticeByIdForAdmin(Long noticeId);
}
