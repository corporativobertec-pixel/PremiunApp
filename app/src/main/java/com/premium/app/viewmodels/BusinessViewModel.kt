package com.premium.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premium.app.models.Ad
import com.premium.app.models.Business
import com.premium.app.repository.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val businessRepository: BusinessRepository
) : ViewModel() {

    private val _businessProfile = MutableStateFlow<Business?>(null)
    val businessProfile: StateFlow<Business?> = _businessProfile

    private val _businessAds = MutableStateFlow<List<Ad>>(emptyList())
    val businessAds: StateFlow<List<Ad>> = _businessAds

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadBusinessProfile(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _businessProfile.value = businessRepository.getBusinessProfile(userId)
                _businessProfile.value?.id?.let { businessId ->
                    _businessAds.value = businessRepository.getAdsForBusiness(businessId)
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar el perfil de negocio: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveBusinessProfile(business: Business) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                businessRepository.saveBusinessProfile(business)
                _businessProfile.value = business
            } catch (e: Exception) {
                _error.value = "Error al guardar el perfil de negocio: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveAd(ad: Ad) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                businessRepository.saveAd(ad)
                // Recargar anuncios o actualizar la lista localmente
                _businessProfile.value?.id?.let { businessId ->
                    _businessAds.value = businessRepository.getAdsForBusiness(businessId)
                }
            } catch (e: Exception) {
                _error.value = "Error al guardar el anuncio: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Métodos para estadísticas básicas, promociones, etc.
    // Estos serían más complejos y podrían involucrar APIs de terceros o modelos de ML.
}
