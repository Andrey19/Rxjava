package ru.effectivemobile.rxjava.repository

import io.reactivex.Single
import ru.effectivemobile.rxjava.model.DiscountCard
import java.util.concurrent.TimeUnit

object CardRepository {
    fun getCardsFromServerA(): Single<List<DiscountCard>> {
        return Single.just(
            listOf(
                DiscountCard(1, "Alice", 10, "Server A"),
                DiscountCard(2, "Bob", 15, "Server A")
            )
        ).delay(1, TimeUnit.SECONDS)
    }

    fun getCardsFromServerB(simulateError: Boolean = true): Single<List<DiscountCard>> {
        return if (simulateError) {
            Single.error(Exception("Сервер B вернул ошибку 500"))
        } else {
            Single.just(
                listOf(
                    DiscountCard(3, "Charlie", 20, "Server B"),
                    DiscountCard(4, "Diana", 25, "Server B")
                )
            ).delay(1, TimeUnit.SECONDS)
        }
    }
}