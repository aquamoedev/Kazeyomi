package com.kazeyomi.data.api

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor() {

    private var baseUrl: String = "http://localhost:4567/"
    private var api: SuwayomiApi? = null

    fun configure(baseUrl: String, username: String? = null, password: String? = null) {
        this.baseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .addInterceptor { chain ->
                val response = chain.proceed(chain.request())
                if (!response.isSuccessful) {
                    val body = response.body?.string() ?: ""
                    response.close()
                    throw ApiException(response.code, body.ifEmpty { "HTTP ${response.code}" })
                }
                response
            }
            .apply {
                if (!username.isNullOrBlank() && !password.isNullOrBlank()) {
                    addInterceptor { chain ->
                        val credentials = Credentials.basic(username, password)
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
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(SuwayomiApi::class.java)
    }

    fun getApi(): SuwayomiApi = api
        ?: throw IllegalStateException("API not configured. Call configure() first.")

    fun isConfigured(): Boolean = api != null

    fun getBaseUrl(): String = baseUrl
}


class ApiException(val code: Int, override val message: String) : Exception(message)
