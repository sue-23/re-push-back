package org.shooong.push.domain.account.entity;

import org.shooong.push.domain.user.mypage.dto.accountSettings.AccountReqDTO;
import jakarta.persistence.*;
import lombok.*;
import org.shooong.push.domain.user.users.entity.Users;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, length = 20)
    private String depositor;

    @Column(nullable = false, length = 20)
    private String bankName;

    @Column(nullable = false, length = 50)
    private String accountNum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;


    public void updateAccount(AccountReqDTO accountReqDTO) {
        this.depositor = accountReqDTO.getDepositor();
        this.bankName = accountReqDTO.getBankName();
        this.accountNum = accountReqDTO.getAccountNum();
    }
}
