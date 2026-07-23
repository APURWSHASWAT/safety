package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_logs")
data class AlertLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val alertType: String, // SOS_BUTTON, POWER_KEY_5X, VEHICLE_ALERT, OFFLINE_SMS
    val timestamp: Long = System.currentTimeMillis(),
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val contactsNotifiedCount: Int,
    val isOfflineMode: Boolean
)
