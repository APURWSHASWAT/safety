package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SafetyRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val dao = db.safetyDao()

    private val prefs: SharedPreferences =
        context.getSharedPreferences("women_safety_prefs", Context.MODE_PRIVATE)

    // User Login State
    private val _isLoggedIn = MutableStateFlow(prefs.getBoolean("is_logged_in", false))
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userPhone = MutableStateFlow(prefs.getString("user_phone", "") ?: "")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    private val _isPowerKeyShortcutEnabled =
        MutableStateFlow(prefs.getBoolean("power_key_shortcut", true))
    val isPowerKeyShortcutEnabled: StateFlow<Boolean> = _isPowerKeyShortcutEnabled.asStateFlow()

    val contacts: Flow<List<EmergencyContact>> = dao.getAllContacts()
    val trips: Flow<List<VehicleTrip>> = dao.getAllTrips()
    val alertLogs: Flow<List<AlertLog>> = dao.getAllAlertLogs()

    suspend fun checkAndSeedDefaultContacts() {
        if (dao.getContactsCount() == 0) {
            dao.insertContact(
                EmergencyContact(
                    name = "Women Helpline (National)",
                    phoneNumber = "1091",
                    relationship = "Official Helpline",
                    isPrimary = true
                )
            )
            dao.insertContact(
                EmergencyContact(
                    name = "Police Emergency",
                    phoneNumber = "112",
                    relationship = "Police Control Room",
                    isPrimary = true
                )
            )
            dao.insertContact(
                EmergencyContact(
                    name = "Mom / Primary Contact",
                    phoneNumber = "+91 98765 43210",
                    relationship = "Family",
                    isPrimary = true
                )
            )
        }
    }

    fun loginUser(phoneNumber: String) {
        prefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_phone", phoneNumber)
            .apply()
        _isLoggedIn.value = true
        _userPhone.value = phoneNumber
    }

    fun logoutUser() {
        prefs.edit()
            .putBoolean("is_logged_in", false)
            .apply()
        _isLoggedIn.value = false
    }

    fun togglePowerKeyShortcut(enabled: Boolean) {
        prefs.edit().putBoolean("power_key_shortcut", enabled).apply()
        _isPowerKeyShortcutEnabled.value = enabled
    }

    suspend fun addContact(contact: EmergencyContact) = dao.insertContact(contact)
    suspend fun updateContact(contact: EmergencyContact) = dao.updateContact(contact)
    suspend fun deleteContact(contact: EmergencyContact) = dao.deleteContact(contact)

    suspend fun addTrip(trip: VehicleTrip): Long = dao.insertTrip(trip)
    suspend fun updateTripStatus(tripId: Int, status: String) = dao.updateTripStatus(tripId, status)

    suspend fun logAlert(log: AlertLog) = dao.insertAlertLog(log)
}
