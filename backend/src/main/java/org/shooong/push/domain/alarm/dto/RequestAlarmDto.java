package org.shooong.push.domain.alarm.dto;

import org.shooong.push.domain.alarm.entity.Alarm;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.enumData.AlarmType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
public class RequestAlarmDto {

    private Long userId;
    private AlarmType alarmType;
    private LocalDate alarmDate;
    private boolean alarmRead;


    public static Alarm toEntity(Long userId, AlarmType alarmType){
        return Alarm.builder()
                .users(Users.builder().userId(userId).build())
                .alarmType(alarmType)
                .alarmDate(LocalDateTime.now())
                .alarmRead(false)
                .build();
    }


}
