package com.premium.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.premium.app.viewmodels.ProfileViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = viewModel()) {
    val profilePictureUrl by profileViewModel.profilePictureUrl.collectAsState()
    val userName by profileViewModel.userName.collectAsState()
    val bio by profileViewModel.bio.collectAsState()
    val followersCount by profileViewModel.followersCount.collectAsState()
    val followingCount by profileViewModel.followingCount.collectAsState()
    val postsCount by profileViewModel.postsCount.collectAsState()
    val nameChangeRemainingTime by profileViewModel.nameChangeRemainingTime.collectAsState()
    val usernameChangeRemainingTime by profileViewModel.usernameChangeRemainingTime.collectAsState()

    var showEditBioDialog by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showEditUsernameDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Perfil", style = MaterialTheme.typography.titleLarge) },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de perfil
            Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.LightGray)) {
                AsyncImage(
                    model = profilePictureUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar foto de perfil",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp)
                        .clickable { /* TODO: Implementar cambio de foto */ },
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de usuario y botón de edición
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(userName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showEditNameDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar nombre")
                }
            }
            Text("@$userName", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            // Biografía y botón de edición
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(bio, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showEditBioDialog = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar biografía")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas de perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ProfileStat(count = postsCount, label = "Publicaciones")
                ProfileStat(count = followersCount, label = "Seguidores")
                ProfileStat(count = followingCount, label = "Seguidos")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Biblioteca de publicaciones guardadas (placeholder)
            Button(onClick = { /* TODO: Navegar a biblioteca */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Biblioteca de Publicaciones Guardadas")
            }

            // Diálogos de edición
            if (showEditBioDialog) {
                EditBioDialog(currentBio = bio, onDismiss = { showEditBioDialog = false }) {
                    profileViewModel.updateBio(it)
                    showEditBioDialog = false
                }
            }

            if (showEditNameDialog) {
                EditNameDialog(currentName = userName, remainingTime = nameChangeRemainingTime, onDismiss = { showEditNameDialog = false }) {
                    profileViewModel.updateName(it)
                    showEditNameDialog = false
                }
            }

            if (showEditUsernameDialog) {
                EditUsernameDialog(currentUsername = userName, remainingTime = usernameChangeRemainingTime, onDismiss = { showEditUsernameDialog = false }) {
                    profileViewModel.updateUsername(it)
                    showEditUsernameDialog = false
                }
            }
        }
    }
}

@Composable
fun ProfileStat(count: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBioDialog(currentBio: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var newBio by remember { mutableStateOf(currentBio) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Biografía") },
        text = {
            TextField(
                value = newBio,
                onValueChange = { newBio = it },
                label = { Text("Biografía") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = { onSave(newBio) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNameDialog(currentName: String, remainingTime: Long, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var newName by remember { mutableStateOf(currentName) }
    val canChange = remainingTime == 0L
    val timeString = formatMillisToTime(remainingTime)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Nombre") },
        text = {
            Column {
                TextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = canChange
                )
                if (!canChange) {
                    Text(
                        "Puedes cambiar tu nombre en $timeString",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(newName) }, enabled = canChange) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditUsernameDialog(currentUsername: String, remainingTime: Long, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var newUsername by remember { mutableStateOf(currentUsername) }
    val canChange = remainingTime == 0L
    val timeString = formatMillisToTime(remainingTime)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Nombre de Usuario") },
        text = {
            Column {
                TextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("Nombre de Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = canChange
                )
                if (!canChange) {
                    Text(
                        "Puedes cambiar tu nombre de usuario en $timeString",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(newUsername) }, enabled = canChange) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

fun formatMillisToTime(millis: Long): String {
    if (millis <= 0) return "ahora"

    val days = TimeUnit.MILLISECONDS.toDays(millis)
    val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

    return when {
        days > 0 -> String.format("%d días, %d horas", days, hours)
        hours > 0 -> String.format("%d horas, %d minutos", hours, minutes)
        minutes > 0 -> String.format("%d minutos, %d segundos", minutes, seconds)
        else -> String.format("%d segundos", seconds)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    // ProfileScreen()
}
