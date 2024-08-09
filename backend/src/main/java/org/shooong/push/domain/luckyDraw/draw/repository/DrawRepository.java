package org.shooong.push.domain.luckyDraw.draw.repository;

import org.shooong.push.domain.user.mypage.dto.drawHistory.DrawDetailsDto;
import org.shooong.push.domain.luckyDraw.draw.entity.Draw;
import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import org.shooong.push.domain.enumData.LuckyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {

    // 전체 럭키드로우 응모 건수
    @Query("SELECT COUNT(d) FROM Draw d WHERE d.user.userId = :userId")
    Long countAllByUserId(Long userId);

    // 진행 중 건수
    @Query("SELECT COUNT(d) FROM Draw d WHERE d.user.userId = :userId AND d.luckyStatus ='PROCESS'")
    Long countProcessByUserId(Long userId);

    // 당첨 건수
    @Query("SELECT COUNT(d) FROM Draw d WHERE d.user.userId = :userId AND d.luckyStatus ='LUCKY'")
    Long countLuckyByUserId(Long userId);

    // TODO: QueryDSL로 변경할 것
    // 럭키드로우 응모 상세 정보
    @Query("SELECT new org.shooong.push.domain.user.mypage.dto.drawHistory.DrawDetailsDto(l.luckyImage, l.luckyName, l.luckySize, l.luckyDate, d.luckyStatus) " +
            "FROM Draw d JOIN d.luckyDraw l JOIN d.user u " +
            "WHERE u.userId = :userId " +
            "ORDER BY l.luckyDate DESC")
    List<DrawDetailsDto> findDrawDetailsByUserId(Long userId);


    @Query("SELECT d.drawId FROM Draw d WHERE d.luckyDraw.luckyId = :luckyId")
    List<Long> findAllDrawIdByLuckyDraw(Long luckyId);

    // luckyStatus 상태 변경
    @Modifying
    @Query("UPDATE Draw d SET d.luckyStatus = :luckyStatus WHERE d.drawId IN :drawIdList")
    void updateLuckyStatus(LuckyStatus luckyStatus, List<Long> drawIdList);

    // 사용자가 이미 응모한 럭키드로우인지 확인
    boolean existsByUserUserIdAndLuckyDrawLuckyId(Long userId, Long luckyId);


    Draw findByLuckyDrawAndLuckyStatus(LuckyDraw luckyDraw, LuckyStatus luckyStatus);
}
