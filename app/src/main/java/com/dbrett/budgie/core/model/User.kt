package com.dbrett.budgie.core.model

import com.google.firebase.auth.FirebaseUser

data class User(
    val id : String,
    val email : String?,
    val username : String?,
    val isEmailVerified : Boolean
)

fun FirebaseUser.toDomainModel() : User = User(
    id = uid,
    email = email,
    username = displayName,
    isEmailVerified = isEmailVerified
)