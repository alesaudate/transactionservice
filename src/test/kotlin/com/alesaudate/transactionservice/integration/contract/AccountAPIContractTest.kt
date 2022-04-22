package com.alesaudate.transactionservice.integration.contract

import com.alesaudate.transactionservice.integration.contract.clients.AccountsAPIClient
import com.alesaudate.transactionservice.utils.randomUUID
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.emptyString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AccountAPIContractTest : BaseContractTest() {

    @Autowired
    private lateinit var accountsAPIClient: AccountsAPIClient

    @Test
    fun `given an endpoint to create an account, when I send a request to create an account, then I should have a new account with zeroed balance`() {
        accountsAPIClient.createAccount()
            .body("balance", equalTo(0))
            .body("id", not(emptyString()))
    }

    @Test
    fun `given an existing account, when I send a request to retrieve it, then I should be able to see the account's attributes`() {
        // arrange & act
        val accountId = accountsAPIClient.createAccount()
            .extract()
            .body()
            .path<String>("id")

        // assert
        accountsAPIClient.retrieveAccount(accountId)
            .body("id", equalTo(accountId))
    }

    @Test
    fun `given an ID that is not related to an existing account, when I send a request to retrieve it, then I should receive a HTTP Not found status`() {
        accountsAPIClient.retrieveAccount(randomUUID())
            .statusCode(404)
    }
}
