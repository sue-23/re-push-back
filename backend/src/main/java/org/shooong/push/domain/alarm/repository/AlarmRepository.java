package org.shooong.push.domain.alarm.repository;

import org.shooong.push.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>  {


    List<Alarm> findByUsersUserId(Long userId);

}
