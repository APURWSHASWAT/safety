package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Subway
import androidx.compose.material.icons.filled.TaxiAlert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.PinkPrimary
import com.example.ui.theme.PinkPrimaryDark

@Composable
fun VehicleTrackerScreen(
    viewModel: SafetyViewModel
) {
    val trips by viewModel.trips.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val tripShareStatus by viewModel.tripShareStatus.collectAsState()

    var vehicleNumber by remember { mutableStateOf("") }
    var selectedVehicleType by remember { mutableStateOf("Cab / Taxi") }
    var destination by remember { mutableStateOf("") }
    var driverDetails by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }

    val vehicleTypes = listOf("Cab / Taxi", "Auto Rickshaw", "Bus", "Metro / Train", "Private Car")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Vehicle Travel Safety Tracker",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = PinkPrimary
        )

        Text(
            text = "Enter vehicle details to forward instantly to your saved emergency contacts.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Status Toast Banner
        AnimatedVisibility(visible = tripShareStatus != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tripShareStatus ?: "",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // INPUT FORM CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Vehicle Number Input
                OutlinedTextField(
                    value = vehicleNumber,
                    onValueChange = { vehicleNumber = it.uppercase() },
                    label = { Text("Vehicle Reg Number (e.g. DL 01 AB 1234)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = PinkPrimary
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PinkPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("vehicle_number_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Vehicle Type Chips
                Text(
                    text = "Select Vehicle Type:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    vehicleTypes.take(3).forEach { type ->
                        val isSelected = selectedVehicleType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PinkPrimary else PinkContainer)
                                .clickable { selectedVehicleType = type }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else PinkPrimaryDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    vehicleTypes.drop(3).forEach { type ->
                        val isSelected = selectedVehicleType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PinkPrimary else PinkContainer)
                                .clickable { selectedVehicleType = type }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else PinkPrimaryDark
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Destination
                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = { Text("Destination (Optional)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PinkPrimary
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PinkPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("vehicle_destination_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Driver Details
                OutlinedTextField(
                    value = driverDetails,
                    onValueChange = { driverDetails = it },
                    label = { Text("Driver Name / Cab Service (Optional)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = PinkPrimary
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PinkPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (inputError != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = inputError!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (vehicleNumber.isBlank()) {
                            inputError = "Please enter the vehicle registration number"
                        } else {
                            inputError = null
                            viewModel.shareVehicleTrip(
                                vehicleNumber = vehicleNumber,
                                vehicleType = selectedVehicleType,
                                destination = destination,
                                driverDetails = driverDetails
                            )
                            vehicleNumber = ""
                            destination = ""
                            driverDetails = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("forward_vehicle_details_button")
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FORWARD TO ${contacts.size} CONTACTS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Shared Vehicle Trip History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (trips.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No vehicle trips logged yet. Enter vehicle registration number above when boarding a cab or auto.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(trips) { trip ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(PinkContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (trip.vehicleType) {
                                        "Bus" -> Icons.Default.DirectionsBus
                                        "Metro / Train" -> Icons.Default.Subway
                                        "Cab / Taxi" -> Icons.Default.TaxiAlert
                                        else -> Icons.Default.DirectionsCar
                                    },
                                    contentDescription = null,
                                    tint = PinkPrimary
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${trip.vehicleType} • ${trip.vehicleNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                if (trip.destination.isNotBlank()) {
                                    Text(
                                        text = "To: ${trip.destination}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "Forwarded to ${trip.sharedWithContactsCount} emergency contacts",
                                    fontSize = 11.sp,
                                    color = PinkPrimaryDark,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
