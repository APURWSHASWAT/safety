package com.example.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.PinkPrimary
import com.example.ui.theme.PinkPrimaryDark
import com.example.util.PoliceStation

@Composable
fun PoliceAndMapScreen(
    viewModel: SafetyViewModel
) {
    val context = LocalContext.current
    val nearbyPoliceStations by viewModel.nearbyPoliceStations.collectAsState()
    val locationState by viewModel.locationState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Nearby Police Stations & Helplines",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = PinkPrimary
        )

        Text(
            text = "Real-time updates based on your current location: ${locationState.address}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // HOTLINES GRID
        Text(
            text = "Emergency Helpline Quick Dial",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EmergencyHotlineCard(
                title = "Women Helpline",
                number = "1091",
                icon = Icons.Default.PhoneInTalk,
                bgColor = PinkContainer,
                textColor = PinkPrimaryDark,
                modifier = Modifier.weight(1f)
            ) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1091"))
                context.startActivity(intent)
            }

            EmergencyHotlineCard(
                title = "National SOS",
                number = "112",
                icon = Icons.Default.Security,
                bgColor = Color(0xFFFFEBEE),
                textColor = Color(0xFFD50000),
                modifier = Modifier.weight(1f)
            ) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                context.startActivity(intent)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EmergencyHotlineCard(
                title = "Police Control",
                number = "100",
                icon = Icons.Default.Shield,
                bgColor = Color(0xFFE3F2FD),
                textColor = Color(0xFF1565C0),
                modifier = Modifier.weight(1f)
            ) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:100"))
                context.startActivity(intent)
            }

            EmergencyHotlineCard(
                title = "Ambulance",
                number = "102",
                icon = Icons.Default.LocalHospital,
                bgColor = Color(0xFFE8F5E9),
                textColor = Color(0xFF2E7D32),
                modifier = Modifier.weight(1f)
            ) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:102"))
                context.startActivity(intent)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Nearest Police Stations (${nearbyPoliceStations.size} Found)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(nearbyPoliceStations) { station ->
                PoliceStationCard(station = station) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${station.phoneNumber}"))
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun EmergencyHotlineCard(
    title: String,
    number: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(14.dp),
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = textColor, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, fontSize = 11.sp, color = textColor, fontWeight = FontWeight.Bold)
                Text(text = number, fontSize = 16.sp, fontWeight = FontWeight.Black, color = textColor)
            }
        }
    }
}

@Composable
fun PoliceStationCard(
    station: PoliceStation,
    onCallClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(PinkContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = PinkPrimary)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = station.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = "${station.distanceKm} km away • ${station.address}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "24x7 Emergency Desk: ${station.phoneNumber}",
                    fontSize = 11.sp,
                    color = PinkPrimaryDark,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onCallClick,
                colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("call_police_station_${station.id}")
            ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Call", fontSize = 12.sp)
            }
        }
    }
}
