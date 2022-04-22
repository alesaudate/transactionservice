package com.alesaudate.transactionservice.utils

fun <A> A?.throwIfNull(block: () -> Throwable): A {
    if (this == null) throw block()
    return this
}
