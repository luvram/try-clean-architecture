package me.luvram.trycleanarchitecture.account.domain

import me.luvram.trycleanarchitecture.account.domain.Account.AccountId
import me.luvram.trycleanarchitecture.account.domain.Money.Companion
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

class ActivityWindow(
    val activities: MutableList<Activity>
) {
    constructor(vararg activities: Activity): this(listOf(activities) as MutableList<Activity>)

    fun getStartTimestamp(): LocalDateTime {
        return activities.stream()
            .min(Comparator.comparing(Activity::timestamp))
            .orElseThrow { IllegalAccessException() }
            .timestamp
    }

    fun getEndTimestamp(): LocalDateTime {
        return activities.stream()
            .max(Comparator.comparing(Activity::timestamp))
            .orElseThrow{IllegalArgumentException()}
            .timestamp
    }

    fun calculateBalance(accountId: AccountId): Money {
        val depositBalance = activities.stream()
            .filter { a -> a.targetAccountId == accountId }
            .map(Activity::money)
            .reduce(Money.ZERO, Money::add)

        val withdrawalBalance = activities.stream()
            .filter{a -> a.sourceAccountId == accountId}
            .map(Activity::money)
            .reduce(Companion.ZERO, Money::add)

        return Money.add(depositBalance, withdrawalBalance.negate())
    }

    fun addActivity(activity: Activity) {
        this.activities.add(activity)
    }
}
