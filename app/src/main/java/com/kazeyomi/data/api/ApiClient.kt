package com.kazeyomi.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor() {
    private var baseUrl: String = "http://localhost:4567/"
    private var api: SuwayomiApi? = null
    private var authHeader: String? = null

    fun configure(baseUrl: String, username: String? = null, password: String? = null) {
        this.baseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
        
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .apply {
                if (username != null && password != null) {
                    addInterceptor { chain ->
                        val credentials = okhttp3.Credentials.basic(username, password)
                        val request = chain.request().newBuilder()
                            .header("Authorization", credentials)
                            .build()
                        chain.proceed(request)
                    }
                }
            }
            .build()

        api = Retrofit.Builder()
            .baseUrl(this.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SuwayomiApi::class.java)
    }

    fun getApi(): SuwayomiApi {
        return api ?: throw IllegalStateException("API not configured. Call configure() first.")
    }

    fun isConfigured(): Boolean = api != null

    fun getBaseUrl(): String = baseUrl
}
