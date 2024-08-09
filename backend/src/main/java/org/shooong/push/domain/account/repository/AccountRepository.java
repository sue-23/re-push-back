package org.shooong.push.domain.account.repository;

import org.shooong.push.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUserUserId(Long userId);
}
