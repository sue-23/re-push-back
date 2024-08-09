package org.shooong.push.domain.alarm.controller;

import org.shooong.push.domain.user.users.dto.UserDTO;
import org.shooong.push.domain.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
@Log4j2
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<ResponseBodyEmitter> subscribe(@AuthenticationPrincipal UserDTO userDTO) {

//        return ResponseEntity.ok(alarmService.subscribe(userDTO.getUserId(), lastEventId));
        return ResponseEntity.ok(alarmService.subscribe(userDTO.getUserId()));
    }


}
