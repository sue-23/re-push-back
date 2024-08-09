package org.shooong.push.domain.admin.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
public class AdminProductRespDto {

    private AdminProductDetailDto adminProductDetailDto;
    private List<BuyinBiddingDto> buyingBiddingDtoList;
    private List<SalesBiddingDto> salesBiddingDtoList;


    public AdminProductRespDto(AdminProductDetailDto adminProductDetailDto, List<BuyinBiddingDto> buyingBiddingDtoList, List<SalesBiddingDto> salesBiddingDtoList) {
        this.adminProductDetailDto = adminProductDetailDto;
        this.buyingBiddingDtoList = buyingBiddingDtoList;
        this.salesBiddingDtoList = salesBiddingDtoList;
    }
}
