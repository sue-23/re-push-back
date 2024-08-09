package org.shooong.push.domain.serviceCenter.inquiry.repository;

import org.shooong.push.domain.serviceCenter.inquiry.entity.InquiryResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryResponseRepository extends JpaRepository<InquiryResponse, Long> {
    List<InquiryResponse> findByInquiry_InquiryId(Long inquiryId);
}
