package org.shooong.push.domain.alarm.service;
import org.shooong.push.domain.alarm.dto.RequestAlarmDto;
import org.shooong.push.domain.alarm.dto.ResponseAlarmDto;
import org.shooong.push.domain.alarm.entity.Alarm;
import org.shooong.push.domain.enumData.AlarmType;
import org.shooong.push.domain.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

@Log4j2
@RequiredArgsConstructor
@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;

    private final static Long DEFAULT_TIMEOUT = 3600000L;
    private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId){
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
//        String emitterId = userId+"_"+System.currentTimeMillis();
        userEmitters.put(userId, emitter);

        // 기존 알람 데이터 전송
        sendAlarmNotification(userId);

//        if (!lastEventId.isEmpty()) {
//            Map<String, Object> events = eventCache.entrySet().stream().filter(entry -> entry.getKey().startsWith(emitterId))
//                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//            events.entrySet().stream()
//                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
//                    .forEach(entry -> sendAlarmNotification(userId));
//        }

        // 상황별 emitter 삭제 처리
        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));
        emitter.onError(e -> userEmitters.remove(userId));

        return emitter;
    }

    @Scheduled(fixedRate = 40 * 1000)
    public void sendHeartBeat() {
        List<Long> failedEmitters = new ArrayList<>();
        SseEmitter emitter = null;

        for (Long userId : userEmitters.keySet()) {
            try {
                emitter = userEmitters.get(userId);

                if (emitter == null) {
                    userEmitters.remove(userId);
                    emitter.complete();
                    continue;
                }

                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .name("beat")
                        .data("alarm beat"));

            } catch (Exception e) {
                e.printStackTrace();
                emitter.complete();
            }
        }
        failedEmitters.forEach(userEmitters::remove);
    }

    // 알림들 가져오기
    @Async
    public void sendAlarmNotification(Long userId) {
        List<ResponseAlarmDto> list = alarmRepository.findByUsersUserId(userId).stream().map(ResponseAlarmDto::fromEntity).collect(toList());
        SseEmitter emitter = userEmitters.get(userId);

        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(userId))
                    .name("alarm-list")
                    .data(list));

        } catch (IOException e) {
            userEmitters.remove(userId,emitter);
        }


    }

    // 알람 저장하고 보내기
    @Transactional
    public void sendNotification(Long userId, AlarmType alarmType){

        Alarm newAlarm = alarmRepository.save(RequestAlarmDto.toEntity(userId, alarmType));
        ResponseAlarmDto responseAlarmDto = ResponseAlarmDto.fromEntity(newAlarm);
        SseEmitter emitter = userEmitters.get(userId);
        try {
            if (emitter != null) {
                emitter.send(SseEmitter.event()
                        .name("alarm")
                        .id(String.valueOf(responseAlarmDto.getAlarmId()))
                        .data(responseAlarmDto));
            }
        } catch (IOException e) {
            userEmitters.remove(userId);
        }
    }
}
