package RequestContentData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class UserRepository {

    // 模拟一个简单的 JSON 解析 (实际建议用 Moshi/Gson)
    suspend fun getUser(userId: Int): Result<User> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.example.com/users/$userId"

            val request = okhttp3.Request.Builder()
                .url(url)
                .get()
                .build()

            // execute() 是阻塞方法，所以必须在 IO 线程 (withContext) 中运行
            NetworkClient.client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("请求失败: ${response.code}"))
                }

                val responseBody = response.body?.string()
                if (responseBody == null) {
                    return@withContext Result.failure(Exception("响应体为空"))
                }

                // --- 手动解析 JSON 示例 (不推荐复杂项目使用) ---
                val json = JSONObject(responseBody)
                val user = User(
                    id = json.getInt("id"),
                    name = json.getString("name"),
                    age = json.getInt("age")
                )
                // ------------------------------------------

                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

