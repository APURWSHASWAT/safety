package com.example.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SignalCellularOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.PinkPrimary
import com.example.ui.theme.PinkPrimaryDark
import com.example.ui.theme.RedSos
import com.example.ui.theme.RedSosBg

@Composable
fun HomeScreen(
    viewModel: SafetyViewModel,
    onNavigateToVehicle: () -> Unit,
    onNavigateToPolice: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onOpenOfflineGuide: () -> Unit
) {
    val locationState by viewModel.locationState.collectAsState()
    val isPowerKeyShortcutEnabled by viewModel.isPowerKeyShortcutEnabled.collectAsState()
    val powerKeyPressCount by viewModel.powerKeyPressCount.collectAsState()
    val isSirenOn by viewModel.isSirenOn.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val nearbyPoliceStations by viewModel.nearbyPoliceStations.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "SosPulseHome")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScaleHome"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Network / Offline Mode Status Header Bar
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (locationState.isOfflineMode) Color(0xFFFFF3E0) else PinkContainer
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (locationState.isOfflineMode) Icons.Default.SignalCellularOff else Icons.Default.SignalCellularAlt,
                        contentDescription = null,
                        tint = if (locationState.isOfflineMode) Color(0xFFE65100) else PinkPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (locationState.isOfflineMode) "OFFLINE MODE ACTIVE" else locationState.networkQuality,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (locationState.isOfflineMode) Color(0xFFE65100) else PinkPrimaryDark
                        )
                        Text(
                            text = if (locationState.isOfflineMode) "Emergency SMS Fallback Ready" else "Real-time Live Location Active",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Quick Offline Switch
                Switch(
                    checked = locationState.isOfflineMode,
                    onCheckedChange = { isOffline ->
                        viewModel.setNetworkMode(
                            isOffline = isOffline,
                            quality = if (isOffline) "Offline Mode" else "Good Network (4G/5G)"
                        )
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFE65100),
                        uncheckedThumbColor = PinkPrimary,
                        uncheckedTrackColor = Color.White
                    ),
                    modifier = Modifier.testTag("offline_mode_switch")
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // PROMINENT EMERGENCY SOS BUTTON CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = RedSos
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "EMERGENCY ALERT",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = RedSos
                        )
                    }

                    // Siren Alarm Quick Toggle Button
                    IconButton(
                        onClick = { viewModel.toggleSiren() },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSirenOn) RedSos else PinkContainer)
                            .testTag("siren_toggle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Siren Toggle",
                            tint = if (isSirenOn) Color.White else PinkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // GIANT PROMINENT SOS BUTTON
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(RedSos, Color(0xFFB71C1C))
                            )
                        )
                        .border(6.dp, RedSosBg, CircleShape)
                        .clickable { viewModel.triggerSos("SOS BUTTON") }
                        .testTag("prominent_sos_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "SOS Icon",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "S O S",
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "TAP FOR HELP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFCDD2)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Pressing SOS immediately sends Live Location & SMS alerts to ${contacts.size} emergency contact(s) & nearby police.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sos_misuse_warning_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Misuse Warning",
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Warning: People who misuse the SOS button have to pay a ₹500 penalty.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // POWER BUTTON 5 TIMES SHORTCUT CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PinkContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PowerSettingsNew,
                                contentDescription = null,
                                tint = PinkPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "5-Press Power Button Shortcut",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Rapidly press power button 5 times",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isPowerKeyShortcutEnabled,
                        onCheckedChange = { viewModel.togglePowerKeyShortcut(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PinkPrimary
                        ),
                        modifier = Modifier.testTag("power_key_shortcut_switch")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Interactive Simulator Trigger Button
                Button(
                    onClick = { viewModel.registerPowerKeyPress() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (powerKeyPressCount > 0) RedSos else PinkContainer
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("test_power_press_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = if (powerKeyPressCount > 0) Color.White else PinkPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (powerKeyPressCount > 0)
                            "⚡ Press Recorded! ($powerKeyPressCount / 5) - Tap rapidly!"
                        else
                            "Test Power Key 5-Press Trigger",
                        fontWeight = FontWeight.Bold,
                        color = if (powerKeyPressCount > 0) Color.White else PinkPrimaryDark,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // REAL-TIME LOCATION TRACKING CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PinkPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Real-Time Location Tracking",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    // Refresh/Change Mock Location
                    IconButton(
                        onClick = {
                            viewModel.updateMockLocation(
                                address = "Rajiv Chowk Metro Hub, Central Delhi",
                                lat = 28.6328,
                                lng = 77.2197
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Location",
                            tint = PinkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = locationState.address,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Lat: ${locationState.latitude} | Long: ${locationState.longitude}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onNavigateToPolice,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("nearby_police_button")
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Nearby Police", fontSize = 12.sp, color = PinkPrimary)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = onNavigateToVehicle,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("vehicle_transit_button")
                    ) {
                        Icon(imageVector = Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Travel Alert", fontSize = 12.sp, color = PinkPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // QUICK NEARBY POLICE SNAPSHOT CARD
        if (nearbyPoliceStations.isNotEmpty()) {
            val nearest = nearbyPoliceStations.first()
            Card(
                colors = CardDefaults.cardColors(containerColor = PinkContainer),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NEAREST POLICE STATION (${nearest.distanceKm} km)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PinkPrimaryDark
                        )
                        Text(
                            text = nearest.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = nearest.address,
                            fontSize = 11.sp,
                            color = Color(0xFF444444)
                        )
                    }

                    Button(
                        onClick = { onNavigateToPolice() },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
