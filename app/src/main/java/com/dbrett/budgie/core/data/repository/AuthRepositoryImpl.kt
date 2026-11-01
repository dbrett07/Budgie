package com.dbrett.budgie.core.data.repository

import com.dbrett.budgie.core.model.User
import com.dbrett.budgie.core.model.toDomainModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebase : FirebaseAuth
) : AuthRepository {

    override val currentUser : User?
        get() = firebase.currentUser?.toDomainModel()

    override val authState : Flow<AuthState> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                trySend(AuthState.Authenticated(user.toDomainModel()))
            } else {
                trySend(AuthState.Unauthenticated)
            }
        }

        firebase.addAuthStateListener(listener)

        awaitClose {
            firebase.removeAuthStateListener(listener)
        }
    }

    override suspend fun loginWithEmail(email: String, password : String): Result<User> = runCatching {
        val result = firebase.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw IllegalStateException("User missing after sign in")
        firebaseUser.toDomainModel()
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<User> = runCatching {
        val result = firebase.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw IllegalStateException("User missing after sign up")
        firebaseUser.toDomainModel()
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> = runCatching {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebase.signInWithCredential(credential).await()
        val firebaseUser = result.user ?: throw IllegalStateException("User missing after sign in")
        firebaseUser.toDomainModel()
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> = runCatching {
        firebase.sendPasswordResetEmail(email).await()
    }

    override suspend fun sendEmailVerification(): Result<Unit> = runCatching {
        val user = firebase.currentUser ?: throw IllegalStateException("No authenticated user")
        user.sendEmailVerification().await()
    }

    override suspend fun reloadUser(): Result<Unit> = runCatching {
        val user = firebase.currentUser ?: throw IllegalStateException("No authenticated user")
        user.reload().await()
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        firebase.signOut()
    }

    override suspend fun deleteAccount(): Result<Unit> = runCatching {
        val user = firebase.currentUser ?: throw IllegalStateException("No authenticated user")
        user.delete().await()
    }
}