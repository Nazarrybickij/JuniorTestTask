package com.nazarrybickij.juniortesttask.network

class ApiHelper(private val apiService: ApiService) {
    suspend fun getCars(count:Int, key:String) = apiService.getCars(count, key)
}