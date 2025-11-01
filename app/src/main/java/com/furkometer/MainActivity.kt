package com.furkometer

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.furkometer.ui.theme.FurkoMeterTheme
import com.furkometer.data.StepRecord
import com.furkometer.R
import java.text.SimpleDateFormat
import java.util.Locale


class MainActivity : ComponentActivity() {
    private val viewModel: PedometerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FurkoMeterTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color(0xFF2C3E50),
                        darkIcons = false
                    )
                }
                PedometerScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PedometerScreen(viewModel: PedometerViewModel) {
    val steps by viewModel.steps.collectAsState()
    val distance by viewModel.distance.collectAsState()
    val calories by viewModel.calories.collectAsState()
    
    var showHistory by remember { mutableStateOf(false) }

    val permissionsList = mutableListOf<String>()
    
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        permissionsList.add(Manifest.permission.ACTIVITY_RECOGNITION)
    }
    
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        permissionsList.add(Manifest.permission.POST_NOTIFICATIONS)
    }
    
    val permissionsState = rememberMultiplePermissionsState(
        permissions = permissionsList
    )

    LaunchedEffect(Unit) {
        if (permissionsList.isNotEmpty() && !permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.a),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.4f),
            contentScale = ContentScale.Crop
        )
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.b),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1A2E).copy(alpha = 0.7f),
                            Color(0xFF16213E).copy(alpha = 0.6f),
                            Color(0xFF0F3460).copy(alpha = 0.5f)
                        )
                    )
                )
        )
        
        val shouldShowContent = permissionsList.isEmpty() || permissionsState.allPermissionsGranted
        
        if (shouldShowContent) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = "GoghMeter",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.c),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.6f),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF3498DB).copy(alpha = 0.3f),
                                        Color(0xFF2980B9).copy(alpha = 0.2f),
                                        Color(0xFF1F5F8B).copy(alpha = 0.2f)
                                    )
                                )
                            )
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$steps",
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF000000),
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = "adım",
                            fontSize = 24.sp,
                            color = Color(0xFF000000).copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        value = String.format("%.2f", distance),
                        unit = "km",
                        label = "Mesafe",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard(
                        value = "${calories}",
                        unit = "kcal",
                        label = "Kalori",
                        modifier = Modifier.weight(1f)
                    )
                }

                Button(
                    onClick = { showHistory = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3498DB),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Geçmiş",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            val context = LocalContext.current
            LaunchedEffect(shouldShowContent) {
                if (shouldShowContent) {
                    viewModel.startStepCounter()
                    MidnightReceiver.scheduleMidnightAlarm(context)
                }
            }
            
            DisposableEffect(Unit) {
                viewModel.loadData()
                onDispose { }
            }
        } else {
            PermissionRequestScreen(
                onRequestPermission = { 
                    if (permissionsList.isNotEmpty()) {
                        permissionsState.launchMultiplePermissionRequest()
                    }
                }
            )
        }
        
        if (showHistory) {
            HistoryScreen(
                onDismiss = { showHistory = false },
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun StatCard(
    value: String,
    unit: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3498DB).copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = unit,
                    fontSize = 16.sp,
                    color = Color(0xFFFFD700).copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun PermissionRequestScreen(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.a),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.4f),
            contentScale = ContentScale.Crop
        )
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.b),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A1A2E).copy(alpha = 0.7f),
                            Color(0xFF16213E).copy(alpha = 0.6f),
                            Color(0xFF0F3460).copy(alpha = 0.5f)
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Adım sayma için izin gerekli",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = onRequestPermission,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3498DB),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "İzin Ver",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onDismiss: () -> Unit,
    viewModel: PedometerViewModel
) {
    val history by viewModel.stepHistory.collectAsState()
    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy, EEEE", Locale("tr", "TR")) }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2C3E50),
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .padding(vertical = 12.dp)
                    .background(Color(0xFFFFD700).copy(alpha = 0.5f), RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Adım Geçmişi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Kapat",
                        tint = Color(0xFFFFD700)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Henüz kayıt yok",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(500.dp)
                ) {
                    items(history) { record ->
                        HistoryItem(record, dateFormat)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    record: StepRecord,
    dateFormat: SimpleDateFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF34495E).copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = dateFormat.format(java.util.Date(record.date)),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${String.format("%.2f", record.distance)} km • ${record.calories} kcal",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${record.steps}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = " adım",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Ana Ekran")
@Composable
fun PedometerScreenPreview() {
    FurkoMeterTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.a),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.4f),
                contentScale = ContentScale.Crop
            )
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.b),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1A1A2E).copy(alpha = 0.7f),
                                Color(0xFF16213E).copy(alpha = 0.6f),
                                Color(0xFF0F3460).copy(alpha = 0.5f)
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "GoghMeter",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.c),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.6f),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF3498DB).copy(alpha = 0.3f),
                                        Color(0xFF2980B9).copy(alpha = 0.2f),
                                        Color(0xFF1F5F8B).copy(alpha = 0.2f)
                                    )
                                )
                            )
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "8520",
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF000000),
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = "adım",
                            fontSize = 24.sp,
                            color = Color(0xFF020202).copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        value = "6.39",
                        unit = "km",
                        label = "Mesafe",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    StatCard(
                        value = "383",
                        unit = "kcal",
                        label = "Kalori",
                        modifier = Modifier.weight(1f)
                    )
                }

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3498DB),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Geçmiş",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "İzin İsteği Ekranı")
@Composable
fun PermissionRequestScreenPreview() {
    FurkoMeterTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2C3E50),
                            Color(0xFF34495E),
                            Color(0xFF5D4E75)
                        )
                    )
                )
        ) {
            PermissionRequestScreen(onRequestPermission = { })
        }
    }
}

@Preview(showBackground = true, name = "Stat Kartı")
@Composable
fun StatCardPreview() {
    FurkoMeterTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF2C3E50))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard(
                    value = "6.39",
                    unit = "km",
                    label = "Mesafe",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                StatCard(
                    value = "383",
                    unit = "kcal",
                    label = "Kalori",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

