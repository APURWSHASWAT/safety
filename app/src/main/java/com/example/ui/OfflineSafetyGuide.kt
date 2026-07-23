package com.example.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SignalCellularOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.PinkPrimary
import com.example.ui.theme.PinkPrimaryDark

@Composable
fun OfflineSafetyGuide() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF3E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SignalCellularOff,
                    contentDescription = null,
                    tint = Color(0xFFE65100)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Offline Mode & Low Network Safety",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
                Text(
                    text = "Guaranteed alert dispatch even without active internet data.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // HOW OFFLINE MODE WORKS CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Message, contentDescription = null, tint = PinkPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cellular SMS Backup Protocol",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "When internet data (4G/5G/Wi-Fi) is unavailable, the app automatically switches to direct SMS transmission over standard cellular networks.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = PinkContainer),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "OFFLINE SMS FORMAT PREVIEW:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = PinkPrimaryDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "\"[OFFLINE SMS ALERT] Urgent help needed! My location: Connaught Circus (https://maps.google.com/?q=28.6315,77.2167).\"",
                            fontSize = 12.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // OFFLINE SAFETY TIPS CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = PinkPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Essential Emergency Self-Defense Tips",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SafetyTipItem(
                    number = "1",
                    title = "Use Power Button 5-Press Shortcut",
                    desc = "Rapidly press the power button 5 times without unlocking your phone to send a silent emergency alert."
                )

                SafetyTipItem(
                    number = "2",
                    title = "Dial 112 or 1091 Direct Cellular Call",
                    desc = "National emergency numbers 112 and 1091 route to nearest control rooms directly via basic phone tower signals."
                )

                SafetyTipItem(
                    number = "3",
                    title = "Vehicle Details Record",
                    desc = "Always enter cab/auto registration numbers in the Travel Safety Tracker before boarding."
                )
            }
        }
    }
}

@Composable
fun SafetyTipItem(number: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(PinkPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
