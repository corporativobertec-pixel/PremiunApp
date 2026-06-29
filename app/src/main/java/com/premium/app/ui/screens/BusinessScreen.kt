package com.premium.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.premium.app.models.Ad
import com.premium.app.models.Business
import com.premium.app.viewmodels.BusinessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessScreen(businessViewModel: BusinessViewModel = viewModel(), userId: String = "sampleUserId") {
    val businessProfile by businessViewModel.businessProfile.collectAsState()
    val businessAds by businessViewModel.businessAds.collectAsState()
    val isLoading by businessViewModel.isLoading.collectAsState()
    val error by businessViewModel.error.collectAsState()

    LaunchedEffect(userId) {
        businessViewModel.loadBusinessProfile(userId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil de Negocio", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
        } else if (businessProfile == null) {
            // Opción para crear un nuevo perfil de negocio si no existe
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No tienes un perfil de negocio. ¿Quieres crear uno?", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /* TODO: Navegar a pantalla de creación de negocio */ }) {
                        Text("Crear Perfil de Negocio")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    businessProfile?.let { business ->
                        // Galería de imágenes del negocio
                        Text("Galería de Imágenes", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                        LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(business.imageUrls) {
                                AsyncImage(
                                    model = it,
                                    contentDescription = "Imagen de negocio",
                                    modifier = Modifier.size(120.dp).clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        // Información del negocio
                        Text(business.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(business.description, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
                        Spacer(modifier = Modifier.height(8.dp))

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Contacto y Horarios
                        BusinessInfoRow(Icons.Default.Phone, business.phone)
                        BusinessInfoRow(Icons.Default.LocationOn, business.address)
                        BusinessInfoRow(Icons.Default.Schedule, business.hours)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Redes Sociales (placeholders)
                        Text("Redes Sociales", style = MaterialTheme.typography.titleSmall, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            IconButton(onClick = { /* TODO: Abrir Facebook */ }) { Icon(Icons.Default.Facebook, contentDescription = "Facebook") }
                            IconButton(onClick = { /* TODO: Abrir Instagram */ }) { Icon(Icons.Default.Instagram, contentDescription = "Instagram") }
                            IconButton(onClick = { /* TODO: Abrir Twitter */ }) { Icon(Icons.Default.Share, contentDescription = "Twitter") }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Anuncios y Promociones
                        Text("Anuncios y Promociones", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                        Button(onClick = { /* TODO: Crear nuevo anuncio */ }, modifier = Modifier.fillMaxWidth()) {
                            Text("Crear Nuevo Anuncio")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (businessAds.isEmpty()) {
                            Text("No hay anuncios activos.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        } else {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                businessAds.forEach { ad ->
                                    AdCard(ad = ad) { /* TODO: Editar anuncio */ }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Estadísticas Básicas (placeholders)
                        Text("Estadísticas Básicas", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            StatisticItem(value = "1.2K", label = "Vistas")
                            StatisticItem(value = "250", label = "Clicks")
                            StatisticItem(value = "50", label = "Compras")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessInfoRow(icon: ImageVector, text: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdCard(ad: Ad, onEditClick: (Ad) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ad.imageUrl.ifEmpty { "https://via.placeholder.com/80" },
                contentDescription = "Imagen del anuncio",
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(ad.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(ad.description, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            }
            IconButton(onClick = { onEditClick(ad) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar anuncio")
            }
        }
    }
}

@Composable
fun StatisticItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBusinessScreen() {
    // BusinessScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewAdCard() {
    val sampleAd = Ad(
        id = "ad1",
        businessId = "business1",
        title = "Oferta Especial de Verano",
        description = "Disfruta de un 20% de descuento en todos nuestros productos durante este mes.",
        imageUrl = "https://via.placeholder.com/150",
        targetUrl = "https://example.com/oferta",
        isActive = true
    )
    AdCard(ad = sampleAd) {}
}
