package com.example.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class PoliceStation(
    val id: String,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val latitude: Double,
    val longitude: Double,
    val is24x7: Boolean = true,
    val distanceKm: Double = 0.0
)

object PoliceStationProvider {
    val sampleStations = listOf(
        PoliceStation(
            id = "ps_1",
            name = "Central City Police Station & Women Desk",
            address = "Block C, Connaught Circus, Central Zone",
            phoneNumber = "011-23340000",
            latitude = 28.6315,
            longitude = 77.2167
        ),
        PoliceStation(
            id = "ps_2",
            name = "Women Safety & Special Crime Cell",
            address = "Nanakpura, Moti Bagh, South District",
            phoneNumber = "1091",
            latitude = 28.5833,
            longitude = 77.1667
        ),
        PoliceStation(
            id = "ps_3",
            name = "Metro Rail & Transit Police Station",
            address = "Platform Level 1, Rajiv Chowk Interchange",
            phoneNumber = "011-23412345",
            latitude = 28.6328,
            longitude = 77.2197
        ),
        PoliceStation(
            id = "ps_4",
            name = "West District Women Police Post",
            address = "Community Center, Rajouri Garden",
            phoneNumber = "011-25419999",
            latitude = 28.6482,
            longitude = 77.1221
        ),
        PoliceStation(
            id = "ps_5",
            name = "Integrated Highway Patrol & Emergency Desk",
            address = "NH-48 Toll Plaza Control Room",
            phoneNumber = "112",
            latitude = 28.5000,
            longitude = 77.0800
        )
    )

    fun getNearbyStations(userLat: Double, userLng: Double): List<PoliceStation> {
        return sampleStations.map { station ->
            val dist = calculateDistanceKm(userLat, userLng, station.latitude, station.longitude)
            station.copy(distanceKm = Math.round(dist * 10.0) / 10.0)
        }.sortedBy { it.distanceKm }
    }

    private fun calculateDistanceKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
