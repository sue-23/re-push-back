package org.shooong.push.domain.luckyDraw.luckyDraw.service;

import org.shooong.push.domain.luckyDraw.luckyDraw.dto.LuckyDrawsDto;
import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import org.shooong.push.domain.enumData.LuckyProcessStatus;
import org.shooong.push.domain.enumData.LuckyStatus;
import org.shooong.push.domain.luckyDraw.draw.repository.DrawRepository;
import org.shooong.push.domain.luckyDraw.luckyDraw.repository.LuckyDrawRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class LuckyDrawService {

    private final LuckyDrawRepository luckyDrawRepository;
    private final DrawRepository drawRepository;

    /**
     * 럭키드로우 메인페이지 상품 전체 조회
     */
    public List<LuckyDrawsDto> getAllLuckyDraws(){
        return luckyDrawRepository.findByProcess(LuckyProcessStatus.PROCESS).stream()
                .map(LuckyDrawsDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 럭키드로우 상세페이지 상품 정보 조회
     */
    public LuckyDrawsDto getLuckyDrawById(Long luckyId){
        return luckyDrawRepository.findById(luckyId)
                .map(LuckyDrawsDto::fromEntity)
                .orElseThrow(()-> new IllegalArgumentException("Lucky Draw Not Found: " + luckyId));
    }

    /**
     * 매주 월요일 11시에 응모마감일 확인 후 luckyProcessStatus 변경
     */
    @Scheduled(cron = "0 0 11 * * MON")
    @Transactional
    public void changeLuckyProcessStatus() {

        List<LuckyDraw> luckyDrawList =  luckyDrawRepository.findTodayEnd(LocalDateTime.now());

        for (LuckyDraw luckyDraw : luckyDrawList) {
            luckyDrawRepository.updateLuckyProcessStatus(luckyDraw.getLuckyId(), LuckyProcessStatus.END);
        }

        log.info("luckyProcessStatus 변경 완료");
    }

    /**
     * 매주 화요일 18시에 당첨발표일 확인 후 luckyStatus 변경
     */
    @Scheduled(cron = "0 0 18 * * TUE")
    @Transactional
    public void getTodayLucky(){

        List<LuckyDraw> luckyDrawList =  luckyDrawRepository.findTodayLucky(LocalDateTime.now());

        List<LuckyDrawsDto> luckyDrawsDto = luckyDrawList.stream()
                .map(LuckyDrawsDto::fromEntity)
                .toList();

        luckyDrawsDto.stream()
                .map(LuckyDrawsDto::getLuckyId)
                .forEach(this::changeLuckyStatus);

        log.info("luckyStatus 변경 완료");
    }

    @Transactional
    public void changeLuckyStatus(Long luckyId) {
        LuckyDraw luckyDraw = validationLuckyId(luckyId);

        List<Long> drawIdList = drawRepository.findAllDrawIdByLuckyDraw(luckyId);

        if (!drawIdList.isEmpty()) {
            Random random = new Random();

            // 당첨인원 기준 랜덤으로 당첨자의 drawId 선정
            List<Long> pickDrawIdList = random.ints(0, drawIdList.size())
                    .distinct()
                    .limit(luckyDraw.getLuckyPeople())
                    .mapToObj(drawIdList::get)
                    .collect(Collectors.toList());

            drawRepository.updateLuckyStatus(LuckyStatus.LUCKY, pickDrawIdList);

            drawIdList.removeAll(pickDrawIdList);
            drawRepository.updateLuckyStatus(LuckyStatus.UNLUCKY, drawIdList);
        }
    }

    /**
     * 존재하는 럭키드로우인지 검사
     */
    public LuckyDraw validationLuckyId(Long luckyId) {
        return luckyDrawRepository.findById(luckyId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 럭키드로우 입니다."));
    }

}

