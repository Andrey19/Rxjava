package ru.effectivemobile.rxjava.api

import io.reactivex.Single
import retrofit2.http.GET
import ru.effectivemobile.rxjava.model.DiscountCard
import ru.effectivemobile.rxjava.model.SimplePost

interface CardApiService {
    @GET("cards/serverA")
    fun getCardsFromServerA(): Single<List<DiscountCard>>

    @GET("cards/serverB")
    fun getCardsFromServerB(): Single<List<DiscountCard>>
}


interface SimpleApiService {
    @GET("posts/1")
    fun getPost(): Single<SimplePost>
}