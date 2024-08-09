package org.shooong.push.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    // 데이터가 새로 생성될 때 시간 저장
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    // 데이터가 수정될 때 시간 저장 (업데이트)
    @LastModifiedDate
    private LocalDateTime modifyDate;
}
