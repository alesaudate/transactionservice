package com.alesaudate.transactionservice.interfaces.incoming.http

import com.alesaudate.transactionservice.domain.AccountService
import com.alesaudate.transactionservice.utils.throwIfNull
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Service
@RestController
@RequestMapping("$API_PREFIX/accounts")
class AccountsAPI(
    private val accountService: AccountService
) : AccountsAPIDocumentation {

    @GetMapping("/{id}")
    override fun findAccountById(@PathVariable("id") id: String) =
        accountService.loadAccountById(id)
            .throwIfNull { NotFoundHTTPStatusCode("Account with id $id has not been found") }

    @PostMapping
    override fun createAccount(@RequestBody request: CreateAccountWithBalanceRequest) =
        accountService.createNewAccount(request.balance)
}

@Schema(description = "Used to create a new account with a specified balance")
data class CreateAccountWithBalanceRequest(
    @Schema(description = "The initial account balance, in cents (no floating point)", example = "1212")
    val balance: Long
)
