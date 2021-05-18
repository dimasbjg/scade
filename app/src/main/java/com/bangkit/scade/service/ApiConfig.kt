package com.bangkit.scade.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        @Volatile
        private var retrofit: Retrofit? = null
        fun getInstance(): Retrofit {
            val client = OkHttpClient.Builder()
                .build()
            return retrofit ?: synchronized(this) {
                retrofit ?: Retrofit.Builder()
                    .baseUrl("http://35.213.130.133:8008/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
        }
    }
}