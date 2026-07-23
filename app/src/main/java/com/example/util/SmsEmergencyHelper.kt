package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.example.data.EmergencyContact
import com.example.data.VehicleTrip

object SmsEmergencyHelper {

    fun generateSosMessage(
        address: String,
        lat: Double,
        lng: Double,
        isOfflineMode: Boolean
    ): String {
        val mapsUrl = "https://maps.google.com/?q=$lat,$lng"
        val modeText = if (isOfflineMode) "[OFFLINE SMS ALERT] " else "[EMERGENCY SOS ALERT] "
        return "$modeText I need urgent help! My current location: $address ($mapsUrl). Please send assistance immediately!"
    }

    fun generateVehicleTripMessage(trip: VehicleTrip, userPhone: String): String {
        return " [TRAVEL SAFETY ALERT] I am traveling in a ${trip.vehicleType} " +
                "(Reg No: ${trip.vehicleNumber}). Destination: '${trip.destination.ifEmpty { "En Route" }}'. " +
                "Driver Info: '${trip.driverDetails.ifEmpty { "N/A" }}'. " +
                "Please monitor my journey! Emergency Contact: $userPhone"
    }

    fun sendDirectSmsOrLaunchIntent(
        context: Context,
        contacts: List<EmergencyContact>,
        message: String
    ): Int {
        if (contacts.isEmpty()) {
            Toast.makeText(context, "No emergency contacts configured!", Toast.LENGTH_LONG).show()
            return 0
        }

        var sentCount = 0
        val phoneNumbers = contacts.map { it.phoneNumber }.filter { it.isNotBlank() }

        try {
            val smsManager = context.getSystemService(SmsManager::class.java)
            for (phone in phoneNumbers) {
                try {
                    smsManager.sendTextMessage(phone, null, message, null, null)
                    sentCount++
                } catch (e: Exception) {
                    Log.e("SmsEmergencyHelper", "Direct SMS failed for $phone, launching Intent fallback", e)
                }
            }
        } catch (e: Exception) {
            Log.e("SmsEmergencyHelper", "SmsManager unavailable", e)
        }

        // If direct SMS fails or for fallback display
        if (sentCount == 0 && phoneNumbers.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:" + phoneNumbers.joinToString(";"))
                putExtra("sms_body", message)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(intent)
                sentCount = phoneNumbers.size
            } catch (e: Exception) {
                Toast.makeText(context, "Could not open messaging app", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                context,
                "Emergency SMS sent to $sentCount contact(s)!",
                Toast.LENGTH_LONG
            ).show()
        }

        return sentCount
    }
}
