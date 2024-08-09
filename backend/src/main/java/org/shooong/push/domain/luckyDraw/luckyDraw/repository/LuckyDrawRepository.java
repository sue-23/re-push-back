package org.shooong.push.domain.luckyDraw.luckyDraw.repository;

import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import org.shooong.push.domain.enumData.LuckyProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LuckyDrawRepository extends JpaRepository<LuckyDraw,Long> {

    // 메인페이지에서 응모 진행 중인 상품만 조회
    @Query("SELECT ld FROM LuckyDraw ld WHERE ld.luckyProcessStatus = :luckyProcessStatus")
    List<LuckyDraw> findByProcess(LuckyProcessStatus luckyProcessStatus);

    // 현재를 지난 응모마감일인 럭키드로우 조회
    @Query("SELECT ld FROM LuckyDraw ld WHERE ld.luckyEndDate <= :currentDate")
    List<LuckyDraw> findTodayEnd(LocalDateTime currentDate);

    // 응모가 마감되었고, 현재를 지난 당첨발표일인 럭키드로우 조회
    @Query("SELECT ld FROM LuckyDraw ld WHERE ld.luckyProcessStatus = 'END' AND ld.luckyDate <= :currentDate")
    List<LuckyDraw> findTodayLucky(LocalDateTime currentDate);

    // luckyProcessStatus 상태 변경
    @Modifying
    @Query("UPDATE LuckyDraw ld SET ld.luckyProcessStatus = :luckyProcessStatus WHERE ld.luckyId = :luckyId")
    void updateLuckyProcessStatus(Long luckyId, LuckyProcessStatus luckyProcessStatus);


    //LuckyProcessStatus 상태에 따라 상품 조회
    Page<LuckyDraw> findByLuckyProcessStatus(LuckyProcessStatus luckyProcessStatus, Pageable pageable);
    List<LuckyDraw> findByLuckyProcessStatus(LuckyProcessStatus luckyProcessStatus);
}
