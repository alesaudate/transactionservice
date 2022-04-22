package com.alesaudate.transactionservice.utils

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ExtensionsTest {

    @Test
    fun `throwIfNull - object is null - provided exception is thrown`() {
        assertThatThrownBy {
            null.throwIfNull {
                RuntimeException("Test message")
            }
        }.isExactlyInstanceOf(RuntimeException::class.java).hasMessage("Test message")
    }

    @Test
    fun `throwIfNull - object is not null - provided exception is not thrown`() {
        val obj = Object().throwIfNull {
            RuntimeException("Should not be thrown")
        }
        assertThat(obj).isNotNull
    }
}
