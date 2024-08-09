package org.shooong.push.domain.user.mypage.dto.accountSettings;

import org.shooong.push.domain.account.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AccountDTO {

    private String depositor;
    private String bankName;
    private String accountNum;

    public static AccountDTO fromEntity(Account account) {

        return AccountDTO.builder()
                .depositor(account.getDepositor())
                .bankName(account.getBankName())
                .accountNum(account.getAccountNum())
                .build();
    }
}
