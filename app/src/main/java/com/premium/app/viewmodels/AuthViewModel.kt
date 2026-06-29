package com.premium.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premium.app.models.User
import com.premium.app.repository.AuthRepository
import com.premium.app.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * AuthViewModel - Maneja toda la lógica de autenticación:
 * Login, Registro, Google Sign-In, validaciones en tiempo real y verificación de email.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Estado de autenticación
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Campos de Login
    private val _loginEmail = MutableStateFlow("")
    val loginEmail: StateFlow<String> = _loginEmail.asStateFlow()

    private val _loginPassword = MutableStateFlow("")
    val loginPassword: StateFlow<String> = _loginPassword.asStateFlow()

    // Campos de Registro
    private val _registerName = MutableStateFlow("")
    val registerName: StateFlow<String> = _registerName.asStateFlow()

    private val _registerUsername = MutableStateFlow("")
    val registerUsername: StateFlow<String> = _registerUsername.asStateFlow()

    private val _registerEmail = MutableStateFlow("")
    val registerEmail: StateFlow<String> = _registerEmail.asStateFlow()

    private val _registerPassword = MutableStateFlow("")
    val registerPassword: StateFlow<String> = _registerPassword.asStateFlow()

    private val _registerConfirmPassword = MutableStateFlow("")
    val registerConfirmPassword: StateFlow<String> = _registerConfirmPassword.asStateFlow()

    private val _registerBirthdate = MutableStateFlow("")
    val registerBirthdate: StateFlow<String> = _registerBirthdate.asStateFlow()

    private val _acceptedTerms = MutableStateFlow(false)
    val acceptedTerms: StateFlow<Boolean> = _acceptedTerms.asStateFlow()

    // Validaciones en tiempo real
    val isNameValid: Boolean get() = ValidationUtils.isValidName(_registerName.value)
    val isUsernameValid: Boolean get() = ValidationUtils.isValidUsername(_registerUsername.value)
    val isEmailValid: Boolean get() = ValidationUtils.isValidEmail(_registerEmail.value)
    val isPasswordValid: Boolean get() = ValidationUtils.isValidPassword(_registerPassword.value)
    val isConfirmPasswordValid: Boolean get() = _registerPassword.value == _registerConfirmPassword.value && _registerConfirmPassword.value.isNotEmpty()
    val isBirthdateValid: Boolean get() = ValidationUtils.isAgeValid(_registerBirthdate.value)
    val isLoginEmailValid: Boolean get() = ValidationUtils.isValidEmail(_loginEmail.value)

    // Verificar si el usuario ya está autenticado
    val isUserLoggedIn: Boolean get() = authRepository.currentUser != null

    // Actualizar campos de Login
    fun updateLoginEmail(email: String) { _loginEmail.value = email }
    fun updateLoginPassword(password: String) { _loginPassword.value = password }

    // Actualizar campos de Registro
    fun updateRegisterName(name: String) { _registerName.value = name }
    fun updateRegisterUsername(username: String) { _registerUsername.value = username }
    fun updateRegisterEmail(email: String) { _registerEmail.value = email }
    fun updateRegisterPassword(password: String) { _registerPassword.value = password }
    fun updateRegisterConfirmPassword(password: String) { _registerConfirmPassword.value = password }
    fun updateRegisterBirthdate(date: String) { _registerBirthdate.value = date }
    fun updateAcceptedTerms(accepted: Boolean) { _acceptedTerms.value = accepted }

    /**
     * Iniciar sesión con correo y contraseña
     */
    fun login() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.loginUser(_loginEmail.value, _loginPassword.value)
            _authState.value = if (result.isSuccess) {
                AuthState.Success(result.getOrNull())
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    /**
     * Registrar nuevo usuario
     */
    fun register() {
        if (!_acceptedTerms.value) {
            _authState.value = AuthState.Error("Debes aceptar los términos y condiciones")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = User(
                name = _registerName.value,
                username = _registerUsername.value,
                email = _registerEmail.value,
                birthdate = _registerBirthdate.value
            )
            val result = authRepository.registerUser(_registerEmail.value, _registerPassword.value, user)
            _authState.value = if (result.isSuccess) {
                // Enviar correo de verificación
                authRepository.currentUser?.sendEmailVerification()
                AuthState.Success(result.getOrNull())
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    /**
     * Inicio de sesión con Google
     */
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // TODO: Implementar Google Sign-In con Firebase
            _authState.value = AuthState.Error("Google Sign-In pendiente de configuración")
        }
    }

    /**
     * Recuperar contraseña
     */
    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepository.sendPasswordResetEmail(email)
            _authState.value = if (result.isSuccess) {
                AuthState.PasswordResetSent
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error al enviar correo")
            }
        }
    }

    /**
     * Cerrar sesión
     */
    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _authState.value = AuthState.Idle
        }
    }

    /**
     * Resetear estado
     */
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

/**
 * Estados posibles de autenticación
 */
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User?) : AuthState()
    data class Error(val message: String) : AuthState()
    object PasswordResetSent : AuthState()
}
