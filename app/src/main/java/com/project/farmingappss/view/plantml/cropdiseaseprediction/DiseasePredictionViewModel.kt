package com.project.farmingappss.view.plantml.cropdiseaseprediction

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.farmingappss.model.PlantApiInstance
import com.project.farmingappss.model.SusyaApi
import com.project.farmingappss.model.data.Image
import com.project.farmingappss.model.data.SusyaDetectionResponse
import com.project.farmingappss.utilities.value
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import android.util.Base64;
import com.project.farmingappss.utilities.Empty

class DiseasePredictionViewModel: ViewModel() {

    val plantApi: SusyaApi = PlantApiInstance.plantApi

    private val _plantSusyaResponse= MutableLiveData<SusyaDetectionResponse>()


    fun getResultsForCropImage(base64Img: String): LiveData<SusyaDetectionResponse> {
        viewModelScope.launch {
            try{
                val response = plantApi.sendBase64Img(
                    Image(base64Img.value)
                )

                if (response != null && response.isSuccessful) {
                    _plantSusyaResponse.value = response.body() ?: SusyaDetectionResponse()
                }
            }
            catch (e: Exception) {
                _plantSusyaResponse.value =  SusyaDetectionResponse()
            }
        }

        return _plantSusyaResponse
    }

    fun imageToBase64 (bitmap: Bitmap): String {

        val byteArrayOs = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOs)

        val byteArr = byteArrayOs.toByteArray() ?: return String.Empty

        return Base64.encodeToString(byteArr, Base64.DEFAULT)
    }

}