package com.project.farmingappss.viewmodel

import androidx.lifecycle.LiveData

interface ArticleListener {

    fun onStarted()
    fun onSuccess(authRepo: LiveData<String>)
    fun onFailure(message: String)

}