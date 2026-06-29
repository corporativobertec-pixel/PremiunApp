package com.premium.app.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.premium.app.models.Ad
import com.premium.app.models.Business
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val businessCollection = firestore.collection("businesses")
    private val adsCollection = firestore.collection("ads")

    /**
     * Crea o actualiza un perfil de negocio.
     * @param business El objeto Business a guardar.
     */
    suspend fun saveBusinessProfile(business: Business) {
        businessCollection.document(business.id).set(business).await()
    }

    /**
     * Obtiene un perfil de negocio por su ID de usuario.
     * @param userId El ID del usuario propietario del negocio.
     * @return El objeto Business, o null si no se encuentra.
     */
    suspend fun getBusinessProfile(userId: String): Business? {
        return businessCollection.whereEqualTo("userId", userId).get().await()
            .documents.firstOrNull()?.toObject(Business::class.java)
    }

    /**
     * Crea o actualiza un anuncio para un negocio.
     * @param ad El objeto Ad a guardar.
     */
    suspend fun saveAd(ad: Ad) {
        adsCollection.document(ad.id).set(ad).await()
    }

    /**
     * Obtiene todos los anuncios de un negocio.
     * @param businessId El ID del negocio.
     * @return Una lista de anuncios.
     */
    suspend fun getAdsForBusiness(businessId: String): List<Ad> {
        return adsCollection.whereEqualTo("businessId", businessId).get().await()
            .documents.mapNotNull { it.toObject(Ad::class.java) }
    }

    // Métodos para estadísticas básicas, promociones, etc. serían similares
}
