package com.project.farmingappss.model.data

import com.google.gson.annotations.SerializedName
import com.project.farmingappss.utilities.value

data class Image(
    @SerializedName("image")
    val imgBase64Str: String? = null
)

data class SusyaDetectionResponse(
    @SerializedName("disease")
    private val _disease: String? = null,

    @SerializedName("remedy")
    private val _remedy : String? = null,

    @SerializedName ("plant")
    private val _plantName: String? = null
) {
    val disease: String get() = _disease.value
    val remedy : String get() = _remedy.value
    val plantName: String get() = _plantName.value
}

