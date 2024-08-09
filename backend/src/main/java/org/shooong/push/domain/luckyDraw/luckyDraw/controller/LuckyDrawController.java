package org.shooong.push.domain.luckyDraw.luckyDraw.controller;

import org.shooong.push.domain.luckyDraw.draw.dto.DrawDto;
import org.shooong.push.domain.luckyDraw.luckyDraw.dto.LuckyDrawsDto;
import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.enumData.AlarmType;
import org.shooong.push.domain.luckyDraw.draw.service.DrawService;
import org.shooong.push.domain.luckyDraw.luckyDraw.service.LuckyDrawService;
import org.shooong.push.domain.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class LuckyDrawController {

    private final LuckyDrawService luckyDrawService;
    private final DrawService drawService;

    private final AlarmService alarmService;

    /**
     * 럭키드로우 메인
     */
    @GetMapping("/luckydraw")
    public List<LuckyDrawsDto> luckyDraws() {
        return luckyDrawService.getAllLuckyDraws();
    }

    /**
     * 럭키드로우 상세
     */
    @GetMapping("/luckydraw/{luckyId}")
    public LuckyDrawsDto luckyDrawById(@PathVariable("luckyId") Long luckyId) {
        return luckyDrawService.getLuckyDrawById(luckyId);
    }


    /**
     * 사용자 럭키드로우 응모
     */
    @PostMapping("/api/luckydraw/{luckyId}/enter")
    public ResponseEntity<DrawDto> enterLuckyDraw(@PathVariable Long luckyId, @AuthenticationPrincipal UserDTO userDTO) {

        Long userId = userDTO.getUserId();

        DrawDto drawDTO = drawService.saveDraw(userId, luckyId);

        // 알림 전송
        alarmService.sendNotification(userId, AlarmType.LUCKYAPPLY);

        return ResponseEntity.ok().body(drawDTO);
    }

}