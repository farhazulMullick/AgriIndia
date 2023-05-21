package com.project.farmingappss.model

import com.project.farmingappss.model.data.Image
import com.project.farmingappss.model.data.KrishiMlResponse
import com.project.farmingappss.model.data.SusyaDetectionResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface SusyaApi {

    @POST(".")
    suspend fun sendBase64Img(
        @Body image: Image
    ): Response<SusyaDetectionResponse>?
}

interface KrishiMlApi{

    @POST("/predict")
    @FormUrlEncoded
    suspend fun predictCrop(
        @FieldMap map: Map<String, String>
    ): Response<KrishiMlResponse>

}

object KrishiMlApiInstance {
    val krishiMlApi: KrishiMlApi

    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://krishiml.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        krishiMlApi = retrofit.create()
    }
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