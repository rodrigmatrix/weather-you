package com.rodrigmatrix.weatheryou.addlocation

import android.app.Activity
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rodrigmatrix.weatheryou.core.viewmodel.ViewModel
import com.rodrigmatrix.weatheryou.domain.usecase.AddLocationUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.GetFamousLocationsUseCase
import com.rodrigmatrix.weatheryou.domain.usecase.SearchLocationUseCase
import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewEffect.LocationAdded
import com.rodrigmatrix.weatheryou.addlocation.AddLocationViewEffect.ShowError
import com.rodrigmatrix.weatheryou.ads.manager.AdsManager
import com.rodrigmatrix.weatheryou.domain.exception.LocationLimitException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import com.rodrigmatrix.weatheryou.components.R
import com.rodrigmatrix.weatheryou.domain.model.City
import com.rodrigmatrix.weatheryou.domain.model.SearchAutocompleteLocation
import kotlinx.coroutines.launch

class AddLocationViewModel(
    private val addLocationUseCase: AddLocationUseCase,
    private val getFamousLocationsUseCase: GetFamousLocationsUseCase,
    private val searchLocationUseCase: SearchLocationUseCase,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val adsManager: AdsManager,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): ViewModel<AddLocationViewState, AddLocationViewEffect>(AddLocationViewState()) {

    init {
        getFamousLocations()
    }

    fun addLocation(
        location: SearchAutocompleteLocation?,
        activity: Activity,
        showAds: Boolean = true,
    ) {
        setState { it.copy(isLoading = true) }
        adsManager.showRewardedInterstitial(
            activity = activity,
            showAd = false,
            flagId = "location_added_rewarded_interstitial_ad_id",
            onRewardGranted = {
                viewModelScope.launch {
                    if (location != null) {
                        addLocationUseCase(
                            location.name,
                            location.lat,
                            location.long,
                            location.countryCode,
                        ).flowOn(coroutineDispatcher)
                            .onStart { setState { it.copy(isLoading = true) } }
                            .catch { exception ->
                                exception.handleError()
                            }
                            .collect {
                                firebaseAnalytics.logEvent("ADDED_LOCATION", bundleOf("name" to location.name))
                                setEffect { LocationAdded }
                            }
                    }
                }
            }
        )
    }

    fun addFamousLocation(city: City, activity: Activity, showAds: Boolean = true) {
        setState { it.copy(isLoading = true) }
        adsManager.showRewardedInterstitial(
            activity = activity,
            showAd = false,
            flagId = "location_added_rewarded_interstitial_ad_id",
            onRewardGranted = {
                viewModelScope.launch {
                    addLocationUseCase(
                        activity.getString(city.name),
                        city.lat,
                        city.long,
                        city.countryCode,
                    ).flowOn(coroutineDispatcher)
                        .onStart { setState { it.copy(isLoading = true) } }
                        .catch { exception ->
                            firebaseAnalytics.logEvent("ADD_FAMOUS_LOCATION_ERROR", bundleOf("error" to exception.localizedMessage))
                            exception.handleError()
                        }
                        .collect {
                            firebaseAnalytics.logEvent("ADDED_FAMOUS_LOCATION", bundleOf("countryCode" to city.countryCode))
                            setEffect { LocationAdded }
                        }
                }
            }
        )
    }

    private fun getFamousLocations() {
        viewModelScope.launch {
            getFamousLocationsUseCase()
                .flowOn(coroutineDispatcher)
                .catch { exception ->
                    exception.logError()
                }
                .collect { famousLocationsList ->
                    setState { it.copy(famousLocationsList = famousLocationsList) }
                }
        }
    }

    fun onQueryChanged(searchText: String) {
        setState {
            it.copy(
                searchText = searchText,
                isLoading = false,
                showKeepTyping = searchText.length < 3 && searchText.isNotEmpty(),
                showClickToSearch = searchText.length >= 3,
                showEmptyState = false,
            )
        }
    }

    fun search() {
        viewModelScope.launch {
            searchLocationUseCase(viewState.value.searchText)
                .flowOn(coroutineDispatcher)
                .onStart {
                    setState {
                        it.copy(isLoading = true)
                    }
                }
                .catch { exception ->
                    firebaseAnalytics.logEvent("ERROR_SEARCHED_LOCATION", bundleOf("error" to exception.localizedMessage))
                    exception.logError()
                }
                .collect { locations ->
                    firebaseAnalytics.logEvent("SEARCHED_LOCATION", bundleOf("name" to viewState.value.searchText))
                    setState {
                        it.copy(
                            locationsList = locations,
                            showEmptyState = locations.isEmpty(),
                            showClickToSearch = false,
                            isLoading = false,
                        )
                    }
                }
        }
    }

    private fun Throwable.handleError() {
        firebaseCrashlytics.recordException(this)
        firebaseAnalytics.logEvent("ADD_LOCATION_ERROR", bundleOf("error" to this.localizedMessage))
        val exception = when (this) {
            is LocationLimitException -> R.string.add_location_limit_error
            else -> R.string.add_location_error
        }
        setEffect { ShowError(exception) }
        setState { it.copy(isLoading = false) }
    }

    private fun Throwable.logError() {
        firebaseCrashlytics.recordException(this)
        setEffect { AddLocationViewEffect.ShowErrorString(this.toString()) }
        setState { it.copy(isLoading = false) }
    }
}
