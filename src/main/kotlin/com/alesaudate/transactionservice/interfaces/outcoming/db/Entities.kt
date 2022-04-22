package com.alesaudate.transactionservice.interfaces.outcoming.db

import org.hibernate.Hibernate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    val id: String = randomUUID(),
    val balance: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Account

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , balance = $balance )"
    }
}

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    val id: String = randomUUID(),
    val amount: Long,

    @ManyToOne
    @JoinColumn(name = "from_account")
    val from: Account,

    @ManyToOne
    @JoinColumn(name = "to_account")
    val to: Account
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Transaction

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , amount = $amount , from = $from , to = $to )"
    }
}

fun randomUUID() = UUID.randomUUID().toString()
