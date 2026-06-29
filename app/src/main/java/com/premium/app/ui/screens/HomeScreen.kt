package com.premium.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.premium.app.models.Post
import com.premium.app.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val feedPosts by homeViewModel.feedPosts.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val listState = rememberLazyListState()

    // Cargar más posts cuando el usuario se acerca al final de la lista
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null && lastVisibleIndex >= feedPosts.size - 5 && !isLoading) {
                    homeViewModel.loadMorePosts()
                }
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Premium Feed", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(state = listState, contentPadding = PaddingValues(8.dp)) {
                items(feedPosts, key = { it.id }) {
                    VideoPostCard(post = it, onLikeClick = { postId ->
                        // Simulación de userId
                        homeViewModel.likePost(postId, "currentUserId")
                    }, onDeleteClick = { postId ->
                        homeViewModel.deletePost(postId)
                    })
                }
                item {
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPostCard(
    post: Post,
    onLikeClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            // Encabezado del post (creador y botón X)
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar del creador (placeholder)
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Creador: ${post.userId}", fontWeight = FontWeight.Medium)
                }
                if (post.isOwner) {
                    IconButton(onClick = { onDeleteClick(post.id) }) {
                        Icon(Icons.Default.Close, contentDescription = "Eliminar post")
                    }
                }
            }

            // Miniatura del video
            AsyncImage(
                model = post.thumbnailUrl.ifEmpty { "https://via.placeholder.com/400x200?text=Video" },
                contentDescription = "Miniatura del video",
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )

            // Descripción y vistas
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = post.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text(text = "${post.views} reproducciones", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }

            // Botones de interacción
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionIcon(Icons.Default.Favorite, "Me gusta (${post.likes})") { onLikeClick(post.id) }
                ActionIcon(Icons.Default.Comment, "Comentar (${post.comments})") { /* TODO: Implementar */ }
                ActionIcon(Icons.Default.Share, "Compartir (${post.shares})") { /* TODO: Implementar */ }
                ActionIcon(Icons.Default.Bookmark, "Guardar") { /* TODO: Implementar */ }
                ActionIcon(Icons.Default.PersonAdd, "Seguir") { /* TODO: Implementar */ }
            }
        }
    }
}

@Composable
fun ActionIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick)) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(24.dp))
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    // Para la vista previa, se necesita un ViewModel simulado o un HiltViewModelFactory
    // Esto es solo un placeholder para la estructura.
    // HomeScreen()
}

@Preview(showBackground = true)
@Composable
fun PreviewVideoPostCard() {
    val samplePost = Post(
        id = "1",
        userId = "usuario_ejemplo",
        videoUrl = "",
        thumbnailUrl = "https://picsum.photos/400/200",
        description = "Este es un video de ejemplo con una descripción interesante.",
        likes = 150,
        comments = 25,
        shares = 10,
        views = 1200,
        isOwner = true
    )
    VideoPostCard(post = samplePost, onLikeClick = {}, onDeleteClick = {})
}
