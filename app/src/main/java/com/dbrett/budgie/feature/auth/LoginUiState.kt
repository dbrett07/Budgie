package com.dbrett.budgie.feature.auth

data class LoginUiState(
    val isLoading : Boolean = false,
    val error : String? = null
)
