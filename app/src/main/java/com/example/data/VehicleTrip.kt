package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_trips")
data class VehicleTrip(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val vehicleNumber: String,
    val vehicleType: String, // Cab, Auto, Bus, Train, Private
    val driverDetails: String = "",
    val destination: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "ACTIVE", // ACTIVE, COMPLETED, ALERT_TRIGGERED
    val sharedWithContactsCount: Int = 0
)
