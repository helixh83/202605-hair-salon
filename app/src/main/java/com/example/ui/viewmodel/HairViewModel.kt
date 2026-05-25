package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiClient
import com.example.data.database.AppDatabase
import com.example.data.database.VisitHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// UI state representations
data class Salon(
    val id: Int,
    val name: String,
    val address: String,
    val distance: String,
    val rating: Float,
    val reviewCount: Int,
    val phone: String,
    val imageResUrl: String? = null
)

data class Product(
    val id: Int,
    val name: String,
    val brand: String,
    val price: Int,
    val originalPrice: Int,
    val imageResUrl: String,
    val tag: String,
    val score: Float
)

data class HairEvent(
    val id: Int,
    val title: String,
    val subTitle: String,
    val datePeriod: String,
    val discountPercent: Int,
    val badge: String
)

data class PresetFace(
    val id: Int,
    val faceShape: String, // "Oval", "Round", "Square/Long"
    val gender: String, // "남자", "여자"
    val description: String,
    val imageUrlBefore: String,
    val imageUrlAfter: String
)

class HairViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.visitHistoryDao()

    // 1. Visit histories from local Room DB
    val visitHistories: StateFlow<List<VisitHistory>> = dao.getAllVisits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Salon Data list
    private val _salons = MutableStateFlow<List<Salon>>(emptyList())
    val salons: StateFlow<List<Salon>> = _salons.asStateFlow()

    // 3. Products Data list
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // 4. Events Data list
    private val _events = MutableStateFlow<List<HairEvent>>(emptyList())
    val events: StateFlow<List<HairEvent>> = _events.asStateFlow()

    // 5. Preset faces for face analysis simulation
    private val _presetFaces = MutableStateFlow<List<PresetFace>>(emptyList())
    val presetFaces: StateFlow<List<PresetFace>> = _presetFaces.asStateFlow()

    // 6. Currently selected face for styling check
    private val _selectedFace = MutableStateFlow<PresetFace?>(null)
    val selectedFace: StateFlow<PresetFace?> = _selectedFace.asStateFlow()

    // 7. AI analysis response state
    private val _aiAnalysisResult = MutableStateFlow<String>("")
    val aiAnalysisResult: StateFlow<String> = _aiAnalysisResult.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    // 8. Custom user photo (if they choose upload photo instead of preset)
    private val _customPhotoBase64 = MutableStateFlow<String?>(null)
    val customPhotoBase64: StateFlow<String?> = _customPhotoBase64.asStateFlow()

    init {
        loadStaticMockData()
        seedDefaultHistoryIfEmpty()
    }

    private fun loadStaticMockData() {
        // Mock Salons
        _salons.value = listOf(
            Salon(1, "준오헤어 강남역점", "서울특별시 강남구 강남대로 406", "0.3 km", 4.9f, 439, "02-555-1234"),
            Salon(2, "박승철헤어스튜디오 서초점", "서울특별시 서초구 서초대로 315", "1.1 km", 4.7f, 212, "02-3474-0988"),
            Salon(3, "헤어더뷰 신논현점", "서울특별시 강남구 봉은사로 115", "0.8 km", 4.8f, 184, "02-545-2311"),
            Salon(4, "리안헤어 역삼역점", "서울특별시 강남구 테헤란로 208", "1.5 km", 4.6f, 98, "02-234-9911")
        )

        // Mock Products
        _products.value = listOf(
            Product(1, "어반 매트 스타일 왁스 100g", "헤어핏 프로", 18000, 24000, "https://images.unsplash.com/photo-1608248597279-f99d160bfcbc?w=200", "Best Seller", 4.8f),
            Product(2, "실키 컬 크림 & 에센스 200ml", "글램 스타일", 21000, 26000, "https://images.unsplash.com/photo-1526947425960-945c6e72858f?w=200", "컬링 강화", 4.9f),
            Product(3, "스칼프 너리싱 삼샴푸 500ml", "네이처큐브", 25000, 32000, "https://images.unsplash.com/photo-1535585209827-a15fcdbc4c2d?w=200", "탈모 예방", 4.7f),
            Product(4, "인텐스 리페어 헤어 에센스 오일 80ml", "오가닉 테라피", 23000, 28000, "https://images.unsplash.com/photo-1601049541289-9b1b7bbbfe19?w=200", "손상모 완화", 4.9f)
        )

        // Mock Events
        _events.value = listOf(
            HairEvent(1, "첫 방문 고객 30% 프리미엄 케어 할인", "모든 컷트 및 펌 시술 시 특별 우대 혜택 적용", "2026.05.01 ~ 2026.06.30", 30, "NEW"),
            HairEvent(2, "리프가 가볍게! 여름 더위 저격 컷&다운펌", "더워지는 여름 대비 시원하고 트렌디한 다운펌 패키지", "2026.05.15 ~ 2026.08.31", 20, "SUMMER"),
            HairEvent(3, "웨딩 스타일링 커플 매칭 특별 기획", "예비 신부 신랑을 위한 원데이 토탈 스타일링 케어", "2026.05.01 ~ 2026.12.31", 15, "WEDDING")
        )

        // Preset male models (Before vs After) for styling simulation
        _presetFaces.value = listOf(
            PresetFace(
                id = 1,
                faceShape = "정갈한 계란형 (Oval)",
                gender = "남자",
                description = "댄디하고 부드러운 호감형 마스크",
                imageUrlBefore = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300", // Man with straight hair
                imageUrlAfter = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=300" // Man styled, hair up or tidy
            ),
            PresetFace(
                id = 2,
                faceShape = "부드러운 둥근형 (Round)",
                gender = "남자",
                description = "볼살이 있어 귀여우나 정돈이 필요한 마스크",
                imageUrlBefore = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300",
                imageUrlAfter = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300"
            ),
            PresetFace(
                id = 3,
                faceShape = "샤프하고 각진/긴형 (Square/Long)",
                gender = "남자",
                description = "선이 굵어 남성적이나 긴 보정이 필요한 마스크",
                imageUrlBefore = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=300",
                imageUrlAfter = "https://images.unsplash.com/photo-1489980508314-941910ded1f4?w=300"
            )
        )

        // Select the first as default
        _selectedFace.value = _presetFaces.value.first()
    }

    private fun seedDefaultHistoryIfEmpty() {
        viewModelScope.launch {
            // Check count
            val list = dao.getVisitById(1)
            // Seeding default histories representing different stages of customer salon matching flow
            if (list == null) {
                dao.insertVisit(
                    VisitHistory(
                        id = 1,
                        salonName = "준오헤어 강남역점",
                        visitDate = "2026-05-10",
                        designerName = "현우 수석 디자이너",
                        styleName = "시스루 댄디컷 + 블러 다운펌",
                        price = 66000,
                        status = "COMPLETED",
                        notes = "얼굴 비율이 훨씬 훤칠해 보여 무척 마음에 듭니다.",
                        beforePhotoUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=300",
                        requestPhotoUrl = "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=300",
                        afterPhotoUrl = "https://images.unsplash.com/photo-1621252179027-94459d278660?w=300"
                    )
                )
                dao.insertVisit(
                    VisitHistory(
                        id = 2,
                        salonName = "준오헤어 강남역점",
                        visitDate = "2026-05-24",
                        designerName = "유리 실장",
                        styleName = "내츄럴 애즈펌 (6:4)",
                        price = 120000,
                        status = "APPROVED",
                        notes = "원장님의 제안을 토대로 예약 승인됨. 방문 일정 확정 대기 중.",
                        beforePhotoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300",
                        requestPhotoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300",
                        afterPhotoUrl = null
                    )
                )
                dao.insertVisit(
                    VisitHistory(
                        id = 3,
                        salonName = "헤어더뷰 신논현점",
                        visitDate = "2026-05-25",
                        designerName = "민아 디자이너",
                        styleName = "가일컷 & 리백 다운펌",
                        price = 55000,
                        status = "REQUESTED",
                        notes = "AI 헤어스타일 매칭 결과를 헤어더뷰에 시술 요청서로 보냈습니다. (승인 대기 중)",
                        beforePhotoUrl = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=300",
                        requestPhotoUrl = "https://images.unsplash.com/photo-1489980508314-941910ded1f4?w=300",
                        afterPhotoUrl = null
                    )
                )
            }
        }
    }

    // Set active model face preset
    fun selectFacePreset(face: PresetFace) {
        _selectedFace.value = face
        _aiAnalysisResult.value = "" // clear previous result to prompt fresh action
    }

    fun setCustomPhoto(base64: String?) {
        _customPhotoBase64.value = base64
        _selectedFace.value = null
        _aiAnalysisResult.value = ""
    }

    // Run Gemini face shape style suggestion
    fun requestAiHairstyleAnalysis() {
        viewModelScope.launch {
            _isAnalyzing.value = true
            val face = _selectedFace.value
            val isCustom = _customPhotoBase64.value != null

            val prompt = if (isCustom) {
                "사용자가 얼굴 사진을 직접 업로드하고 스타일 분석을 요청했습니다. 이마라인, 얼굴 윤곽 등을 고려해 트렌디한 남자 헤어스타일(댄디컷, 가일컷, 리프컷, 쉐도우펌 등)을 3개 이상 한글로 아주 세련되고 친절하며 신뢰감 가는 어조로 추천해 줘."
            } else {
                "얼굴형: ${face?.faceShape ?: "계란형"} (${face?.description ?: "부드러운 마스크"}) 에 맞춘 세련된 남자 헤어스타일 매칭 분석을 진행해 줘. 1. 얼굴 특징 분석, 2. 추천하는 헤어 스타일 3가지, 3. 미용실 시술시 볼륨/다운 핵심 요청 팁을 한국어로 전문적이고 세련되게 작성해 줘."
            }

            val result = GeminiClient.analyzeFaceAndSuggest(prompt)
            _aiAnalysisResult.value = result
            _isAnalyzing.value = false
        }
    }

    // Customer requests appointment with this hairstyle
    fun submitStylingRequestToSalon(salon: Salon, styleName: String, price: Int, notes: String, beforePhotoUrl: String?, requestPhotoUrl: String?) {
        viewModelScope.launch {
            val newRequest = VisitHistory(
                salonName = salon.name,
                visitDate = "2026-05-26",
                designerName = "현명한 원장 / 유리 담당",
                styleName = styleName,
                price = price,
                status = "REQUESTED",
                notes = notes,
                beforePhotoUrl = beforePhotoUrl,
                requestPhotoUrl = requestPhotoUrl,
                afterPhotoUrl = null
            )
            dao.insertVisit(newRequest)
        }
    }

    // Salon simulation - Designer approves appointment request
    fun approveStylingRequest(visit: VisitHistory) {
        viewModelScope.launch {
            val approvedVisit = visit.copy(status = "APPROVED", notes = "미용실에서 고객제안을 확인하고 예약을 최종 승인했습니다! 매장에서 뵙겠습니다.")
            dao.updateVisit(approvedVisit)
        }
    }

    // Salon simulation - Designer finishes the session (simulated complete)
    fun completeStylingRequest(visit: VisitHistory, afterPhoto: String) {
        viewModelScope.launch {
            val completedVisit = visit.copy(
                status = "COMPLETED",
                notes = "시술이 성공적으로 최종 완료되었습니다! 헤어핏 AI와의 비교 분석이 보관함(마이샵 로그북)에 저장되었습니다.",
                afterPhotoUrl = afterPhoto
            )
            dao.updateVisit(completedVisit)
        }
    }

    // Salon simulation - Delete history row
    fun deleteHistory(visit: VisitHistory) {
        viewModelScope.launch {
            dao.deleteVisit(visit)
        }
    }
}
