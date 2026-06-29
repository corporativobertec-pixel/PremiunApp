package com.premium.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.VideoCameraFront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.premium.app.viewmodels.UploadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(uploadViewModel: UploadViewModel = viewModel()) {
    val context = LocalContext.current
    val isUploading by uploadViewModel.isUploading.collectAsState()
    val uploadProgress by uploadViewModel.uploadProgress.collectAsState()
    val uploadError by uploadViewModel.uploadError.collectAsState()
    val uploadSuccess by uploadViewModel.uploadSuccess.collectAsState()

    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var isVideoSelected by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        selectedMediaUri = it
        isVideoSelected = false
    }

    val pickVideoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        selectedMediaUri = it
        isVideoSelected = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Subir Contenido", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (selectedMediaUri == null) {
                Text(
                    text = "Selecciona un video o una foto para subir",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    ExtendedFloatingActionButton(
                        onClick = { pickVideoLauncher.launch("video/*") },
                        icon = { Icon(Icons.Default.VideoCameraFront, contentDescription = "Subir Video") },
                        text = { Text("Subir Video") },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    ExtendedFloatingActionButton(
                        onClick = { pickImageLauncher.launch("image/*") },
                        icon = { Icon(Icons.Default.AddAPhoto, contentDescription = "Subir Foto") },
                        text = { Text("Subir Foto") },
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            } else {
                Text(
                    text = if (isVideoSelected) "Video Seleccionado" else "Foto Seleccionada",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // Placeholder para la vista previa del contenido seleccionado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Vista previa del contenido", color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Opciones de edición (placeholders)
                Text("Opciones de Edición (IA, Recorte, Filtros, etc.)", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                // Aquí irían los composables para los editores de video/imagen con IA, recorte, filtros, etc.
                // Por simplicidad, solo se muestra un texto.
                Text("Editor de video con IA, editor de imágenes con IA, recorte, filtros, texto, música, ajustes automáticos...", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { selectedMediaUri?.let { uploadViewModel.uploadMedia(context, it, isVideoSelected) } },
                    enabled = !isUploading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CloudUpload, contentDescription = "Publicar")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isUploading) "Subiendo..." else "Publicar")
                }

                if (isUploading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(progress = uploadProgress, modifier = Modifier.fillMaxWidth())
                    Text("Progreso: ${(uploadProgress * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
                }

                uploadError?.let { error ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(error, color = MaterialTheme.colorScheme.error)
                }

                uploadSuccess?.let { uri ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("¡Contenido subido exitosamente! URL: $uri", color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { selectedMediaUri = null }) {
                    Text("Cancelar / Seleccionar otro")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUploadScreen() {
    // UploadScreen()
}
