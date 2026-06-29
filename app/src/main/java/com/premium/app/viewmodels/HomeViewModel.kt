package com.premium.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premium.app.models.Post
import com.premium.app.repository.ContentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val _feedPosts = MutableStateFlow<List<Post>>(emptyList())
    val feedPosts: StateFlow<List<Post>> = _feedPosts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var lastPostId: String? = null
    private val pageSize = 10L

    init {
        loadMorePosts()
    }

    fun loadMorePosts() {
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val newPosts = contentRepository.getPaginatedFeed(pageSize, lastPostId)
                _feedPosts.value = _feedPosts.value + newPosts
                lastPostId = newPosts.lastOrNull()?.id
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun likePost(postId: String, userId: String) {
        viewModelScope.launch {
            try {
                contentRepository.addLike(postId, userId)
                // Actualizar el estado local de la publicación
                _feedPosts.value = _feedPosts.value.map { post ->
                    if (post.id == postId) post.copy(likes = post.likes + 1) else post
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            try {
                contentRepository.deletePost(postId)
                _feedPosts.value = _feedPosts.value.filter { it.id != postId }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    // Métodos para comentar, compartir, guardar, seguir, etc.
    // Estos métodos interactuarían con ContentRepository de manera similar.
}
