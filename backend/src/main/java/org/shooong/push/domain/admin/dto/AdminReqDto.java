package org.shooong.push.domain.admin.dto;

import org.shooong.push.domain.luckyDraw.luckyDraw.entity.LuckyDraw;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


 // 기본 생성자 추가
public class AdminReqDto {

     @Getter
     @Setter
     @NoArgsConstructor
    public static class AdminLuckDrawDto {
        private String luckyName;
        private String content;
        private String luckyImage;
        private String luckySize;
        private Integer luckyPeople;
        private MultipartFile luckyphoto;

        public AdminLuckDrawDto(LuckyDraw luckyDraw) {
            this.luckyName = luckyDraw.getLuckyName();
            this.content = luckyDraw.getContent();
            this.luckyImage = luckyDraw.getLuckyImage();
            this.luckySize = luckyDraw.getLuckySize();
            this.luckyPeople = luckyDraw.getLuckyPeople();
        }
    }
}
