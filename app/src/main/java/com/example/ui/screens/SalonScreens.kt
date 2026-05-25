package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.database.VisitHistory
import com.example.ui.components.TransformationShowcase
import com.example.ui.components.SleekPageContainer
import com.example.ui.components.SleekHeader
import com.example.ui.components.SleekCard
import com.example.ui.viewmodel.HairViewModel
import com.example.ui.viewmodel.PresetFace
import com.example.ui.viewmodel.Product
import com.example.ui.viewmodel.Salon
import com.example.ui.viewmodel.HairEvent

// ==========================================
// 1. FIND SALON SCREEN (미용실 찾기)
// ==========================================
@Composable
fun FindSalonScreen(
    viewModel: HairViewModel,
    onRequestStyling: (Salon) -> Unit,
    modifier: Modifier = Modifier
) {
    val salons by viewModel.salons.collectAsState()
    val context = LocalContext.current

    SleekPageContainer {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Core transformation showcase requested by client!
            item {
                SleekHeader(
                    badgeText = "HairFit Premium",
                    titleText = "실시간 트렌드 헤어 매직",
                    subtitleText = "시술 전후의 놀라운 변신 효과를 미용실 가기 전에 확인해 보세요."
                )
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TransformationShowcase()
                }
            }

            // Secondary Info cards from the Sleek mockup layout
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SleekCard(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Hot Event",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "HOT EVENT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "첫 방문 30% 할인",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    SleekCard(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = "Best Sellers",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "BEST SELLERS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "헤어 왁스 & 오일",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "내 주변 추천 미용실 📍",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "현재 고객 제안 승인 & 시술 예약이 가능한 파트너 숍",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            items(salons) { salon ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SleekCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("salon_card_${salon.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = salon.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = salon.address,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                
                                // Rating box
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = salon.rating.toString(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Distance",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = " ${salon.distance}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.ChatBubble,
                                            contentDescription = "ReviewCount",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = " 리뷰 ${salon.reviewCount}",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                        )
                                    }
                                }

                                // Call dialer button
                                IconButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${salon.phone}"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                                            CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "Call",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { onRequestStyling(salon) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("salon_book_button_${salon.id}"),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.ContentCut, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Text(
                                        "이 미용실에 AI 헤어 매치 예약하기", 
                                        fontSize = 13.sp, 
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. FACE UPLOAD SCREEN (내 얼굴형 사진 업로드 & AI 헤어스타일 제안)
// ==========================================
data class AiStyleOption(
    val name: String,
    val imageUrl: String,
    val description: String,
    val price: Int
)

@Composable
fun FaceUploadScreen(
    viewModel: HairViewModel,
    onNavigateToMyShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedFace by viewModel.selectedFace.collectAsState()
    val presetFaces by viewModel.presetFaces.collectAsState()
    val aiResult by viewModel.aiAnalysisResult.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val customPhoto by viewModel.customPhotoBase64.collectAsState()
    val salons by viewModel.salons.collectAsState()

    var showRequestDialog by remember { mutableStateOf(false) }
    var selectedTargetSalon by remember { mutableStateOf<Salon?>(null) }
    var requestedHairstyle by remember { mutableStateOf("트렌디 가일컷 (Gail Cut)") }
    var userMemo by remember { mutableStateOf("AI가 내 얼굴형을 디코딩해 추천해준 맞춤 헤어 솔루션과 가이드를 확인했습니다. 볼륨감과 사이드 다운펌 시술을 맞춤형으로 부탁드립니다.") }

    var selectedStyleOptionIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current

    if (salons.isNotEmpty() && selectedTargetSalon == null) {
        selectedTargetSalon = salons.first()
    }

    val aiStyleOptions = remember(selectedFace, customPhoto) {
        listOf(
            AiStyleOption(
                name = "트렌디 가일컷 (Gail Cut)",
                imageUrl = if (customPhoto != null) "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=300" else selectedFace?.imageUrlAfter ?: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=300",
                description = "이마가 시원하게 드러나고 옆머리가 슬림하게 붙어 지적이고 샤프한 세련미",
                price = 68000
            ),
            AiStyleOption(
                name = "내츄럴 애즈펌 (As Perm)",
                imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300",
                description = "시스루 분위기에 부드러운 유선형 구도 볼륨과 6:4 가르마 훈남 정석 머리",
                price = 120000
            ),
            AiStyleOption(
                name = "시스루 댄디컷 (Dandy Cut)",
                imageUrl = "https://images.unsplash.com/photo-1621252179027-94459d278660?w=300",
                description = "이마 라인을 가볍게 비추어 무겁지 않고 부드러운 호감도를 주는 댄디컷",
                price = 55000
            ),
            AiStyleOption(
                name = "클래식 아이비리그 (Ivy Cut)",
                imageUrl = "https://images.unsplash.com/photo-1489980508314-941910ded1f4?w=300",
                description = "스포티하며 이목구비가 뚜렷해 보이는 짧은 기장 베이스의 단정 스마트 컷",
                price = 45000
            )
        )
    }

    // Sync input when index changes
    LaunchedEffect(selectedStyleOptionIndex) {
        if (selectedStyleOptionIndex in aiStyleOptions.indices) {
            requestedHairstyle = aiStyleOptions[selectedStyleOptionIndex].name
        }
    }

    // Dial log container
    if (showRequestDialog && selectedTargetSalon != null) {
        AlertDialog(
            onDismissRequest = { showRequestDialog = false },
            title = {
                Text(
                    text = "원하는 헤어스타일 제안 예약",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "고객님의 AI 맞춤 스타일링 제안서를 미용실 전산으로 전송하여 원장님의 예결 승인을 요청합니다.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text("1. 시술 예약 미용실", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    // Simple select box (combobox simulated via Row click)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        salons.forEach { sl ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedTargetSalon = sl }
                                    .padding(vertical = 6.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (selectedTargetSalon?.id == sl.id),
                                    onClick = { selectedTargetSalon = sl }
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(sl.name, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    Text("2. 원하는 헤어 스타일", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = requestedHairstyle,
                        onValueChange = { requestedHairstyle = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 13.sp)
                    )

                    Text("3. 디자이너 요청 사항", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    OutlinedTextField(
                        value = userMemo,
                        onValueChange = { userMemo = it },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        textStyle = TextStyle(fontSize = 12.sp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val beforeUrl = if (customPhoto != null) "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200" else selectedFace?.imageUrlBefore
                        val requestUrl = aiStyleOptions.getOrNull(selectedStyleOptionIndex)?.imageUrl

                        viewModel.submitStylingRequestToSalon(
                            salon = selectedTargetSalon!!,
                            styleName = requestedHairstyle,
                            price = aiStyleOptions.getOrNull(selectedStyleOptionIndex)?.price ?: 68000,
                            notes = userMemo,
                            beforePhotoUrl = beforeUrl,
                            requestPhotoUrl = requestUrl
                        )
                        showRequestDialog = false
                        Toast.makeText(context, "미용실에 스타일 제안 및 방문요청 수신을 완료했습니다! [마이샵]에서 예약 승인 상태를 확인해 보세요.", Toast.LENGTH_LONG).show()
                        onNavigateToMyShop()
                    }
                ) {
                    Text("시술 제안서 및 방문 승인 요청")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRequestDialog = false }) {
                    Text("취소")
                }
            }
        )
    }

    SleekPageContainer {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SleekHeader(
                badgeText = "AI Face Styling ⚡",
                titleText = "내 얼굴형 맞춤 AI 제안",
                subtitleText = "사진을 업로드하거나 프리셋 모델을 선택하면, AI 기술을 통해 즉석에서 얼굴 윤곽에 어울리는 최적 헤어스타일링을 제안합니다."
            )

            // Section for Photo selection
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                SleekCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "STEP 1. 얼굴 이미지 준비 📸",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        // Mode select chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            presetFaces.forEach { face ->
                                val isSelected = selectedFace?.id == face.id && customPhoto == null
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.selectFacePreset(face) },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) {
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        } else {
                                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.02f)
                                        }
                                    ),
                                    border = BorderStroke(
                                        1.5.dp,
                                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                        ) {
                                            AsyncImage(
                                                model = face.imageUrlBefore,
                                                contentDescription = face.faceShape,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = face.faceShape.split(" ").last(),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom upload button
                        Button(
                            onClick = {
                                // Simulate instant profile photo upload via preset beautiful image
                                viewModel.setCustomPhoto("BASE64_SIMULATED_USER_IMAGE")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (customPhoto != null) {
                                    MaterialTheme.colorScheme.secondary
                                } else {
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                                },
                                contentColor = if (customPhoto != null) Color.White else MaterialTheme.colorScheme.onBackground
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                if (customPhoto != null) "내 사진 업로드 완료! (도윤_얼굴.png)" else "내 스마트폰에 있는 사진 업로드하기",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // [NEW] Various Hairstyle Expression Grid/Carousel
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                SleekCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "STEP 2. AI 가상 스타일링 체험 🔮",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "고객님의 스마트 분석 데이터를 토대로 생성한 4가지 AI 헤어 컬렉션입니다. 마음에 드는 디자인을 선택해보세요.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Horizontal style options carousel
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            aiStyleOptions.forEachIndexed { index, option ->
                                val isSelected = index == selectedStyleOptionIndex
                                Card(
                                    modifier = Modifier
                                        .width(130.dp)
                                        .clickable { selectedStyleOptionIndex = index },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) {
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
                                        } else {
                                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.01f)
                                        }
                                    ),
                                    border = BorderStroke(
                                        2.dp,
                                        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(114.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        ) {
                                            AsyncImage(
                                                model = option.imageUrl,
                                                contentDescription = option.name,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                            if (isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .background(MaterialTheme.colorScheme.secondary)
                                                        .align(Alignment.TopEnd)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text("선택됨", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                        
                                        Spacer(modifier = Modifier.height(6.dp))
                                        
                                        Text(
                                            text = option.name,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        
                                        Text(
                                            text = "${option.price}원",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Comparison Layout Showcase
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                SleekCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "AI 맞춤 시술 전/후 비교 보드 🔎",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Before styling
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Text("① 시술 전 상태", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                                ) {
                                    AsyncImage(
                                        model = if (customPhoto != null) "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200" else selectedFace?.imageUrlBefore,
                                        contentDescription = "Before face",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color.Black.copy(alpha = 0.5f))
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                    ) {
                                        Text("내 얼굴", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                    }
                                }
                            }

                            // Arrow
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "to",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .size(24.dp)
                            )

                            // After style preview matched!
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Text("② AI 시술요청 매칭", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(14.dp))
                                ) {
                                    AsyncImage(
                                        model = aiStyleOptions.getOrNull(selectedStyleOptionIndex)?.imageUrl,
                                        contentDescription = "AI Styled variant",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f))
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                    ) {
                                        Text("AI 매칭됨", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (customPhoto != null) {
                                "고객 업로드 사진 감지: [샤프형 가름 이마라인]\n매칭 스타일: ${aiStyleOptions.getOrNull(selectedStyleOptionIndex)?.name}"
                            } else {
                                "선택 가이드: ${selectedFace?.faceShape} \n매칭 스타일: ${aiStyleOptions.getOrNull(selectedStyleOptionIndex)?.name}"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // The Big AI Trigger Button
                        Button(
                            onClick = { viewModel.requestAiHairstyleAnalysis() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("ai_analysis_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RoundedCornerShape(14.dp),
                            enabled = !isAnalyzing
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("AI 헤어 스타일 처방 인코딩 중...", fontSize = 14.sp)
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("AI 분석 리포트 생성 & 추천 솔루션 받기", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            // Result Container with Smooth Reveal Animation
            AnimatedVisibility(
                visible = aiResult.isNotEmpty(),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SleekCard(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f))
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI MATCH",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI 헤어핏 개인화 레포트 ✨",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = aiResult,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "이 제안서로 미용실 헤어 예약하기 👨‍🎨",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                              ) {
                                Button(
                                    onClick = {
                                        showRequestDialog = true
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .testTag("request_book_with_ai"),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("미용실 승인 및 방문 요청", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. EVENT SCREEN (이벤트)
// ==========================================
@Composable
fun EventScreen(
    viewModel: HairViewModel,
    modifier: Modifier = Modifier
) {
    val events by viewModel.events.collectAsState()
    val context = LocalContext.current

    SleekPageContainer {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SleekHeader(
                    badgeText = "SALON EVENTS 🎁",
                    titleText = "HOT 이벤트 및 쿠폰북",
                    subtitleText = "스타일 변신을 더욱 부담 없이 누릴 수 있는 제휴 및 멤버십 쿠폰 리스트입니다."
                )
            }

            items(events) { event ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SleekCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    // Badge
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    ) {
                                        Text(
                                            text = " ${event.badge} ",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 6.dp)
                                        )
                                    }

                                    Text(
                                        text = event.title,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = event.subTitle,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "기간: ${event.datePeriod}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }

                                // Huge discount percentage in the top corner circle
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(end = 16.dp)
                                        .size(72.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${event.discountPercent}%",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "OFF",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                            
                            // Coupon download footer action
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                                    .clickable {
                                        Toast
                                            .makeText(
                                                context,
                                                "✨ 쿠폰이 마이샵 할인 쿠폰북에 적립 되었습니다!",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                              ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ConfirmationNumber,
                                        contentDescription = "download",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "원터치 발급 및 보관함 넣기",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// ==========================================
// 4. PRODUCT PURCHASE SCREEN (제품구매)
// ==========================================
@Composable
fun ProductScreen(
    viewModel: HairViewModel,
    modifier: Modifier = Modifier
) {
    val products by viewModel.products.collectAsState()
    val context = LocalContext.current

    SleekPageContainer {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SleekHeader(
                badgeText = "PREMIUM BARBER SHOP 🧴",
                titleText = "살롱 헤어케어 제품 몰",
                subtitleText = "실제 명품 뷰티 숍 디자이너들이 시술 시 애용하는 고품질 헤어 스타일 전용 케어 진열대"
            )

            // Grid layout for products with padding
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(products) { product ->
                    SleekCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Product image box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .background(Color.White)
                            ) {
                                AsyncImage(
                                    model = product.imageResUrl,
                                    contentDescription = product.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                
                                // Badge label
                                Surface(
                                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                ) {
                                    Text(
                                        text = product.tag,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = product.brand,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                
                                Text(
                                    text = product.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${product.originalPrice}원",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                            textDecoration = TextDecoration.LineThrough,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                        Text(
                                            text = "${product.price}원",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            Toast.makeText(context, "🛒 ${product.name}을(를) 장바구니에 담았습니다.", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                                CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddShoppingCart,
                                            contentDescription = "Buy",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

                              // ==========================================
// 5. MY SHOP SCREEN (마이샵 - 내 방문 이력 및 디자이너 제안 최종 서명)
// ==========================================
@Composable
fun MyShopScreen(
    viewModel: HairViewModel,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.visitHistories.collectAsState()
    
    // Toggle representing the salon manager simulation! 
    // Very important, as this implements confirming, approving, and scheduling visit requests instantly!
    var adminModeEnabled by remember { mutableStateOf(false) }

    SleekPageContainer {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Elegant top banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.0f)) {
                        Text(
                            text = "MY BEAUTY LOG BOOK",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = if (adminModeEnabled) "살롱 원장/디자이너 대시보드" else "나의 헤어 마이샵",
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // Simulator switch trigger
                    Column(horizontalAlignment = Alignment.End) {
                        Text("원장님 예약승인 모드", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Switch(
                            checked = adminModeEnabled,
                            onCheckedChange = { adminModeEnabled = it },
                            modifier = Modifier.testTag("admin_mode_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = if (adminModeEnabled) {
                            "미용실 관리자 관점입니다. 고객들이 직접 전송한 AI 얼굴 분석 제안을 보고 승인하여 예약을 확정합니다."
                        } else {
                            "나의 살롱 예약 및 시술 내역, 미용실 방문 히스토리 장부입니다. 승인 완료 시 매장 방문 일정을 조율할 수 있습니다."
                        },
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            // List Header label
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (adminModeEnabled) "📝 심사 대기 중인 고객 요청 건" else "📅 나의 시술 이력 및 예약 상태",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = "총 ${logs.size}건",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (logs.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        SleekCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventNote,
                                    contentDescription = "Empty",
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    "등록된 이력이 없습니다.\n지금 내 얼굴형 사진을 업로드하고 미용실에 AI 헤어 제안을 보내보세요!",
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            } else {
                items(logs) { visit ->
                    val isRequested = visit.status == "REQUESTED"
                    val isApproved = visit.status == "APPROVED"
                    val isCompleted = visit.status == "COMPLETED"

                    val statusColor = when {
                        isCompleted -> Color.Gray
                        isApproved -> Color(0xFF4CAF50) // Green
                        else -> Color(0xFFFF9800) // Orange/Yellow
                    }

                    val statusText = when {
                        isCompleted -> "시술 완료 (방문완료)"
                        isApproved -> "예약 승인됨 (방문요청)"
                        else -> "미용실 검토중 (대기)"
                    }

                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        SleekCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("visit_card_${visit.id}"),
                            border = BorderStroke(
                                width = if (isRequested) 1.5.dp else 1.dp,
                                color = if (isRequested) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "방문 예정일: ${visit.visitDate}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Badge
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = statusColor.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = statusText,
                                            color = statusColor,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = visit.salonName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "시술명: ${visit.styleName}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                                    )
                                    Text(
                                        text = "금액: ${visit.price}원",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "담당: ${visit.designerName}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )

                                // [NEW] 3-Column Image Comparison Grid (전 / 요청 / 후)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("📸 AI 헤어핏 히스토리 레코드", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Pre-treatment (Before)
                                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                        ) {
                                            if (visit.beforePhotoUrl != null) {
                                                AsyncImage(
                                                    model = visit.beforePhotoUrl,
                                                    contentDescription = "시술 전",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            } else {
                                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                    Text("미등록", fontSize = 10.sp, color = Color.Gray)
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("시술전 (My Face)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                    }

                                    // Request Style (Requested)
                                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                                .border(1.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                        ) {
                                            if (visit.requestPhotoUrl != null) {
                                                AsyncImage(
                                                    model = visit.requestPhotoUrl,
                                                    contentDescription = "시술 요청",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            } else {
                                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                    Text("미등록", fontSize = 10.sp, color = Color.Gray)
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("시술요청 (AI)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                    }

                                    // Completed Style (Finished)
                                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                                .border(2.dp, if (visit.afterPhotoUrl != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                        ) {
                                            if (visit.afterPhotoUrl != null) {
                                                AsyncImage(
                                                    model = visit.afterPhotoUrl,
                                                    contentDescription = "시술 완료",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            } else {
                                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                                        Icon(Icons.Default.HourglassEmpty, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                                                        Text("시술 대기중", fontSize = 9.sp, color = Color.Gray)
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("시술후 (완료)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (visit.afterPhotoUrl != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                                    }
                                }

                                if (visit.notes.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = "💬 " + visit.notes,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                        )
                                    }
                                }

                                // Simulator actions inside list cards
                                if (adminModeEnabled && isRequested) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.approveStylingRequest(visit) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(38.dp)
                                                .testTag("admin_approve_${visit.id}"),
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("고객 헤어 승인 & 예약 확정", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                                        }

                                        OutlinedButton(
                                            onClick = { viewModel.deleteHistory(visit) },
                                            modifier = Modifier.height(38.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                                            border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.4f))
                                        ) {
                                            Text("거절", fontSize = 11.sp)
                                        }
                                    }
                                } else if (adminModeEnabled && isApproved) {
                                    // Simulated Complete Styling action
                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            // Provide styled completed haircut photo (either original styled or preset beautiful style)
                                            val beautyAfterOption = visit.requestPhotoUrl ?: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=300"
                                            viewModel.completeStylingRequest(visit, beautyAfterOption)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(38.dp)
                                            .testTag("admin_complete_${visit.id}"),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.ContentCut, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("✂️ 최종 시술 완료 (시술후 사진 피팅)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                } else if (!adminModeEnabled && isApproved) {
                                    // User visual confirmation
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            // Simulated call / map navigation for salon visit confirmation
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(38.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("승인된 미용실 길찾기 및 방문 체크", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
