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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = hiltViewModel()) {

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Observe registration success
    LaunchedEffect(authViewModel.isRegisterSuccess) {
        if (authViewModel.isRegisterSuccess) {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Welcome.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.register_button),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Name Input
        OutlinedTextField(
            value = authViewModel.registerName,
            onValueChange = { authViewModel.validateName(it) },
            label = { Text(stringResource(R.string.name_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = !authViewModel.isNameValid,
            trailingIcon = {
                AnimatedVisibility(
                    visible = authViewModel.registerName.isNotBlank(),
                    enter = fadeIn(animationSpec = tween(250)),
                    exit = fadeOut(animationSpec = tween(250))
                ) {
                    if (authViewModel.isNameValid) {
                        Icon(Icons.Default.CheckCircle, "", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Error, "", tint = Color.Red)
                    }
                }
            },
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Username Input
        OutlinedTextField(
            value = authViewModel.registerUsername,
            onValueChange = { authViewModel.validateUsername(it) },
            label = { Text(stringResource(R.string.username_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = !authViewModel.isUsernameValid,
            trailingIcon = {
                AnimatedVisibility(
                    visible = authViewModel.registerUsername.isNotBlank(),
                    enter = fadeIn(animationSpec = tween(250)),
                    exit = fadeOut(animationSpec = tween(250))
                ) {
                    if (authViewModel.isUsernameValid) {
                        Icon(Icons.Default.CheckCircle, "", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Error, "", tint = Color.Red)
                    }
                }
            },
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email Input
        OutlinedTextField(
            value = authViewModel.registerEmail,
            onValueChange = { authViewModel.validateEmail(it) },
            label = { Text(stringResource(R.string.email_hint)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = !authViewModel.isEmailValid,
            trailingIcon = {
                AnimatedVisibility(
                    visible = authViewModel.registerEmail.isNotBlank(),
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
            value = authViewModel.registerPassword,
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
                        visible = authViewModel.registerPassword.isNotBlank(),
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
        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Input
        OutlinedTextField(
            value = authViewModel.registerConfirmPassword,
            onValueChange = { authViewModel.validateConfirmPassword(it) },
            label = { Text(stringResource(R.string.confirm_password_hint)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = !authViewModel.isConfirmPasswordValid,
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnimatedVisibility(
                        visible = authViewModel.registerConfirmPassword.isNotBlank(),
                        enter = fadeIn(animationSpec = tween(250)),
                        exit = fadeOut(animationSpec = tween(250))
                    ) {
                        if (authViewModel.isConfirmPasswordValid) {
                            Icon(Icons.Default.CheckCircle, "", tint = Color.Green)
                        } else {
                            Icon(Icons.Default.Error, "", tint = Color.Red)
                        }
                    }
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            },
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Birthdate Input (simple text for now)
        OutlinedTextField(
            value = authViewModel.registerBirthdate,
            onValueChange = { authViewModel.validateBirthdate(it) },
            label = { Text(stringResource(R.string.birthdate_hint)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = !authViewModel.isBirthdateValid,
            trailingIcon = {
                AnimatedVisibility(
                    visible = authViewModel.registerBirthdate.isNotBlank(),
                    enter = fadeIn(animationSpec = tween(250)),
                    exit = fadeOut(animationSpec = tween(250))
                ) {
                    if (authViewModel.isBirthdateValid) {
                        Icon(Icons.Default.CheckCircle, "", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Error, "", tint = Color.Red)
                    }
                }
            },
            shape = MaterialTheme.shapes.medium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Photo (Optional) - Placeholder
        Button(
            onClick = { /* TODO: Implement profile photo selection */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(stringResource(R.string.profile_photo_optional), modifier = Modifier.padding(vertical = 8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Terms and Conditions Checkbox
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = authViewModel.registerTermsAccepted,
                    onValueChange = { authViewModel.registerTermsAccepted = it },
                    role = Role.Checkbox
                )
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = authViewModel.registerTermsAccepted,
                onCheckedChange = null // Handled by toggleable modifier
            )
            Text(text = stringResource(R.string.terms_and_conditions))
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Register Button
        Button(
            onClick = { authViewModel.register() },
            modifier = Modifier.fillMaxWidth(),
            enabled = authViewModel.isNameValid && authViewModel.isUsernameValid && authViewModel.isEmailValid &&
                    authViewModel.isPasswordValid && authViewModel.isConfirmPasswordValid &&
                    authViewModel.isBirthdateValid && authViewModel.registerTermsAccepted &&
                    !authViewModel.isLoadingRegister,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            if (authViewModel.isLoadingRegister) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(R.string.sign_up_button), modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        // Error Message
        authViewModel.registerError?.let { error ->
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
fun RegisterScreenPreview() {
    PremiumAppTheme {
        RegisterScreen(navController = rememberNavController())
    }
}
