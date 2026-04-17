package ru.effectivemobile.rxjava.model

data class DiscountCard(
    val id: Int,
    val owner: String,
    val discountPercent: Int,
    val server: String // для отладки
)


data class SimplePost(
    val id: Int,
    val title: String
)