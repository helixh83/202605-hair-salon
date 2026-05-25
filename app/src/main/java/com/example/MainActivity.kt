package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.HairViewModel

import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip

class MainActivity : ComponentActivity() {
    private val viewModel: HairViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(viewModel: HairViewModel) {
    // Tab indicator state: 
    // 0: 미용실 찾기 (Find Salon)
    // 1: AI 얼굴형 분석 (Upload & Style)
    // 2: 이벤트 (Events/Coupons)
    // 3: 제품구매 (Haircare Shop)
    // 4: 마이샵 (My Shop history & manager approval simulator)
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_scaffold"),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Sleek Circular Brand Logo Circle
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCut,
                                contentDescription = "Cut Logo",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        
                        Row {
                            Text(
                                text = "HairFit ",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "AI",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* Simulated Notifications */ },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.shadow(1.dp)
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                // 1. 미용실 찾기
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                            contentDescription = "미용실 찾기"
                        )
                    },
                    label = { Text("미용실 찾기", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_tab_salons")
                )

                // 2. 내 얼굴형 사진 업로드
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Filled.Face else Icons.Outlined.Face,
                            contentDescription = "AI 분석"
                        )
                    },
                    label = { Text("AI 분석", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_tab_ai_analysis")
                )

                // 3. 이벤트
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.ConfirmationNumber else Icons.Outlined.ConfirmationNumber,
                            contentDescription = "이벤트"
                        )
                    },
                    label = { Text("이벤트", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_tab_events")
                )

                // 4. 제품구매
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 3) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                            contentDescription = "제품구매"
                        )
                    },
                    label = { Text("제품구매", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_tab_products")
                )

                // 5. 마이샵
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 4) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                            contentDescription = "마이샵"
                        )
                    },
                    label = { Text("마이샵", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_tab_myshop")
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> FindSalonScreen(
                    viewModel = viewModel,
                    onRequestStyling = { _ ->
                        // Automatically transit to AI suggestion tab so they matching hairstyle instantly
                        selectedTab = 1
                    }
                )
                1 -> FaceUploadScreen(
                    viewModel = viewModel,
                    onNavigateToMyShop = { selectedTab = 4 }
                )
                2 -> EventScreen(viewModel = viewModel)
                3 -> ProductScreen(viewModel = viewModel)
                4 -> MyShopScreen(viewModel = viewModel)
            }
        }
    }
}
