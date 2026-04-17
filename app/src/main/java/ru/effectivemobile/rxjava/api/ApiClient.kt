package ru.effectivemobile.rxjava.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object ApiClient {
    private const val BASE_URL = "https://my-json-server.typicode.com/typicode/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            if (request.url.toString().contains("serverB")) {

                if (System.currentTimeMillis() % 2 == 0L) {
                    throw IOException("Сервер B временно недоступен")
                }
            }
            response
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val simpleApi: SimpleApiService = retrofit.create(SimpleApiService::class.java)


    val cardApi: CardApiService = retrofit.create(CardApiService::class.java)
}