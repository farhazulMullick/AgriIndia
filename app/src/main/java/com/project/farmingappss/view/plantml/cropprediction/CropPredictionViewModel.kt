package com.project.farmingappss.view.plantml.cropprediction

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.farmingappss.model.KrishiMlApi
import com.project.farmingappss.model.KrishiMlApiInstance
import com.project.farmingappss.model.data.KrishiMlResponse
import kotlinx.coroutines.launch
import java.lang.Exception

class CropPredictionViewModel : ViewModel() {

    private val krishiMlApi: KrishiMlApi = KrishiMlApiInstance.krishiMlApi
    var loading: ObservableBoolean = ObservableBoolean(false)

    fun getPredictionResult(requestMap: Map<String, String>): MutableLiveData<KrishiMlResponse> {
        val _cropPredictionLiveData = MutableLiveData<KrishiMlResponse>()
        val noData = MutableLiveData<Unit>()

        viewModelScope.launch {
            try {
                loading.set(true)
                val response = krishiMlApi.predictCrop(requestMap)

                if (response.isSuccessful) {
                    loading.set(false)
                    _cropPredictionLiveData.value = response.body()
                }
            }
            catch (e: Exception){
                noData.value = Unit
                loading.set(false)
            }

        }

        return _cropPredictionLiveData
    }



}