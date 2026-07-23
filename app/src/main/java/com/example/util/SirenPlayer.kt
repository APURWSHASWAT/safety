package com.example.util

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log

class SirenPlayer {
    private var toneGenerator: ToneGenerator? = null
    private var isPlaying = false

    fun startSiren() {
        if (isPlaying) return
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 10000)
            isPlaying = true
        } catch (e: Exception) {
            Log.e("SirenPlayer", "Failed to start siren tone", e)
        }
    }

    fun stopSiren() {
        try {
            toneGenerator?.stopTone()
            toneGenerator?.release()
            toneGenerator = null
            isPlaying = false
        } catch (e: Exception) {
            Log.e("SirenPlayer", "Error stopping siren", e)
        }
    }

    fun isPlaying(): Boolean = isPlaying
}
