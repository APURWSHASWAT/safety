package com.example.util

import android.os.SystemClock
import android.util.Log

class PowerButtonDetector(
    private val requiredPresses: Int = 5,
    private val timeWindowMs: Long = 3000L,
    private val onTriggerSos: () -> Unit
) {
    private val pressTimestamps = mutableListOf<Long>()

    fun registerPress(): Int {
        val now = SystemClock.elapsedRealtime()
        
        // Remove timestamps older than time window
        pressTimestamps.removeAll { now - it > timeWindowMs }
        pressTimestamps.add(now)

        val count = pressTimestamps.size
        Log.d("PowerButtonDetector", "Power button press count: $count / $requiredPresses")

        if (count >= requiredPresses) {
            pressTimestamps.clear()
            onTriggerSos()
        }
        return count
    }

    fun getCurrentCount(): Int {
        val now = SystemClock.elapsedRealtime()
        pressTimestamps.removeAll { now - it > timeWindowMs }
        return pressTimestamps.size
    }

    fun reset() {
        pressTimestamps.clear()
    }
}
