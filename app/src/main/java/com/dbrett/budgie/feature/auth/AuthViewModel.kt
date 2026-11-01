package com.dbrett.budgie.feature.auth

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.TextField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbrett.budgie.core.data.repository.AuthRepository
import com.dbrett.budgie.core.data.repository.AuthState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.jvm.Throws

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository : AuthRepository
) : ViewModel() {

    val authState : StateFlow<AuthState> = authRepository.authState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AuthState.Loading
        )

    val emailTextField = TextFieldState("")
    val passwordTextField = TextFieldState("")
    val retypePasswordTextField = TextFieldState("")

    private val _uiState = MutableStateFlow(LoginUiState(isLoading = false, error = null))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun loginWithEmail() {
        val email = emailTextField.text.toString().trim()
        val pass = passwordTextField.text.toString().trim()

        if (email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(error = "Email and Password cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authRepository.loginWithEmail(email, pass)
                .onSuccess { _uiState.update { it.copy(isLoading = false) } }
                .onFailure { error ->
                    _uiState.update { it.copy(
                            isLoading = false,
                            error = resolveErrorString(error)
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun resolveErrorString(error : Throwable) : String {
        return when (error) {
            is FirebaseAuthInvalidCredentialsException -> "Invalid Email or Password"
            is FirebaseAuthInvalidUserException -> "Account Not Found"
            else -> "An unexpected error occurred"
        }
    }
}