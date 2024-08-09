package org.shooong.push.domain.serviceCenter.notice.repository;

import org.shooong.push.domain.serviceCenter.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByOrderByCreateDateDesc();
}
