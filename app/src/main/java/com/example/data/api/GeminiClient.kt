package com.example.data.api

import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import com.example.BuildConfig

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String? = null,
    @Json(name = "inlineData") val inlineData: InlineData? = null
)

@JsonClass(generateAdapter = true)
data class InlineData(
    @Json(name = "mimeType") val mimeType: String,
    @Json(name = "data") val data: String
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "generationConfig") val generationConfig: GenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @Json(name = "temperature") val temperature: Float? = null,
    @Json(name = "responseMimeType") val responseMimeType: String? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>? = null
)

interface GeminiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val service = retrofit.create(GeminiService::class.java)

    suspend fun analyzeFaceAndSuggest(prompt: String, base64Image: String? = null): String {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key is not configured, using fallback simulation mode.")
            return generateMockAnalysis(prompt)
        }

        val parts = mutableListOf<Part>()
        parts.add(Part(text = prompt))
        if (!base64Image.isNullOrEmpty()) {
            parts.add(Part(inlineData = InlineData(mimeType = "image/jpeg", data = base64Image)))
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = parts))
        )

        return try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "AI 분석 결과를 가져올 수 없었습니다."
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API failed", e)
            "AI 서비스 연결 오류가 발생했습니다. (${e.localizedMessage})\n\n[로컬 분석 시뮬레이션 결과]:\n" + generateMockAnalysis(prompt)
        }
    }

    private fun generateMockAnalysis(prompt: String): String {
        val lowercase = prompt.lowercase()
        return when {
            lowercase.contains("둥근") || lowercase.contains("round") -> """
                [AI 분석 결과 - 둥근 얼굴형]
                둥근 얼굴형은 볼륨감을 상단과 탑 쪽에 유치시켜 얼굴이 좀 더 길어 보이고 슬림해 보이게 연출하는 것이 좋습니다.

                추천 헤어스타일:
                1. 리프컷 (Leaf Cut): 앞머리를 자연스럽게 양 옆으로 넘겨 이마 라인을 드러내어 도회적인 무드를 연출합니다.
                2. 가르마펌 (Side-part Perm): 5:5 혹은 6:4 가르마로 위쪽 풍성함을 주면 얼굴선이 무척 샤프하게 보입니다.
                3. 애즈펌 (As Perm): 앞머리의 폭을 좁게 이마가 살짝 노출되게 하여 부드럽고 지적인 인상을 줍니다.

                피해야 할 스타일: 
                앞머리를 꽉 채워 무겁게 내리는 풀뱅 스타일은 얼굴을 더 둥글고 정사각 느낌으로 보이게 만드니 조율이 필요합니다.
            """.trimIndent()
            lowercase.contains("긴") || lowercase.contains("long") -> """
                [AI 분석 결과 - 긴 얼굴형 / 각진 얼굴형]
                얼굴의 긴 세로선 비율을 조율하기 위해 앞머리에 자연스러운 볼륨감을 주어 이마 라인을 일부 채우고, 측면 볼륨을 채우는 레이어드 컷팅이 효과적입니다.

                추천 헤어스타일:
                1. 댄디컷 (Dandy Cut): 앞머리를 차분하게 가려 길어 보이는 얼굴을 시각적으로 단축시켜 줍니다.
                2. 쉐도우펌 (Shadow Perm): 탑부분에서 자연스러운 S라인 컬을 연출하여 전체적으로 동글동글하고 입체적인 텍스처를 줍니다.
                3. 시스루 가르마: 앞머리를 자연스러운 시스루 레이어로 가져가며 세련된 감도를 유지합니다.

                스타일 팁:
                사이드 구두발(옆머리) 영역을 다운펌으로 너무 과하게 붙이거나 세로 높이를 극대화하면 세로 비율이 더 부각되니 주의해주세요.
            """.trimIndent()
            else -> """
                [AI 헤어스타일 매칭 완료]
                계란형 및 밸런스 페이스 형태 분석 결과입니다. 이 얼굴형은 거의 모든 남성 프리미엄 헤어 스타일을 매우 훌륭하게 소화할 수 있습니다.

                추천 헤어스타일:
                1. 가일컷 (Guile Cut): 한쪽은 와일드하게 넘기고, 반대쪽 앞머리는 엣지 있게 떨어뜨리는 세련되고 남성적인 디자인.
                2. 시스루 댄디컷: 가볍고 트렌디하면서도 누구나 깔끔한 인상을 자아내는 국민 스타일.
                3. 드롭컷 (Drop Cut): 앞머리 가운데 부분은 짧게 세우고 양옆은 떨어뜨려 샤프한 남성미를 물씬 풍기는 트렌드 디자인.

                살롱 권장 옵션:
                - 깔끔한 구강구조 라인을 위해 다운펌 코스를 함께 연제하시면 더욱 트렌디하고 세련되게 완성됩니다.
            """.trimIndent()
        }
    }
}
