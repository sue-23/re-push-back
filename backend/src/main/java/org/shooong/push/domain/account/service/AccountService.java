package org.shooong.push.domain.account.service;

import org.shooong.push.domain.user.mypage.dto.accountSettings.AccountDTO;
import org.shooong.push.domain.user.mypage.dto.accountSettings.AccountReqDTO;
import org.shooong.push.domain.account.entity.Account;
import org.shooong.push.domain.user.users.entity.Users;
import org.shooong.push.domain.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * 등록된 계좌 조회
     * 1 : 1
     */
    public AccountDTO getAccount(Long userId){
        Account account = accountRepository.findByUserUserId(userId);

        if(account == null) { return null; }

        return AccountDTO.fromEntity(account);
    }

    /**
     * 계좌 등록 및 변경
     */
    @Transactional
    public AccountDTO updateAccount(Long userId, AccountReqDTO accountReqDTO){
        Account account = accountRepository.findByUserUserId(userId);

        if (account == null) {
            account = Account.builder()
                    .user(Users.builder().userId(userId).build())
                    .depositor(accountReqDTO.getDepositor())
                    .bankName(accountReqDTO.getBankName())
                    .accountNum(accountReqDTO.getAccountNum())
                    .build();
        }

        account.updateAccount(accountReqDTO);

        accountRepository.save(account);

        return AccountDTO.fromEntity(account);
    }
}
