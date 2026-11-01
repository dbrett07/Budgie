package com.dbrett.budgie.core.data.repository

import com.dbrett.budgie.core.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUser : User?
    val authState : Flow<AuthState>

    suspend fun signUpWithEmail(email : String, password : String) : Result<User>

    suspend fun loginWithEmail(email : String, password : String) : Result<User>

    suspend fun signInWithGoogle(idToken : String) : Result<User>

    suspend fun signOut() : Result<Unit>

    suspend fun sendPasswordReset(email : String) : Result<Unit>

    suspend fun sendEmailVerification() : Result<Unit>

    suspend fun reloadUser() : Result<Unit>

    suspend fun deleteAccount() : Result<Unit>
}

sealed interface AuthState {
    data object Loading : AuthState
    data object Unauthenticated : AuthState
    data class Authenticated(val user : User) : AuthState
}