package com.premium.app.repository

import com.premium.app.models.Message
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRepository @Inject constructor() {

    /**
     * Simula la comunicación con una API de IA para obtener una respuesta.
     * En una aplicación real, esto implicaría hacer una llamada HTTP a un servicio de IA.
     * @param userMessage El mensaje del usuario.
     * @param conversationHistory El historial de la conversación para dar contexto a la IA.
     * @return La respuesta de la IA.
     */
    suspend fun getAIResponse(userMessage: String, conversationHistory: List<Message>): Message {
        delay(1000) // Simula el tiempo de respuesta de la API

        val aiResponseText = when {
            userMessage.contains("hola", ignoreCase = true) -> "¡Hola! ¿En qué puedo ayudarte hoy?"
            userMessage.contains("tiempo", ignoreCase = true) -> "No tengo acceso a información en tiempo real sobre el clima, pero puedo responder otras preguntas."
            userMessage.contains("programación", ignoreCase = true) -> "Claro, puedo ayudarte con programación. ¿Qué lenguaje o problema tienes en mente?"
            userMessage.contains("explica", ignoreCase = true) -> "Para explicarte algo, necesito que me digas qué tema te interesa."
            else -> "Lo siento, no entendí tu pregunta. ¿Podrías reformularla?"
        }

        return Message(
            id = System.currentTimeMillis().toString(),
            senderId = "AI",
            text = aiResponseText,
            timestamp = System.currentTimeMillis(),
            isUser = false
        )
    }
}
