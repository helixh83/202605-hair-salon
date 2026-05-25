package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay

@Composable
fun TransformationShowcase(modifier: Modifier = Modifier) {
    // List of premium transformation pairs (Before, After, Styling description)
    val transformList = remember {
        listOf(
            TransformationData(
                title = "시스루 댄디컷 & 다운펌",
                description = "덥수룩하고 뜨는 옆머리를 블러 다운펌으로 누르고 이마가 들여다보이는 가벼운 댄디 컷팅",
                beforeUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=500", // shaggy look
                afterUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500" // tidy dandy style
            ),
            TransformationData(
                title = "내츄럴 가일 컷팅",
                description = "M자 이마 보완과 동시에 한쪽을 섹시하게 드러내는 도시적 매칭",
                beforeUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=500",
                afterUrl = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=500"
            ),
            TransformationData(
                title = "인텐스 쉐도우 펌",
                description = "숨죽은 모발에 풍성한 볼륨 S컬 웨이브를 넣어 입체적 소년미 연출",
                beforeUrl = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=500",
                afterUrl = "https://images.unsplash.com/photo-1489980508314-941910ded1f4?w=500"
            )
        )
    }

    var currentIndex by remember { mutableStateOf(0) }
    var isShowingBefore by remember { mutableStateOf(true) }

    // Run looping animation
    // Every 3.5 seconds toggles between Before <-> After, and every 7 seconds shifts to next styling style.
    LaunchedEffect(Unit) {
        while (true) {
            delay(2800)
            isShowingBefore = false
            delay(3200)
            isShowingBefore = true
            delay(500)
            currentIndex = (currentIndex + 1) % transformList.size
        }
    }

    val currentData = transformList[currentIndex]

    // Animation values for smooth fading cross-effect
    val alphaAnim by animateFloatAsState(
        targetValue = if (isShowingBefore) 0.15f else 1.0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "AlphaAfter"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Before Image (Base background)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentData.beforeUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Before style",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // After Image layered on top with animated alpha
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentData.afterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "After style with AI mapping",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = alphaAnim
            )

            // Elegant overlay gradient for text legibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.95f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Top Status Badges indicating transformation state
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge info
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isShowingBefore) MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(
                        text = if (isShowingBefore) "BEFORE (시술 전)" else "AFTER (시술 후 완벽 변신 ✨)",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "HairFit Live 🔴",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp)
                    )
                }
            }

            // Bottom informational panel describing the hairstyle transformation
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = currentData.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentData.description,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Smooth loading/progress dot indicator representing slide index
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    transformList.forEachIndexed { idx, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (idx == currentIndex) 16.dp else 6.dp, 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (idx == currentIndex) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.4f)
                                )
                        )
                    }
                }
            }
        }
    }
}

data class TransformationData(
    val title: String,
    val description: String,
    val beforeUrl: String,
    val afterUrl: String
)
