package com.premium.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.premium.app.R
import com.premium.app.navigation.Screen
import com.premium.app.ui.theme.PremiumAppTheme
import com.premium.app.viewmodels.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {

    var passwordVisible by remember { mutableStateOf(false) }

    // Observe login success
    LaunchedEffect(authViewModel.isLoginSuccess) {
        if (authViewModel.isLoginSuccess) {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_button),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Email Input
        OutlinedTextField(
            value = authViewModel.loginEmail,
            onValueChange = { authViewModel.validateEmail(it) },
            label = { Text(stringResource(R.string.email_hint)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = !authViewModel.isEmailValid,
            trailingIcon = {
                AnimatedVisibility(
                    visible = authViewModel.loginEmail.isNotBlank(),
                    enter = fadeIn(animationSpec = tween(250)),
                    exit = fadeOut(animationSpec = tween(250))
                ) {
                    if (authViewModel.isEmailValid) {
                        Icon(Icons.Default.CheckCircle, "", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Error, "", tint = Color.Red)
                    }
                }
            },
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        OutlinedTextField(
            value = authViewModel.loginPassword,
            onValueChange = { authViewModel.validatePassword(it) },
            label = { Text(stringResource(R.string.password_hint)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = !authViewModel.isPasswordValid,
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnimatedVisibility(
                        visible = authViewModel.loginPassword.isNotBlank(),
                        enter = fadeIn(animationSpec = tween(250)),
                        exit = fadeOut(animationSpec = tween(250))
                    ) {
                        if (authViewModel.isPasswordValid) {
                            Icon(Icons.Default.CheckCircle, "", tint = Color.Green)
                        } else {
                            Icon(Icons.Default.Error, "", tint = Color.Red)
                        }
                    }
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            },
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password
        TextButton(onClick = { authViewModel.sendPasswordReset() }) {
            Text(stringResource(R.string.forgot_password))
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = { authViewModel.login() },
            modifier = Modifier.fillMaxWidth(),
            enabled = authViewModel.isEmailValid && authViewModel.isPasswordValid && !authViewModel.isLoadingLogin,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            if (authViewModel.isLoadingLogin) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(R.string.login_button), modifier = Modifier.padding(vertical = 8.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign-In Button
        Button(
            onClick = { /* TODO: Implement Google Sign-In */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_google_logo), contentDescription = "Google Logo", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.size(8.dp))
            Text(stringResource(R.string.google_sign_in), modifier = Modifier.padding(vertical = 8.dp))
        }

        // Error Message
        authViewModel.loginError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PremiumAppTheme {
        LoginScreen(navController = rememberNavController())
    }
}
