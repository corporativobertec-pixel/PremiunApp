package com.premium.app.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.premium.app.utils.CompressionUtils
import com.premium.app.utils.ContentModerationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val _uploadProgress = MutableStateFlow<Float>(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress

    private val _isUploading = MutableStateFlow<Boolean>(false)
    val isUploading: StateFlow<Boolean> = _isUploading

    private val _uploadError = MutableStateFlow<String?>(null)
    val uploadError: StateFlow<String?> = _uploadError

    private val _uploadSuccess = MutableStateFlow<Uri?>(null)
    val uploadSuccess: StateFlow<Uri?> = _uploadSuccess

    fun uploadMedia(context: Context, uri: Uri, isVideo: Boolean) {
        _isUploading.value = true
        _uploadProgress.value = 0f
        _uploadError.value = null
        _uploadSuccess.value = null

        viewModelScope.launch {
            try {
                // 1. Moderación de contenido
                val isContentSafe = ContentModerationUtils.moderateContent(context, uri, if (isVideo) "video" else "image")
                if (!isContentSafe) {
                    _uploadError.value = "El contenido no es apropiado y no puede ser publicado."
                    _isUploading.value = false
                    return@launch
                }

                // 2. Compresión inteligente
                val compressedUri = if (isVideo) {
                    CompressionUtils.compressVideo(context, uri)
                } else {
                    CompressionUtils.compressImage(context, uri)
                }

                if (compressedUri == null) {
                    _uploadError.value = "Error al comprimir el archivo."
                    _isUploading.value = false
                    return@launch
                }

                // 3. Subida a Firebase Storage
                val storageRef = firebaseStorage.reference
                val mediaRef = storageRef.child("uploads/${System.currentTimeMillis()}_${compressedUri.lastPathSegment}")

                val uploadTask = mediaRef.putFile(compressedUri)

                uploadTask.addOnProgressListener { taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
                    _uploadProgress.value = progress
                }.await()

                val downloadUrl = mediaRef.downloadUrl.await()
                _uploadSuccess.value = downloadUrl

            } catch (e: Exception) {
                _uploadError.value = "Error al subir el archivo: ${e.message}"
            } finally {
                _isUploading.value = false
            }
        }
    }

    // Métodos para edición de video/imagen con IA, recorte, filtros, etc.
    // Estos serían más complejos y podrían involucrar APIs de terceros o modelos de ML.
}
