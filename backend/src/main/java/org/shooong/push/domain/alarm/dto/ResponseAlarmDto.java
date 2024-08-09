package org.shooong.push.domain.alarm.dto;

import org.shooong.push.domain.alarm.entity.Alarm;
import org.shooong.push.domain.enumData.AlarmType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
public class ResponseAlarmDto {

    private Long alarmId;
    private AlarmType alarmType;
    private LocalDate alarmDate;
    private boolean alarmRead;

    public static ResponseAlarmDto fromEntity(Alarm alarm){
        return ResponseAlarmDto.builder()
                .alarmId(alarm.getAlarmId())
                .alarmType(alarm.getAlarmType())
                .alarmRead(alarm.getAlarmRead())
                .alarmDate(alarm.getAlarmDate().toLocalDate())
                .build();
    }

}
