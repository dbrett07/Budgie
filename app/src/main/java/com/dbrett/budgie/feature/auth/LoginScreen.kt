package com.dbrett.budgie.feature.auth

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.intellij.lang.annotations.JdkConstants

@Composable
fun LoginScreen(
    modifier : Modifier = Modifier,
    viewModel : AuthViewModel = hiltViewModel(),
    onRegisterClick : () -> Unit
    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding.plus(PaddingValues(start = 8.dp, end = 8.dp, top = 24.dp, bottom = 8.dp))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(Modifier.height(28.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                state = viewModel.emailTextField,
                readOnly = uiState.isLoading
            )

            Spacer(Modifier.height(12.dp))

            OutlinedSecureTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                state = viewModel.passwordTextField,
                textObfuscationMode = TextObfuscationMode.RevealLastTyped
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = viewModel::loginWithEmail,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Log In")
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(buildAnnotatedString {
                append("Don't have an account yet? ")

                val clickableLink = LinkAnnotation.Clickable(
                    tag = "register_route",
                    styles = TextLinkStyles(
                        style = SpanStyle(color = Color.Blue)
                    ),
                    linkInteractionListener =  {
                        onRegisterClick()
                    }
                )
                withLink(clickableLink) {
                    append("Register")
                }
            })
        }
    }

}