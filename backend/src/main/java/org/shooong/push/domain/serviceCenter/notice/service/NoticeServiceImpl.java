package org.shooong.push.domain.serviceCenter.notice.service;

import org.shooong.push.domain.serviceCenter.notice.dto.NoticeDto;
import org.shooong.push.domain.serviceCenter.notice.entity.Notice;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.serviceCenter.notice.repository.NoticeRepository;
import org.shooong.push.domain.user.users.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;


    // 공지사항 조회
    @Override
    public List<NoticeDto> getAllNoticeList(){

        List<Notice> notices = noticeRepository.findAllByOrderByCreateDateDesc();


        return notices.stream()
                .map(notice -> new NoticeDto(
                        notice.getNoticeId(),
                        notice.getNoticeTitle(),
                        notice.getNoticeContent(),
                        notice.getCreateDate(),
                        notice.getModifyDate(),
                        notice.getUser().getUserId()
                )).collect(Collectors.toList());
    }

    // 공지사항 상세 조회
    @Override
    public NoticeDto getNoticeById(Long noticeId){
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("StyleFeed not found"));

        return new NoticeDto(
                notice.getNoticeId(),
                notice.getNoticeTitle(),
                notice.getNoticeContent(),
                notice.getCreateDate(),
                notice.getModifyDate(),
                notice.getUser().getUserId()
        );
    }

    // 공지사항 문의 리스트조회-관리자
    @Override
    public List<NoticeDto> getAllNoticeListForAdmin() {
        List<Notice> notices = noticeRepository.findAllByOrderByCreateDateDesc();
        return notices.stream()
                .map(notice -> new NoticeDto(
                        notice.getNoticeId(),
                        notice.getNoticeTitle(),
                        notice.getNoticeContent(),
                        notice.getCreateDate(),
                        notice.getModifyDate(),
                        notice.getUser().getUserId()
                )).collect(Collectors.toList());
    }

    // 공지사항 문의 상세조회-관리자
    @Override
    public NoticeDto getNoticeByIdForAdmin(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        return new NoticeDto(
                notice.getNoticeId(),
                notice.getNoticeTitle(),
                notice.getNoticeContent(),
                notice.getCreateDate(),
                notice.getModifyDate(),
                notice.getUser().getUserId()
        );
    }

    // 공지사항 등록-관리자
    @Override
    public Notice createNotice(NoticeDto noticeDto) {

        Users user = userRepository.findById(noticeDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isRole()) {
            throw new RuntimeException("Only administrators can create notices");
        }

        Notice notice = new Notice();
        notice.setNoticeTitle(noticeDto.getNoticeTitle());
        notice.setNoticeContent(noticeDto.getNoticeContent());
        notice.setUser(user);

        return noticeRepository.save(notice);
    }

    // 공지사항 수정-관리자
    @Override
    public NoticeDto updateNotice(Long noticeId, NoticeDto noticeDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        if (noticeDto.getNoticeTitle() != null) {
            notice.setNoticeTitle(noticeDto.getNoticeTitle());
        }
        if (noticeDto.getNoticeContent() != null) {
            notice.setNoticeContent(noticeDto.getNoticeContent());
        }
        Notice updatedNotice = noticeRepository.save(notice);

        return new NoticeDto(
                updatedNotice.getNoticeId(),
                updatedNotice.getNoticeTitle(),
                updatedNotice.getNoticeContent(),
                updatedNotice.getCreateDate(),
                updatedNotice.getModifyDate(),
                updatedNotice.getUser().getUserId()
        );
    }

    // 공지사항 삭제-관리자
    @Override
    public void deleteNotice(final long noticeId) {
        noticeRepository.deleteById(noticeId);
    }
}
