package com.nazarrybickij.juniortesttask.network

import com.nazarrybickij.juniortesttask.entity.Car
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("70d65120")
    suspend fun getCars(@Query(value = "count")count:Int,@Query(value = "key")key:String): ArrayList<Car>
}