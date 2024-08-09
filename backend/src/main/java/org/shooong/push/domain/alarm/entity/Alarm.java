package org.shooong.push.domain.alarm.entity;

import org.shooong.push.domain.enumData.AlarmType;
import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.user.users.entity.Users;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AlarmType alarmType;

    private LocalDateTime alarmDate;

    @Builder.Default
    @Column(nullable = false)
    private Boolean alarmRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarmUserId", nullable = false)
    private Users users;

}
