package com.premium.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Reutilizamos el AuthViewModel del LoginScreen y le añadimos las propiedades y funciones de registro.
// En una aplicación real, esto podría dividirse en RegisterViewModel o tener un AuthViewModel más robusto.
class AuthViewModel : androidx.lifecycle.ViewModel() {
    // Login states (mantener para coherencia si se usa el mismo VM)
    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")

    // Register states
    var registerName by mutableStateOf("")
    var registerUsername by mutableStateOf("")
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var registerConfirmPassword by mutableStateOf("")
    var registerBirthdate by mutableStateOf("") // Formato YYYY-MM-DD
    var acceptedTerms by mutableStateOf(false)

    var authState by mutableStateOf("Idle") // Puede ser "Idle", "Loading", "Success", "Error", "Registered"

    // Validation properties
    val isNameValid: Boolean get() = registerName.length >= 3
    val isUsernameValid: Boolean get() = registerUsername.length >= 3
    val isEmailValid: Boolean get() = android.util.Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches()
    val isPasswordValid: Boolean get() = registerPassword.length >= 6
    val isConfirmPasswordValid: Boolean get() = registerPassword == registerConfirmPassword && registerConfirmPassword.isNotEmpty()
    val isBirthdateValid: Boolean get() {
        return try {
            val date = LocalDate.parse(registerBirthdate, DateTimeFormatter.ISO_LOCAL_DATE)
            date.isBefore(LocalDate.now().minusYears(13)) // Mayor de 13 años
        } catch (e: DateTimeParseException) {
            false
        }
    }

    // Login functions
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

    // Register functions
    fun updateRegisterName(name: String) { registerName = name }
    fun updateRegisterUsername(username: String) { registerUsername = username }
    fun updateRegisterEmail(email: String) { registerEmail = email }
    fun updateRegisterPassword(password: String) { registerPassword = password }
    fun updateRegisterConfirmPassword(confirmPassword: String) { registerConfirmPassword = confirmPassword }
    fun updateRegisterBirthdate(birthdate: String) { registerBirthdate = birthdate }
    fun updateAcceptedTerms(accepted: Boolean) { acceptedTerms = accepted }

    fun register() {
        authState = "Loading"
        if (isNameValid && isUsernameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid && isBirthdateValid && acceptedTerms) {
            // Simular registro exitoso
            authState = "Registered"
        } else {
            authState = "Error"
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(authViewModel: AuthViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Nombre
        OutlinedTextField(
            value = authViewModel.registerName,
            onValueChange = { authViewModel.updateRegisterName(it) },
            label = { Text("Nombre") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre Icon") },
            trailingIcon = {
                if (authViewModel.registerName.isNotEmpty()) {
                    if (authViewModel.isNameValid) {
                        Icon(Icons.Default.Check, contentDescription = "Válido", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Close, contentDescription = "Inválido", tint = Color.Red)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Username
        OutlinedTextField(
            value = authViewModel.registerUsername,
            onValueChange = { authViewModel.updateRegisterUsername(it) },
            label = { Text("Nombre de Usuario") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username Icon") },
            trailingIcon = {
                if (authViewModel.registerUsername.isNotEmpty()) {
                    if (authViewModel.isUsernameValid) {
                        Icon(Icons.Default.Check, contentDescription = "Válido", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Close, contentDescription = "Inválido", tint = Color.Red)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = authViewModel.registerEmail,
            onValueChange = { authViewModel.updateRegisterEmail(it) },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            trailingIcon = {
                if (authViewModel.registerEmail.isNotEmpty()) {
                    if (authViewModel.isEmailValid) {
                        Icon(Icons.Default.Check, contentDescription = "Válido", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Close, contentDescription = "Inválido", tint = Color.Red)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Contraseña
        OutlinedTextField(
            value = authViewModel.registerPassword,
            onValueChange = { authViewModel.updateRegisterPassword(it) },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                if (authViewModel.registerPassword.isNotEmpty()) {
                    if (authViewModel.isPasswordValid) {
                        Icon(Icons.Default.Check, contentDescription = "Válido", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Close, contentDescription = "Inválido", tint = Color.Red)
                    }
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Confirmar Contraseña
        OutlinedTextField(
            value = authViewModel.registerConfirmPassword,
            onValueChange = { authViewModel.updateRegisterConfirmPassword(it) },
            label = { Text("Confirmar Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Confirm Password Icon") },
            trailingIcon = {
                if (authViewModel.registerConfirmPassword.isNotEmpty()) {
                    if (authViewModel.isConfirmPasswordValid) {
                        Icon(Icons.Default.Check, contentDescription = "Válido", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Close, contentDescription = "Inválido", tint = Color.Red)
                    }
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Fecha de Nacimiento
        OutlinedTextField(
            value = authViewModel.registerBirthdate,
            onValueChange = { authViewModel.updateRegisterBirthdate(it) },
            label = { Text("Fecha de Nacimiento (YYYY-MM-DD)") },
            leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Fecha Nacimiento Icon") }, // Usamos Star como reemplazo
            trailingIcon = {
                if (authViewModel.registerBirthdate.isNotEmpty()) {
                    if (authViewModel.isBirthdateValid) {
                        Icon(Icons.Default.Check, contentDescription = "Válido", tint = Color.Green)
                    } else {
                        Icon(Icons.Default.Close, contentDescription = "Inválido", tint = Color.Red)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Aceptar Términos y Condiciones
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = authViewModel.acceptedTerms,
                onCheckedChange = { authViewModel.updateAcceptedTerms(it) }
            )
            Text("Acepto los Términos y Condiciones")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { authViewModel.register() },
            modifier = Modifier.fillMaxWidth(),
            enabled = authViewModel.isNameValid && authViewModel.isUsernameValid &&
                      authViewModel.isEmailValid && authViewModel.isPasswordValid &&
                      authViewModel.isConfirmPasswordValid && authViewModel.isBirthdateValid &&
                      authViewModel.acceptedTerms
        ) {
            Text("Registrarse")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (authViewModel.authState == "Loading") {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else if (authViewModel.authState == "Registered") {
            Text("Registro exitoso", color = Color.Green, modifier = Modifier.padding(top = 16.dp))
        } else if (authViewModel.authState == "Error") {
            Text("Error en el registro. Por favor, verifica tus datos.", color = Color.Red, modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    MaterialTheme {
        RegisterScreen()
    }
}
