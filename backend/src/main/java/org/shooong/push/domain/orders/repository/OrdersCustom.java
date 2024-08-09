package org.shooong.push.domain.orders.repository;

import org.shooong.push.domain.user.mypage.dto.accountSettings.SalesSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface OrdersCustom {
    Page<SalesSummaryDto> findSalesHistoryByUserId(@Param("userId")Long userId, Pageable pageable);

    BigDecimal findTotalSalesAmountByUserId(@Param("userId") Long userId);


}
