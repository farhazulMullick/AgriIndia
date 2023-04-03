package com.project.farmingappss.viewmodel

import androidx.lifecycle.LiveData

interface WeatherListener {
    fun onSuccess(authRepo: LiveData<String>)
}