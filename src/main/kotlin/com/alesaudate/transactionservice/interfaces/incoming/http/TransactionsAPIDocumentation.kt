package com.alesaudate.transactionservice.interfaces.incoming.http

import com.alesaudate.transactionservice.interfaces.outcoming.db.Transaction
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse

interface TransactionsAPIDocumentation {

    @Operation(
        summary = "Creates a money transference between two accounts",
        tags = ["accounts", "transactions"],
        responses = [
            ApiResponse(
                description = "Transference successfully made",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = JSON,
                        examples = [
                            ExampleObject(
                                """
                                {
                                    "id": "5045ae6c-f7ba-4331-93d9-a5b11714af1e",
                                    "amount": 11,
                                    "from": {
                                        "id": "ea98b0a3-70ca-476b-93bb-4eb268ff7bbd",
                                        "balance": 1201
                                    },
                                    "to": {
                                        "id": "ec3a7a97-0c03-401a-bea4-0d91ed71f7f4",
                                        "balance": 1223
                                    }
                                }
                        """
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                description = "If some of the accounts is not found",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                """
                            {
                                "timestamp": "2022-02-24T18:17:36.137+00:00",
                                "status": 404,
                                "error": "Not Found",
                                "path": "/api/v1/transactions"
                            }
                        """
                            )
                        ]
                    )
                ]
            ),
            ApiResponse(
                description = "If the originating account does not have enough funds or the amount request to be transferred is equals or less than zero",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                """
                            {
                                "timestamp": "2022-02-24T18:17:36.137+00:00",
                                "status": 400,
                                "error": "Bad Request",
                                "path": "/api/v1/transactions"
                            }
                        """
                            )
                        ]
                    )
                ]
            )
        ]
    )
    fun createTransferenceTransaction(transferRequest: TransferenceTransactionRequest): Transaction
}
