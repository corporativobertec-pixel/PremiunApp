package com.premium.app.utils

import android.content.Context
import android.net.Uri
import android.util.Log

object ContentModerationUtils {

    private const val TAG = "ContentModerationUtils"

    /**
     * Simula la moderación automática de contenido. En una aplicación real, esto implicaría
     * el uso de APIs de terceros o modelos de ML en el dispositivo/nube para detectar
     * material prohibido.
     *
     * @param context Contexto de la aplicación.
     * @param contentUri URI del contenido (imagen o video) a moderar.
     * @param contentType Tipo de contenido (por ejemplo, "image" o "video").
     * @return true si el contenido es seguro, false si contiene material prohibido.
     */
    suspend fun moderateContent(context: Context, contentUri: Uri, contentType: String): Boolean {
        Log.d(TAG, "Iniciando moderación de contenido para $contentType: $contentUri")

        // Simulación de detección de contenido prohibido
        // En un escenario real, aquí se integraría una API de moderación de contenido
        // o un modelo de Machine Learning.
        val isSafe = (0..100).random() > 5 // 5% de probabilidad de ser marcado como prohibido

        if (!isSafe) {
            Log.w(TAG, "Contenido marcado como prohibido después de la moderación.")
        }

        return isSafe
    }

    /**
     * Verifica si un texto contiene palabras clave prohibidas.
     * @param text El texto a verificar.
     * @return true si el texto es seguro, false si contiene palabras prohibidas.
     */
    fun moderateText(text: String): Boolean {
        val forbiddenKeywords = listOf("badword1", "badword2", "spamlink.com")
        return forbiddenKeywords.none { text.contains(it, ignoreCase = true) }
    }
}
