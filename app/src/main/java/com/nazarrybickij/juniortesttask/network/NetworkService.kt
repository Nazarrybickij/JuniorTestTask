package com.nazarrybickij.juniortesttask.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NetworkService private constructor() {
    private val mRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: ApiService
        get() = mRetrofit.create(ApiService::class.java)

    companion object {


        val BASE_URL = "https://api.mockaroo.com/api/"
        val COUNT = 100
        val KEY = "3cac2af0"

        private var mInstance: NetworkService? = null
        val instance: NetworkService?
            get() {
                if (mInstance == null) {
                    mInstance =
                        NetworkService()
                }
                return mInstance
            }
    }


}