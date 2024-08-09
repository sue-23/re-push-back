package org.shooong.push.domain.serviceCenter.inquiry.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class InquiryListDto {

    private Long inquiryId;
    private String inquiryTitle;
    private String inquiryContent;
    private LocalDateTime createdDate;
    private LocalDateTime modifyDate;
    private Long userId;
}
