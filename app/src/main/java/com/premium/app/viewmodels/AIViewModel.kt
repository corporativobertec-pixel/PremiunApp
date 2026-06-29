package com.premium.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premium.app.models.Message
import com.premium.app.repository.AIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIViewModel @Inject constructor(
    private val aiRepository: AIRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val userMessage = Message(
            id = System.currentTimeMillis().toString(),
            senderId = "User",
            text = text,
            timestamp = System.currentTimeMillis(),
            isUser = true
        )
        _messages.value = _messages.value + userMessage

        _isTyping.value = true
        viewModelScope.launch {
            try {
                val aiResponse = aiRepository.getAIResponse(userMessage.text, _messages.value)
                _messages.value = _messages.value + aiResponse
            } catch (e: Exception) {
                val errorMessage = Message(
                    id = System.currentTimeMillis().toString(),
                    senderId = "AI",
                    text = "Lo siento, hubo un error al procesar tu solicitud.",
                    timestamp = System.currentTimeMillis(),
                    isUser = false
                )
                _messages.value = _messages.value + errorMessage
            } finally {
                _isTyping.value = false
            }
        }
    }

    fun clearChat() {
        _messages.value = emptyList()
    }
}
