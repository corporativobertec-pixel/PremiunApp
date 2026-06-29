package com.premium.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

// ViewModel de ejemplo para BusinessScreen
class BusinessViewModel : androidx.lifecycle.ViewModel() {
    var businessName by mutableStateOf("")
    var businessAddress by mutableStateOf("")
    var businessPhone by mutableStateOf("")
    var businessEmail by mutableStateOf("")
    var businessHours by mutableStateOf("") // Usaremos String para simplificar
    var socialMediaLink1 by mutableStateOf("")
    var socialMediaLink2 by mutableStateOf("")
    var productCount by mutableStateOf(0)
    var adCount by mutableStateOf(0)
    var promoCount by mutableStateOf(0)

    fun updateBusinessName(name: String) { businessName = name }
    fun updateBusinessAddress(address: String) { businessAddress = address }
    fun updateBusinessPhone(phone: String) { businessPhone = phone }
    fun updateBusinessEmail(email: String) { businessEmail = email }
    fun updateBusinessHours(hours: String) { businessHours = hours }
    fun updateSocialMediaLink1(link: String) { socialMediaLink1 = link }
    fun updateSocialMediaLink2(link: String) { socialMediaLink2 = link }

    fun createAd() { adCount++ }
    fun createPromo() { promoCount++ }
    fun addProduct() { productCount++ }

    fun saveBusinessInfo() {
        // Lógica para guardar la información del negocio
        println("Guardando información del negocio: $businessName")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessScreen(businessViewModel: BusinessViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Negocio") },
                actions = {
                    IconButton(onClick = { businessViewModel.saveBusinessInfo() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Guardar Información")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Configuración de tu Negocio",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = businessViewModel.businessName,
                onValueChange = { businessViewModel.updateBusinessName(it) },
                label = { Text("Nombre del Negocio") },
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = businessViewModel.businessAddress,
                onValueChange = { businessViewModel.updateBusinessAddress(it) },
                label = { Text("Dirección") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = businessViewModel.businessPhone,
                onValueChange = { businessViewModel.updateBusinessPhone(it) },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = businessViewModel.businessEmail,
                onValueChange = { businessViewModel.updateBusinessEmail(it) },
                label = { Text("Email de Contacto") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = businessViewModel.businessHours,
                onValueChange = { businessViewModel.updateBusinessHours(it) },
                label = { Text("Horarios (Ej: Lun-Vie 9-18)") },
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Horarios") }, // Reemplazo para Schedule
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Redes Sociales",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            OutlinedTextField(
                value = businessViewModel.socialMediaLink1,
                onValueChange = { businessViewModel.updateSocialMediaLink1(it) },
                label = { Text("Enlace Red Social 1 (Ej: Facebook)") },
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Red Social 1") }, // Reemplazo para Facebook
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = businessViewModel.socialMediaLink2,
                onValueChange = { businessViewModel.updateSocialMediaLink2(it) },
                label = { Text("Enlace Red Social 2 (Ej: Instagram)") },
                leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Red Social 2") }, // Reemplazo para Instagram
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button(onClick = { businessViewModel.createAd() }) {
                    Icon(Icons.Default.Add, contentDescription = "Crear Anuncio")
                    Spacer(Modifier.width(8.dp))
                    Text("Anuncios (${businessViewModel.adCount})")
                }
                Button(onClick = { businessViewModel.createPromo() }) {
                    Icon(Icons.Default.Add, contentDescription = "Crear Promoción")
                    Spacer(Modifier.width(8.dp))
                    Text("Promociones (${businessViewModel.promoCount})")
                }
            }
            Button(onClick = { businessViewModel.addProduct() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Producto")
                Spacer(Modifier.width(8.dp))
                Text("Productos (${businessViewModel.productCount})")
            }

            // Placeholder para Galería y Estadísticas
            Text(
                text = "Galería de Imágenes (próximamente)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Estadísticas del Negocio (próximamente)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBusinessScreen() {
    MaterialTheme {
        BusinessScreen()
    }
}
