package com.premium.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object CompressionUtils {

    private const val TAG = "CompressionUtils"
    private const val MAX_IMAGE_SIZE_KB = 1024 // 1MB
    private const val MAX_VIDEO_SIZE_MB = 10 // 10MB

    /**
     * Comprime una imagen desde una URI y guarda el resultado en un archivo temporal.
     * @param context Contexto de la aplicación.
     * @param imageUri URI de la imagen original.
     * @return Uri del archivo de imagen comprimido, o null si falla la compresión.
     */
    suspend fun compressImage(context: Context, imageUri: Uri): Uri? = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap == null) {
                Log.e(TAG, "No se pudo decodificar el bitmap de la URI: $imageUri")
                return@withContext null
            }

            val outputFile = File(context.cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg")
            FileOutputStream(outputFile).use { outputStream ->
                var quality = 90
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.flush()

                // Reducir calidad hasta que el tamaño del archivo sea aceptable
                while (outputFile.length() / 1024 > MAX_IMAGE_SIZE_KB && quality > 10) {
                    quality -= 10
                    outputStream.channel.truncate(0) // Limpiar el archivo
                    outputStream.seek(0) // Volver al inicio
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    outputStream.flush()
                }
            }
            Log.d(TAG, "Imagen comprimida a: ${outputFile.absolutePath}, tamaño: ${outputFile.length() / 1024} KB")
            return@withContext Uri.fromFile(outputFile)
        } catch (e: Exception) {
            Log.e(TAG, "Error al comprimir imagen: ${e.message}", e)
            return@withContext null
        }
    }

    /**
     * Simula la compresión de video. En una aplicación real, esto requeriría una biblioteca
     * de procesamiento de video como FFmpeg o MediaCodec.
     * @param context Contexto de la aplicación.
     * @param videoUri URI del video original.
     * @return Uri del archivo de video comprimido, o null si falla la compresión.
     */
    suspend fun compressVideo(context: Context, videoUri: Uri): Uri? = withContext(Dispatchers.IO) {
        try {
            // Simulación: en un caso real, aquí se usaría una librería como FFmpeg
            // para re-codificar el video con una tasa de bits más baja.
            val originalFile = getFileFromUri(context, videoUri)
            if (originalFile == null) {
                Log.e(TAG, "No se pudo obtener el archivo del video original: $videoUri")
                return@withContext null
            }

            val originalSizeMB = originalFile.length() / (1024 * 1024)
            if (originalSizeMB <= MAX_VIDEO_SIZE_MB) {
                Log.d(TAG, "Video ya está dentro del tamaño límite (${originalSizeMB}MB <= ${MAX_VIDEO_SIZE_MB}MB), no se necesita compresión.")
                return@withContext videoUri // No se necesita compresión
            }

            val outputFile = File(context.cacheDir, "compressed_video_${System.currentTimeMillis()}.mp4")
            // Simular copia y reducción de tamaño
            originalFile.copyTo(outputFile, overwrite = true)
            // En un escenario real, el tamaño del archivo se reduciría significativamente aquí.
            Log.d(TAG, "Video simulado comprimido a: ${outputFile.absolutePath}, tamaño original: ${originalSizeMB}MB, tamaño simulado: ${outputFile.length() / (1024 * 1024)}MB")
            return@withContext Uri.fromFile(outputFile)
        } catch (e: Exception) {
            Log.e(TAG, "Error al comprimir video: ${e.message}", e)
            return@withContext null
        }
    }

    private fun getFileFromUri(context: Context, uri: Uri): File? {
        if (uri.scheme == "file") {
            return uri.path?.let { File(it) }
        } else if (uri.scheme == "content") {
            // Intentar obtener el archivo real de la URI de contenido
            val filePathColumn = arrayOf(android.provider.MediaStore.MediaColumns.DATA)
            val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(filePathColumn[0])
                    if (columnIndex != -1) {
                        val filePath = it.getString(columnIndex)
                        return filePath?.let { File(it) }
                    }
                }
            }
        }
        return null
    }
}
