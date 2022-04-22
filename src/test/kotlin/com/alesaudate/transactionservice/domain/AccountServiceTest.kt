package com.alesaudate.transactionservice.domain

import com.alesaudate.transactionservice.interfaces.outcoming.db.Account
import com.alesaudate.transactionservice.interfaces.outcoming.db.AccountsRepository
import com.alesaudate.transactionservice.utils.randomBalance
import com.alesaudate.transactionservice.utils.randomUUID
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest(classes = [AccountService::class])
internal class AccountServiceTest {

    @Autowired
    private lateinit var accountService: AccountService

    @MockkBean(relaxed = true)
    private lateinit var accountsRepository: AccountsRepository

    @Test
    fun `loadAccountById - account is connected to an existing account - account is returned`() {
        val account = Account(balance = 0)
        every { accountsRepository.findByIdOrNull(account.id) } returns account

        val loadedAccount = accountService.loadAccountById(account.id)

        assertThat(loadedAccount).isEqualTo(account)
    }

    @Test
    fun `loadAccountById - provided ID is not related to any account - null is returned`() {
        val id = randomUUID()
        every { accountsRepository.findByIdOrNull(id) } returns null

        val loadedAccount = accountService.loadAccountById(id)

        assertThat(loadedAccount).isNull()
    }

    @Test
    fun `createNewAccount - account is created with random balance - balance is saved correctly to database`() {
        val balance = randomBalance()
        every { accountsRepository.save(any()) } answers { firstArg() }

        val createdAccount = accountService.createNewAccount(balance)

        assertThat(createdAccount).isNotNull
        assertThat(createdAccount.balance).isEqualTo(balance)
        verify { accountsRepository.save(createdAccount) }
    }

    @Test
    fun `saveAccounts - two accounts are sent to save - both are saved`() {
        // arrange
        val accountOne = Account(balance = randomBalance())
        val accountTwo = Account(balance = randomBalance())
        every { accountsRepository.saveAll(listOf(accountOne, accountTwo)) } answers { firstArg() }

        // act
        val responseAccounts = accountService.saveAccounts(accountOne, accountTwo)

        // assert
        verify { accountsRepository.saveAll(responseAccounts) }
    }

    @Test
    fun `Account addBalance - some amount is provided - a copy of the account is returned with the new balance`() {
        val account = Account(balance = randomBalance())
        val amountToAdd = randomBalance()

        val newAccount = account.addBalance(amountToAdd)

        assertThat(newAccount).isNotSameAs(account)
        assertThat(newAccount.balance).isEqualTo(account.balance + amountToAdd)
    }

    @Test
    fun `Account reduceBalance - some amount is provided - a copy of the account is returned with the new balance`() {
        val account = Account(balance = randomBalance())
        val amountToAdd = randomBalance()

        val newAccount = account.reduceBalance(amountToAdd)

        assertThat(newAccount).isNotSameAs(account)
        assertThat(newAccount.balance).isEqualTo(account.balance - amountToAdd)
    }
}
