package com.premium.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

data class VideoItem(
    val id: String,
    val title: String,
    val description: String,
    val owner: String,
    val isOwner: Boolean = false,
    var isLiked: Boolean = false,
    var likesCount: Int = 0
)

class HomeViewModel : androidx.lifecycle.ViewModel() {
    private val _videos = mutableStateListOf<VideoItem>()
    val videos: List<VideoItem> = _videos

    var isLoading by mutableStateOf(false)
    var page by mutableStateOf(0)
    val pageSize = 5

    init {
        loadMoreVideos()
    }

    fun loadMoreVideos() {
        if (isLoading) return
        isLoading = true
        // Simular carga de red
        androidx.lifecycle.viewModelScope.launch {
            delay(1000) // Simular retardo de red
            val newVideos = (0 until pageSize).map { i ->
                val videoId = "video_${page * pageSize + i}"
                VideoItem(
                    id = videoId,
                    title = "Video Título ${page * pageSize + i}",
                    description = "Descripción del video ${page * pageSize + i}. Contenido interesante.",
                    owner = if (i % 2 == 0) "UsuarioActual" else "OtroUsuario",
                    isOwner = (i % 2 == 0),
                    likesCount = (10..100).random()
                )
            }
            _videos.addAll(newVideos)
            page++
            isLoading = false
        }
    }

    fun toggleLike(videoId: String) {
        val index = _videos.indexOfFirst { it.id == videoId }
        if (index != -1) {
            val currentVideo = _videos[index]
            _videos[index] = currentVideo.copy(
                isLiked = !currentVideo.isLiked,
                likesCount = if (currentVideo.isLiked) currentVideo.likesCount - 1 else currentVideo.likesCount + 1
            )
        }
    }

    fun deleteVideo(videoId: String) {
        _videos.removeIf { it.id == videoId }
    }

    fun followUser(owner: String) {
        // Lógica para seguir usuario
        println("Siguiendo a $owner")
    }

    fun shareVideo(videoId: String) {
        // Lógica para compartir video
        println("Compartiendo video $videoId")
    }

    fun saveVideo(videoId: String) {
        // Lógica para guardar video
        println("Guardando video $videoId")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size }
            .collect { firstVisibleItemIndex ->
                if (firstVisibleItemIndex >= homeViewModel.videos.size - homeViewModel.pageSize / 2 && !homeViewModel.isLoading) {
                    homeViewModel.loadMoreVideos()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Premium App") }
            )
        }
    ) {\ paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(homeViewModel.videos) { index, video ->
                VideoCard(video = video, homeViewModel = homeViewModel)
                if (index == homeViewModel.videos.lastIndex && homeViewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(top = 16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCard(video: VideoItem, homeViewModel: HomeViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = video.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = video.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Propietario: ${video.owner}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like
                IconButton(onClick = { homeViewModel.toggleLike(video.id) }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Me gusta",
                        tint = if (video.isLiked) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                }
                Text("${video.likesCount}")

                // Comentar (usando Star como reemplazo)
                IconButton(onClick = { /* Lógica de comentario */ }) {
                    Icon(Icons.Default.Star, contentDescription = "Comentar")
                }

                // Compartir
                IconButton(onClick = { homeViewModel.shareVideo(video.id) }) {
                    Icon(Icons.Default.Share, contentDescription = "Compartir")
                }

                // Guardar (usando Star como reemplazo)
                IconButton(onClick = { homeViewModel.saveVideo(video.id) }) {
                    Icon(Icons.Default.Star, contentDescription = "Guardar")
                }

                // Seguir
                IconButton(onClick = { homeViewModel.followUser(video.owner) }) {
                    Icon(Icons.Default.Person, contentDescription = "Seguir")
                }

                // Eliminar (solo si es propietario)
                if (video.isOwner) {
                    IconButton(onClick = { homeViewModel.deleteVideo(video.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    MaterialTheme {
        HomeScreen()
    }
}
