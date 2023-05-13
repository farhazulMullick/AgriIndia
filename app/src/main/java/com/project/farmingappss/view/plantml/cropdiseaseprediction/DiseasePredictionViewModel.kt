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
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import com.project.farmingappss.utilities.Empty
import java.util.Observable

class DiseasePredictionViewModel: ViewModel(), androidx.databinding.Observable {

    val plantApi: SusyaApi = PlantApiInstance.plantApi

    @get:Bindable
    var loading: ObservableBoolean = ObservableBoolean(false)

    private val _plantSusyaResponse= MutableLiveData<SusyaDetectionResponse>()


    fun getResultsForCropImage(base64Img: String): LiveData<SusyaDetectionResponse> {
        viewModelScope.launch {
            loading.set(true)
            try{
                val response = plantApi.sendBase64Img(
                    Image(base64Img.value)
                )

                if (response != null && response.isSuccessful) {
                    loading.set(false)
                    _plantSusyaResponse.value = response.body() ?: SusyaDetectionResponse()
                }
            }
            catch (e: Exception) {
                loading.set(false)
                _plantSusyaResponse.value =  SusyaDetectionResponse()
            }
        }

        return _plantSusyaResponse
    }

    fun imageToBase64 (bitmap: Bitmap): String {

        val byteArrayOs = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOs)

        val byteArr = byteArrayOs.toByteArray() ?: return String.Empty

        val base64msg = Base64.encodeToString(byteArr, Base64.DEFAULT).toString()

        Log.d("imageToBase64()", base64msg)

        return base64msg
    }

    override fun addOnPropertyChangedCallback(callback: androidx.databinding.Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: androidx.databinding.Observable.OnPropertyChangedCallback?) {

    }

}