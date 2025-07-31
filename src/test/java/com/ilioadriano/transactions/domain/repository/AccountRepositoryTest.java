package com.ilioadriano.transactions.domain.repository;

import com.ilioadriano.transactions.TestUtils;
import com.ilioadriano.transactions.domain.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Assert that Account entity and repository is set up correctly
 */
@DataJpaTest
@ActiveProfiles({"mysql-test", "test"})
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void shouldInsertAnAccount() {
        String randomAccountNumber = TestUtils.generateRandomDocumentNumber();

        Account account = new Account();
        account.setDocumentNumber(randomAccountNumber);

        Account savedAccount = accountRepository.saveAndFlush(account);

        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getDocumentNumber()).isEqualTo(randomAccountNumber);
    }

}