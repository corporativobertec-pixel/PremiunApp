package com.premium.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // private val userRepository: UserRepository // Asume un repositorio de usuario
) : ViewModel() {

    private val _profilePictureUrl = MutableStateFlow<String?>("https://via.placeholder.com/150")
    val profilePictureUrl: StateFlow<String?> = _profilePictureUrl

    private val _userName = MutableStateFlow("UsuarioPremium")
    val userName: StateFlow<String> = _userName

    private val _bio = MutableStateFlow("¡Hola! Soy un usuario premium.")
    val bio: StateFlow<String> = _bio

    private val _followersCount = MutableStateFlow(1234)
    val followersCount: StateFlow<Int> = _followersCount

    private val _followingCount = MutableStateFlow(567)
    val followingCount: StateFlow<Int> = _followingCount

    private val _postsCount = MutableStateFlow(89)
    val postsCount: StateFlow<Int> = _postsCount

    private val _nameChangeRemainingTime = MutableStateFlow(0L) // En milisegundos
    val nameChangeRemainingTime: StateFlow<Long> = _nameChangeRemainingTime

    private val _usernameChangeRemainingTime = MutableStateFlow(0L) // En milisegundos
    val usernameChangeRemainingTime: StateFlow<Long> = _usernameChangeRemainingTime

    // Simulación de la última vez que se cambió el nombre/usuario
    private var lastProfileNameChange: Long = 0L
    private var lastUsernameChange: Long = 0L

    private val NAME_CHANGE_COOLDOWN = 5 * 24 * 60 * 60 * 1000L // 5 días en milisegundos
    private val USERNAME_CHANGE_COOLDOWN = 25 * 24 * 60 * 60 * 1000L // 25 días en milisegundos

    init {
        // Cargar datos del perfil del repositorio (simulado)
        viewModelScope.launch {
            // Simular carga de datos
            delay(500)
            lastProfileNameChange = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000L) // Cambiado hace 2 días
            lastUsernameChange = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000L) // Cambiado hace 10 días
            updateCooldownTimers()
        }
    }

    fun updateProfilePicture(newUrl: String) {
        _profilePictureUrl.value = newUrl
        // userRepository.updateProfilePicture(newUrl)
    }

    fun updateName(newName: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastProfileNameChange >= NAME_CHANGE_COOLDOWN) {
            _userName.value = newName
            lastProfileNameChange = currentTime
            updateCooldownTimers()
            // userRepository.updateName(newName)
        } else {
            // Notificar al usuario que no puede cambiar el nombre todavía
        }
    }

    fun updateUsername(newUsername: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUsernameChange >= USERNAME_CHANGE_COOLDOWN) {
            _userName.value = newUsername // Asumiendo que userName también es el username
            lastUsernameChange = currentTime
            updateCooldownTimers()
            // userRepository.updateUsername(newUsername)
        } else {
            // Notificar al usuario que no puede cambiar el nombre de usuario todavía
        }
    }

    fun updateBio(newBio: String) {
        _bio.value = newBio
        // userRepository.updateBio(newBio)
    }

    private fun updateCooldownTimers() {
        viewModelScope.launch {
            while (true) {
                val currentTime = System.currentTimeMillis()
                _nameChangeRemainingTime.value = (lastProfileNameChange + NAME_CHANGE_COOLDOWN - currentTime).coerceAtLeast(0L)
                _usernameChangeRemainingTime.value = (lastUsernameChange + USERNAME_CHANGE_COOLDOWN - currentTime).coerceAtLeast(0L)
                if (_nameChangeRemainingTime.value == 0L && _usernameChangeRemainingTime.value == 0L) {
                    break
                }
                delay(1000) // Actualizar cada segundo
            }
        }
    }
}
