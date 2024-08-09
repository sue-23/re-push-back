package org.shooong.push.domain.user.mypage.dto.main;

import org.shooong.push.domain.user.mypage.dto.buyHistory.BuyHistoryAllDto;
import org.shooong.push.domain.user.mypage.dto.saleHistory.SaleHistoryDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MypageMainDto {

    private ProfileDto profileDto;
    private Long couponCount;
    private BuyHistoryAllDto buyHistoryAllDto;
    private SaleHistoryDto saleHistoryDto;

    private List<BookmarkProductsDto> bookmarkProductsDto;
}
