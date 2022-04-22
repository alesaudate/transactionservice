package com.alesaudate.transactionservice.utils

import java.util.Random
import java.util.UUID

fun randomBalance() = Random().nextLong(0, Long.MAX_VALUE)

fun randomUUID() = UUID.randomUUID().toString()
