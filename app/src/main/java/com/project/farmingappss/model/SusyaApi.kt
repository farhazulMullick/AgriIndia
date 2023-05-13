package com.project.farmingappss.model

import com.project.farmingappss.model.data.Image
import com.project.farmingappss.model.data.SusyaDetectionResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SusyaApi {

    @POST(".")
    suspend fun sendBase64Img(
        @Body image: Image
    ): Response<SusyaDetectionResponse>?
}

object PlantApiInstance {
    val plantApi: SusyaApi

    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://susya.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        plantApi = retrofit.create()
    }


}