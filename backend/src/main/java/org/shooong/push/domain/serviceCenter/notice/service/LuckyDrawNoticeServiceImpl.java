package org.shooong.push.domain.serviceCenter.notice.service;

import org.shooong.push.domain.serviceCenter.notice.dto.LuckyDrawNoticeDto;
import org.shooong.push.domain.serviceCenter.notice.entity.LuckyDrawNotice;
import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import org.shooong.push.domain.luckyDraw.draw.repository.DrawRepository;
import org.shooong.push.domain.serviceCenter.notice.repository.LuckyDrawNoticeRepository;
import org.shooong.push.domain.luckyDraw.luckyDraw.repository.LuckyDrawRepository;
import org.shooong.push.domain.user.users.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LuckyDrawNoticeServiceImpl implements LuckyDrawNoticeService {

    @Autowired
    private LuckyDrawNoticeRepository luckyDrawNoticeRepository;

    @Autowired
    private LuckyDrawRepository luckyDrawRepository;

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private UserRepository userRepository;

    // 이벤트 공지사항 조회
    @Override
    public List<LuckyDrawNoticeDto> getAllLuckyDrawAnnouncementList() {
        List<LuckyDrawNotice> luckyDrawNotices = luckyDrawNoticeRepository.findAll();

        return luckyDrawNotices.stream()
                .map(luckyDrawAnnouncement -> new LuckyDrawNoticeDto(
                        luckyDrawAnnouncement.getLuckyAnnouncementId(),
                        luckyDrawAnnouncement.getLuckyDraw().getLuckyId(),
                        luckyDrawAnnouncement.getLuckyTitle(),
                        luckyDrawAnnouncement.getLuckyContent()
                ))
                .collect(Collectors.toList());
    }

    // 이벤트 공지사항 상세 조회
    @Override
    public LuckyDrawNoticeDto findLuckyDrawAnnouncementById(Long luckyAnnouncementId) {
        LuckyDrawNotice luckyDrawNotice = luckyDrawNoticeRepository.findById(luckyAnnouncementId)
                .orElseThrow(() -> new RuntimeException("Lucky Draw Announcement Not Found"));

        LuckyDraw luckyDraw = luckyDrawNotice.getLuckyDraw();

        return LuckyDrawNoticeDto.builder()
                .luckyAnnouncementId(luckyDrawNotice.getLuckyAnnouncementId())
                .luckyId(luckyDraw.getLuckyId())
                .luckyTitle(luckyDrawNotice.getLuckyTitle())
                .luckyContent(luckyDrawNotice.getLuckyContent())
                .build();
    }

    // 관리자용 메서드 구현
    @Override
    public List<LuckyDrawNoticeDto> getAllAdminLuckyDrawAnnouncementList() {
        // 관리자 전용 로직을 추가할 수 있습니다.
        List<LuckyDrawNotice> luckyDrawNotices = luckyDrawNoticeRepository.findAll();
        return luckyDrawNotices.stream()
                .map(luckyDrawAnnouncement -> new LuckyDrawNoticeDto(
                        luckyDrawAnnouncement.getLuckyAnnouncementId(),
                        luckyDrawAnnouncement.getLuckyDraw().getLuckyId(),
                        luckyDrawAnnouncement.getLuckyTitle(),
                        luckyDrawAnnouncement.getLuckyContent()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public LuckyDrawNoticeDto findAdminLuckyDrawAnnouncementById(Long luckyAnnouncementId) {
        // 관리자 전용 로직을 추가할 수 있습니다.
        LuckyDrawNotice luckyDrawNotice = luckyDrawNoticeRepository.findById(luckyAnnouncementId)
                .orElseThrow(() -> new RuntimeException("Lucky Draw Announcement Not Found"));

        LuckyDraw luckyDraw = luckyDrawNotice.getLuckyDraw();

        return LuckyDrawNoticeDto.builder()
                .luckyAnnouncementId(luckyDrawNotice.getLuckyAnnouncementId())
                .luckyId(luckyDraw.getLuckyId())
                .luckyTitle(luckyDrawNotice.getLuckyTitle())
                .luckyContent(luckyDrawNotice.getLuckyContent())
                .build();
    }

    // 이벤트 공지사항 등록
    @Override
    public LuckyDrawNotice createLuckyDrawAnnouncement(LuckyDrawNoticeDto luckyDrawNoticeDto) {
        LuckyDraw luckyDraw = luckyDrawRepository.findById(luckyDrawNoticeDto.getLuckyId())
                .orElseThrow(() -> new RuntimeException("Lucky Draw Not Found"));

        LuckyDrawNotice luckyDrawNotice = new LuckyDrawNotice();
        luckyDrawNotice.setLuckyTitle(luckyDrawNoticeDto.getLuckyTitle());
        luckyDrawNotice.setLuckyContent(luckyDrawNoticeDto.getLuckyContent());
        luckyDrawNotice.setLuckyDraw(luckyDraw);

        LuckyDrawNotice saved = luckyDrawNoticeRepository.save(luckyDrawNotice);
        log.info(saved.toString());

        return saved;
    }

    @Override
    public LuckyDrawNoticeDto updateLuckyDrawAnnouncement(Long luckyAnnouncementId, LuckyDrawNoticeDto luckyDrawNoticeDto) {
        LuckyDrawNotice luckyDrawNotice = luckyDrawNoticeRepository.findById(luckyAnnouncementId)
                .orElseThrow(() -> new RuntimeException("Lucky Draw Announcement Not Found"));

        luckyDrawNotice.setLuckyTitle(luckyDrawNoticeDto.getLuckyTitle());
        luckyDrawNotice.setLuckyContent(luckyDrawNoticeDto.getLuckyContent());

        LuckyDrawNotice updated = luckyDrawNoticeRepository.save(luckyDrawNotice);
        return new LuckyDrawNoticeDto(
                updated.getLuckyAnnouncementId(),
                updated.getLuckyDraw().getLuckyId(),
                updated.getLuckyTitle(),
                updated.getLuckyContent()
        );
    }

    // 이벤트 공지사항 삭제
    @Override
    public void deleteLuckyDrawAnnouncement(final long luckyAnnouncementId) {
        luckyDrawNoticeRepository.deleteById(luckyAnnouncementId);
    }
}
