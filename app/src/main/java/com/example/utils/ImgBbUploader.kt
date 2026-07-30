package com.example.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object ImgBbUploader {
    const val API_KEY = "6d207e02198a847aa98d0a2a901485a5"
    private const val UPLOAD_URL = "https://api.imgbb.com/1/upload"

    // Real HD realistic avatar presets for instant selection
    val REALISTIC_AVATARS = listOf(
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1517841905240-472988babdf9?q=80&w=600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=600&auto=format&fit=crop"
    )

    /**
     * Uploads image base64 or URL to ImgBB and returns the live `https://i.ibb.co/...` direct link.
     */
    suspend fun uploadToImgBb(inputUrlOrBase64: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val trimmed = inputUrlOrBase64.trim()
            if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
                return@withContext Result.success(trimmed)
            }

            val postData = "key=" + URLEncoder.encode(API_KEY, "UTF-8") +
                    "&image=" + URLEncoder.encode(trimmed, "UTF-8")

            val url = URL(UPLOAD_URL)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                doInput = true
                setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                connectTimeout = 12000
                readTimeout = 12000
            }

            conn.outputStream.use { os ->
                os.write(postData.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            val inputStream: InputStream = if (responseCode == HttpURLConnection.HTTP_OK) {
                conn.inputStream
            } else {
                conn.errorStream ?: conn.inputStream
            }

            val responseText = inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(responseText)

            if (json.optBoolean("success")) {
                val dataObj = json.getJSONObject("data")
                val imageUrl = dataObj.getString("url")
                Result.success(imageUrl)
            } else {
                val errMessage = json.optJSONObject("error")?.optString("message") ?: "Upload failed"
                Result.failure(Exception(errMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
