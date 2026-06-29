package com.premium.app.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.premium.app.models.Comment
import com.premium.app.models.Post
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val postsCollection = firestore.collection("posts")
    private val commentsCollection = firestore.collection("comments")

    /**
     * Obtiene un feed paginado de publicaciones.
     * @param pageSize El número de publicaciones por página.
     * @param lastPostId El ID de la última publicación cargada para la paginación.
     * @return Una lista de publicaciones.
     */
    suspend fun getPaginatedFeed(pageSize: Long, lastPostId: String?): List<Post> {
        var query: Query = postsCollection.orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(pageSize)

        if (lastPostId != null) {
            val lastPostDocument = postsCollection.document(lastPostId).get().await()
            query = query.startAfter(lastPostDocument)
        }

        return query.get().await().documents.mapNotNull { it.toObject(Post::class.java) }
    }

    /**
     * Añade un like a una publicación.
     * @param postId El ID de la publicación.
     * @param userId El ID del usuario que da like.
     */
    suspend fun addLike(postId: String, userId: String) {
        // Implementación para añadir like (ej. transacción para actualizar contador y añadir usuario a subcolección)
        firestore.runTransaction { transaction ->
            val postRef = postsCollection.document(postId)
            val postSnapshot = transaction.get(postRef)
            val currentLikes = postSnapshot.getLong("likes") ?: 0
            transaction.update(postRef, "likes", currentLikes + 1)
            // También podrías añadir el userId a una subcolección 'likes' dentro del post
            null
        }.await()
    }

    /**
     * Añade un comentario a una publicación.
     * @param comment El objeto comentario a añadir.
     */
    suspend fun addComment(comment: Comment) {
        commentsCollection.add(comment).await()
        // Opcional: actualizar el contador de comentarios en la publicación
    }

    /**
     * Elimina una publicación.
     * @param postId El ID de la publicación a eliminar.
     */
    suspend fun deletePost(postId: String) {
        postsCollection.document(postId).delete().await()
    }

    /**
     * Obtiene los comentarios de una publicación.
     * @param postId El ID de la publicación.
     * @return Una lista de comentarios.
     */
    suspend fun getCommentsForPost(postId: String): List<Comment> {
        return commentsCollection.whereEqualTo("postId", postId)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get().await().documents.mapNotNull { it.toObject(Comment::class.java) }
    }

    // Métodos para compartir, guardar, seguir, etc. serían similares
}
