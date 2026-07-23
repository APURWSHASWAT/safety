package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SafetyDao {
    // Contacts Queries
    @Query("SELECT * FROM emergency_contacts ORDER BY isPrimary DESC, name ASC")
    fun getAllContacts(): Flow<List<EmergencyContact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: EmergencyContact)

    @Update
    suspend fun updateContact(contact: EmergencyContact)

    @Delete
    suspend fun deleteContact(contact: EmergencyContact)

    @Query("SELECT COUNT(*) FROM emergency_contacts")
    suspend fun getContactsCount(): Int

    // Vehicle Trips Queries
    @Query("SELECT * FROM vehicle_trips ORDER BY timestamp DESC")
    fun getAllTrips(): Flow<List<VehicleTrip>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: VehicleTrip): Long

    @Query("UPDATE vehicle_trips SET status = :status WHERE id = :tripId")
    suspend fun updateTripStatus(tripId: Int, status: String)

    // Alert Logs Queries
    @Query("SELECT * FROM alert_logs ORDER BY timestamp DESC")
    fun getAllAlertLogs(): Flow<List<AlertLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlertLog(log: AlertLog)
}
