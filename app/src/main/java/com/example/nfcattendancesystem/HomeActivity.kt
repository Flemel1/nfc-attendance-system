package com.example.nfcattendancesystem

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.nfcattendancesystem.extention.hasLocationPermission
import com.example.nfcattendancesystem.screen.route.AppNavHost
import com.example.nfcattendancesystem.ui.theme.NFCAttendanceSystemTheme
import com.example.nfcattendancesystem.viewmodel.BackendViewModel
import com.example.nfcattendancesystem.viewmodel.LocationViewModel
import com.example.nfcattendancesystem.viewmodel.PermissionEvent
import com.example.nfcattendancesystem.viewmodel.ViewState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val locationViewModel: LocationViewModel by viewModels()
        val backendViewModel: BackendViewModel by viewModels<BackendViewModel>()
        setContent {
            
            val permissionState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            val viewState by locationViewModel.viewState.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            NFCAttendanceSystemTheme {
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Selamat Datang") },
                            actions = {
                                IconButton(onClick = { null }) {
                                    Icon(
                                        imageVector = Icons.Filled.AccountCircle,
                                        contentDescription = "Icon User",
                                        tint = Color.Green)
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    
                    LaunchedEffect(!hasLocationPermission()) {
                        permissionState.launchMultiplePermissionRequest()
                    }

                    when {
                        permissionState.allPermissionsGranted -> {
                            LaunchedEffect(Unit) {
                                locationViewModel.handle(PermissionEvent.Granted)
                            }
                        }

                        permissionState.shouldShowRationale -> {
                            RationaleAlert(onDismiss = { }) {
                                permissionState.launchMultiplePermissionRequest()
                            }
                        }

                        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
                            LaunchedEffect(Unit) {
                                locationViewModel.handle(PermissionEvent.Revoked)
                            }
                        }
                    }

                    with(viewState) {
                        when (this) {
                            ViewState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            ViewState.RevokedPermissions -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Menunggu Izin Lokasi App")
                                    Button(
                                        onClick = {
                                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                                        },
                                        enabled = !hasLocationPermission()
                                    ) {
                                        if (hasLocationPermission()) CircularProgressIndicator(
                                            modifier = Modifier.size(14.dp),
                                            color = Color.White
                                        )
                                        else Text("Settings")
                                    }
                                }
                            }

                            is ViewState.Success -> {
                                val sharedPref = this@HomeActivity.getSharedPreferences("location_data",Context.MODE_PRIVATE)
                                val state = this
                                with (sharedPref.edit()) {
                                    putString("latitude", state.location?.latitude.toString())
                                    putString("longitude", state.location?.longitude.toString())
                                    apply()
                                }

                                AppNavHost(navController = navController, viewModel = backendViewModel, modifier = Modifier.padding(innerPadding))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeView(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(5) {
            HomeCard()
        }
    }
}

@Composable
fun HomeCard() {
    Column(modifier = Modifier
        .padding(all = 10.dp)
        .fillMaxWidth()
        .background(color = Color.Green)
        .padding(all = 20.dp)
    ) {
        Text(
            "Matematika Teknik",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White)
        Text(
            "Ruang: S211",
            color = Color.White)
        Text(
            "Hari: Rabu",
            color = Color.White)
        Text(
            "Jam: 07.00 - 09.00",
            color = Color.White)
    }
}

@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Aplikasi ini memerlukan lokasi anda untuk dapat dijalankan",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeViewPreview() {
    NFCAttendanceSystemTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Halo, Daniel") },
                    actions = {
                        IconButton(onClick = { null }) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Icon User",
                                tint = Color.Green)
                        }
                    }
                )
            }
        ) { innerPadding ->
            HomeView(Modifier.padding(innerPadding))
        }
    }
}