package com.premium.app.utils

import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object SpamProtectionUtils {

    private const val TAG = "SpamProtectionUtils"
    private const val MAX_REQUESTS_PER_PERIOD = 5
    private const val PERIOD_MILLIS = 60 * 1000L // 1 minuto

    // Cache para almacenar el conteo de solicitudes por usuario o IP
    private val requestCounts = LruCache<String, Int>(100) // Limite de 100 usuarios/IPs en cache
    private val lastRequestTime = LruCache<String, Long>(100)

    /**
     * Verifica si una acción excede el límite de tasa para un identificador dado.
     * @param identifier Un identificador único (por ejemplo, userId o IP).
     * @return true si la acción está permitida, false si se ha excedido el límite de tasa.
     */
    @Synchronized
    fun isActionAllowed(identifier: String): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastTime = lastRequestTime.get(identifier) ?: 0L
        var count = requestCounts.get(identifier) ?: 0

        if (currentTime - lastTime > PERIOD_MILLIS) {
            // Reiniciar el contador si ha pasado el período
            count = 1
            lastRequestTime.put(identifier, currentTime)
        } else {
            count++
        }
        requestCounts.put(identifier, count)

        return count <= MAX_REQUESTS_PER_PERIOD
    }

    /**
     * Simula una verificación de spam para contenido de texto.
     * En un escenario real, esto podría usar APIs de terceros o modelos de ML.
     * @param text El texto a verificar.
     * @return true si el texto no es spam, false si es spam.
     */
    suspend fun isSpam(text: String): Boolean = withContext(Dispatchers.Default) {
        // Simulación: palabras clave de spam o patrones
        val spamKeywords = listOf("ganar dinero rápido", "oferta increíble", "haga clic aquí")
        delay(100) // Simular un pequeño retraso de procesamiento
        return@withContext spamKeywords.any { text.contains(it, ignoreCase = true) }
    }
}
