package com.alesaudate.transactionservice.interfaces.incoming.http

import com.alesaudate.transactionservice.domain.AccountNotFoundException
import com.alesaudate.transactionservice.domain.InsufficientBalanceException
import com.alesaudate.transactionservice.domain.InvalidAmountException
import com.alesaudate.transactionservice.domain.TransactionsService
import com.alesaudate.transactionservice.interfaces.outcoming.db.Transaction
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Service
@RestController
@RequestMapping("$API_PREFIX/transactions")
class TransactionsAPI(
    val transactionsService: TransactionsService
) : TransactionsAPIDocumentation {

    @PostMapping
    override fun createTransferenceTransaction(@RequestBody transferRequest: TransferenceTransactionRequest): Transaction {
        with(transferRequest) {
            try {
                return transactionsService.performMoneyTransferBetweenAccounts(from, to, amount)
            } catch (e: RuntimeException) {
                when (e) {
                    is InsufficientBalanceException,
                    is InvalidAmountException -> throw BadRequestHTTPStatusCode(e.message ?: "")
                    is AccountNotFoundException -> throw NotFoundHTTPStatusCode(e.message ?: "")
                    else -> throw e
                }
            }
        }
    }
}

@Schema(description = "Used to perform a money transference between two accounts")
data class TransferenceTransactionRequest(
    @Schema(description = "The ID of the account that is issuing the transference")
    val from: String,

    @Schema(description = "The ID of the account that will receive the amount")
    val to: String,

    @Schema(description = "The amount involved in the transference, in cents (no floating point)")
    val amount: Long
)
