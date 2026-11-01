package com.dbrett.budgie.feature.transactions

data class TransactionFormState(
    val activeErrors : Map<FormField, String> = emptyMap()
)

enum class FormField {
    SOURCE, AMOUNT, TYPE
}