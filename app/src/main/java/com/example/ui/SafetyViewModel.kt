package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AlertLog
import com.example.data.EmergencyContact
import com.example.data.SafetyRepository
import com.example.data.VehicleTrip
import com.example.util.PoliceStation
import com.example.util.PoliceStationProvider
import com.example.util.SirenPlayer
import com.example.util.SmsEmergencyHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LocationState(
    val address: String = "Connaught Circus, Central District, Delhi",
    val latitude: Double = 28.6315,
    val longitude: Double = 77.2167,
    val isGpsActive: Boolean = true,
    val isOfflineMode: Boolean = false,
    val networkQuality: String = "Good Network (4G/5G)" // Good Network, Poor Network, Offline Mode
)

class SafetyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SafetyRepository(application)
    private val sirenPlayer = SirenPlayer()

    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
    val userPhone: StateFlow<String> = repository.userPhone
    val isPowerKeyShortcutEnabled: StateFlow<Boolean> = repository.isPowerKeyShortcutEnabled

    val contacts: StateFlow<List<EmergencyContact>> = repository.contacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trips: StateFlow<List<VehicleTrip>> = repository.trips
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alertLogs: StateFlow<List<AlertLog>> = repository.alertLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Location & Network state
    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    // SOS State
    private val _isSosActive = MutableStateFlow(false)
    val isSosActive: StateFlow<Boolean> = _isSosActive.asStateFlow()

    private val _sosTriggerSource = MutableStateFlow("SOS BUTTON")
    val sosTriggerSource: StateFlow<String> = _sosTriggerSource.asStateFlow()

    private val _isSirenOn = MutableStateFlow(false)
    val isSirenOn: StateFlow<Boolean> = _isSirenOn.asStateFlow()

    private val _powerKeyPressCount = MutableStateFlow(0)
    val powerKeyPressCount: StateFlow<Int> = _powerKeyPressCount.asStateFlow()

    // Nearby Police Stations
    private val _nearbyPoliceStations = MutableStateFlow<List<PoliceStation>>(emptyList())
    val nearbyPoliceStations: StateFlow<List<PoliceStation>> = _nearbyPoliceStations.asStateFlow()

    // Vehicle trip sharing status message
    private val _tripShareStatus = MutableStateFlow<String?>(null)
    val tripShareStatus: StateFlow<String?> = _tripShareStatus.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndSeedDefaultContacts()
            updateNearbyPoliceStations()
        }
    }

    fun loginWithMobileAndOtp(phone: String) {
        repository.loginUser(phone)
    }

    fun logout() {
        repository.logoutUser()
    }

    fun togglePowerKeyShortcut(enabled: Boolean) {
        repository.togglePowerKeyShortcut(enabled)
    }

    fun setNetworkMode(isOffline: Boolean, quality: String) {
        _locationState.value = _locationState.value.copy(
            isOfflineMode = isOffline,
            networkQuality = quality
        )
    }

    fun updateMockLocation(address: String, lat: Double, lng: Double) {
        _locationState.value = _locationState.value.copy(
            address = address,
            latitude = lat,
            longitude = lng
        )
        updateNearbyPoliceStations()
    }

    private fun updateNearbyPoliceStations() {
        val currentLoc = _locationState.value
        _nearbyPoliceStations.value = PoliceStationProvider.getNearbyStations(
            currentLoc.latitude,
            currentLoc.longitude
        )
    }

    fun registerPowerKeyPress() {
        val current = _powerKeyPressCount.value + 1
        _powerKeyPressCount.value = current
        if (current >= 5) {
            triggerSos("POWER BUTTON 5X SHORTCUT")
            _powerKeyPressCount.value = 0
        } else {
            viewModelScope.launch {
                delay(3000)
                if (_powerKeyPressCount.value > 0) {
                    _powerKeyPressCount.value = 0
                }
            }
        }
    }

    fun triggerSos(source: String = "SOS BUTTON") {
        _isSosActive.value = true
        _sosTriggerSource.value = source
        _isSirenOn.value = true
        sirenPlayer.startSiren()

        viewModelScope.launch {
            val loc = _locationState.value
            val currentContacts = contacts.value
            val message = SmsEmergencyHelper.generateSosMessage(
                address = loc.address,
                lat = loc.latitude,
                lng = loc.longitude,
                isOfflineMode = loc.isOfflineMode
            )

            val countSent = SmsEmergencyHelper.sendDirectSmsOrLaunchIntent(
                context = getApplication(),
                contacts = currentContacts,
                message = message
            )

            repository.logAlert(
                AlertLog(
                    alertType = source,
                    address = loc.address,
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    contactsNotifiedCount = countSent,
                    isOfflineMode = loc.isOfflineMode
                )
            )
        }
    }

    fun cancelSos() {
        _isSosActive.value = false
        _isSirenOn.value = false
        sirenPlayer.stopSiren()
    }

    fun toggleSiren() {
        val newState = !_isSirenOn.value
        _isSirenOn.value = newState
        if (newState) {
            sirenPlayer.startSiren()
        } else {
            sirenPlayer.stopSiren()
        }
    }

    fun shareVehicleTrip(
        vehicleNumber: String,
        vehicleType: String,
        destination: String,
        driverDetails: String
    ) {
        viewModelScope.launch {
            val currentContacts = contacts.value
            val trip = VehicleTrip(
                vehicleNumber = vehicleNumber,
                vehicleType = vehicleType,
                destination = destination,
                driverDetails = driverDetails,
                status = "ACTIVE",
                sharedWithContactsCount = currentContacts.size
            )
            repository.addTrip(trip)

            val alertMessage = SmsEmergencyHelper.generateVehicleTripMessage(
                trip = trip,
                userPhone = userPhone.value
            )

            val countSent = SmsEmergencyHelper.sendDirectSmsOrLaunchIntent(
                context = getApplication(),
                contacts = currentContacts,
                message = alertMessage
            )

            _tripShareStatus.value = "Vehicle details ($vehicleNumber) forwarded to $countSent emergency contact(s)!"
            delay(4000)
            _tripShareStatus.value = null
        }
    }

    fun addContact(name: String, phone: String, relationship: String, isPrimary: Boolean) {
        viewModelScope.launch {
            repository.addContact(
                EmergencyContact(
                    name = name,
                    phoneNumber = phone,
                    relationship = relationship,
                    isPrimary = isPrimary
                )
            )
        }
    }

    fun deleteContact(contact: EmergencyContact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sirenPlayer.stopSiren()
    }
}
