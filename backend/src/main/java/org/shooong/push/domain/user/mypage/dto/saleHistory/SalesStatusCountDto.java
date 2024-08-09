package org.shooong.push.domain.user.mypage.dto.saleHistory;

import org.shooong.push.domain.enumData.SalesStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SalesStatusCountDto {

    private SalesStatus salesStatus;
    private Long count;

    public SalesStatusCountDto(SalesStatus salesStatus, Long count) {
        this.salesStatus = salesStatus;
        this.count = count;
    }
}
