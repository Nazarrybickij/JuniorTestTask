package com.nazarrybickij.juniortesttask.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.nazarrybickij.juniortesttask.network.ApiHelper
import com.nazarrybickij.juniortesttask.network.NetworkRepository
import com.nazarrybickij.juniortesttask.network.NetworkService
import com.nazarrybickij.juniortesttask.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi

class MainViewModel : ViewModel() {
    private val networkRepository = NetworkRepository(ApiHelper(NetworkService.instance!!.apiService))

    @InternalCoroutinesApi
    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            emit(Resource.success(networkRepository.getCars()))
        } catch (e: Exception) {
            emit(Resource.failed(e.message.toString()))
            Log.e("ERROR:", e.message.toString())
        }
    }
}
