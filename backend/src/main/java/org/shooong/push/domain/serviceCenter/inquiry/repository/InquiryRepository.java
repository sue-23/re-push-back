package org.shooong.push.domain.serviceCenter.inquiry.repository;

import org.shooong.push.domain.serviceCenter.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUser_UserId(Long userId);

    Optional<Inquiry> findByInquiryIdAndUser_UserId(Long inquiryId, Long userId);
}
