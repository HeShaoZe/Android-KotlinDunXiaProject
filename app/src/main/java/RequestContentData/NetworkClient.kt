package RequestContentData

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object NetworkClient {
    // 全局单例
    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        // 可以添加拦截器用于日志打印或统一添加 Token
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Content-Type", "application/json")
                // .header("Authorization", "Bearer $token") // 如果有 Token
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .build()
}
