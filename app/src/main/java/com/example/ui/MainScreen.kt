package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SignalCellularOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ActiveSosDialog
import com.example.ui.theme.PinkContainer
import com.example.ui.theme.PinkPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: SafetyViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val isSosActive by viewModel.isSosActive.collectAsState()
    val sosTriggerSource by viewModel.sosTriggerSource.collectAsState()
    val locationState by viewModel.locationState.collectAsState()
    val isSirenOn by viewModel.isSirenOn.collectAsState()

    val tabs = listOf("Home", "Vehicle", "Police", "Contacts", "Offline")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_happy_woman),
                            contentDescription = "Women Safety Logo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Women Safety",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = PinkPrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.logout() },
                        modifier = Modifier.testTag("logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                tabs.forEachIndexed { index, label ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.Home
                                    1 -> Icons.Default.DirectionsCar
                                    2 -> Icons.Default.Shield
                                    3 -> Icons.Default.People
                                    else -> Icons.Default.SignalCellularOff
                                },
                                contentDescription = label
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PinkPrimary,
                            selectedTextColor = PinkPrimary,
                            indicatorColor = PinkContainer
                        ),
                        modifier = Modifier.testTag("nav_tab_$label")
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToVehicle = { selectedTab = 1 },
                    onNavigateToPolice = { selectedTab = 2 },
                    onNavigateToContacts = { selectedTab = 3 },
                    onOpenOfflineGuide = { selectedTab = 4 }
                )
                1 -> VehicleTrackerScreen(viewModel = viewModel)
                2 -> PoliceAndMapScreen(viewModel = viewModel)
                3 -> ContactsScreen(viewModel = viewModel)
                4 -> OfflineSafetyGuide()
            }

            // FULLSCREEN ACTIVE SOS ALERT OVERLAY DIALOG
            if (isSosActive) {
                ActiveSosDialog(
                    triggerSource = sosTriggerSource,
                    currentAddress = locationState.address,
                    isSirenOn = isSirenOn,
                    onToggleSiren = { viewModel.toggleSiren() },
                    onCancelSos = { viewModel.cancelSos() }
                )
            }
        }
    }
}
