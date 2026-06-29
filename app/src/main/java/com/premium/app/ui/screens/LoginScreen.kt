package com.premium.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// Suponiendo que R.drawable.logo_app existe y es un recurso de imagen.
// Para que esto compile, necesitarías un archivo de logo en res/drawable.
// Por ahora, usaremos un placeholder o asumiremos que el usuario lo proporcionará.
// Si no tienes un logo, puedes comentar la línea de Image y usar un Text("Logo")

// ViewModel de ejemplo para AuthViewModel
class AuthViewModel : androidx.lifecycle.ViewModel() {
    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")
    var authState by mutableStateOf("Idle") // Puede ser "Idle", "Loading", "Success", "Error"

    fun updateLoginEmail(email: String) {
        loginEmail = email
    }

    fun updateLoginPassword(password: String) {
        loginPassword = password
    }

    fun login() {
        authState = "Loading"
        // Lógica de login simulada
        if (loginEmail == "test@example.com" && loginPassword == "password") {
            authState = "Success"
        } else {
            authState = "Error"
        }
    }

    fun sendPasswordReset() {
        authState = "Loading"
        // Lógica de recuperación de contraseña simulada
        if (loginEmail.isNotEmpty()) {
            authState = "PasswordResetSent"
        } else {
            authState = "Error"
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(authViewModel: AuthViewModel = viewModel()) {
    val loginEmail = authViewModel.loginEmail
    val loginPassword = authViewModel.loginPassword
    val authState = authViewModel.authState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Placeholder para el logo. Reemplazar con Image(painter = painterResource(id = R.drawable.logo_app), ...) si existe.
        Icon(Icons.Default.Person, contentDescription = "Logo", modifier = Modifier.size(120.dp))
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = loginEmail,
            onValueChange = { authViewModel.updateLoginEmail(it) },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            trailingIcon = {
                if (loginEmail.isNotEmpty()) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
                        Icon(Icons.Default.Check, contentDescription = "Email Válido", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Close, contentDescription = "Email Inválido", tint = Color.Red)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = loginPassword,
            onValueChange = { authViewModel.updateLoginPassword(it) },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.login() },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginEmail.isNotEmpty() && loginPassword.isNotEmpty()
        ) {
            Text("Iniciar Sesión")
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { /* Navegar a pantalla de registro */ }) {
            Text("¿No tienes cuenta? Regístrate")
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { authViewModel.sendPasswordReset() }) {
            Text("¿Olvidaste tu contraseña?")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f))
            Text(" O ", modifier = Modifier.padding(horizontal = 8.dp))
            Divider(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { /* Lógica de login con Google */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Star, contentDescription = "Google Icon", modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continuar con Google")
        }

        if (authState == "Loading") {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else if (authState == "Success") {
            Text("Inicio de sesión exitoso", color = Color.Green, modifier = Modifier.padding(top = 16.dp))
        } else if (authState == "Error") {
            Text("Error de inicio de sesión o email/contraseña incorrectos", color = Color.Red, modifier = Modifier.padding(top = 16.dp))
        } else if (authState == "PasswordResetSent") {
            Text("Instrucciones de recuperación enviadas a tu email", color = Color.Blue, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    MaterialTheme {
        LoginScreen()
    }
}
