package com.nazarrybickij.juniortesttask.network

import com.nazarrybickij.juniortesttask.network.NetworkService.Companion.COUNT
import com.nazarrybickij.juniortesttask.network.NetworkService.Companion.KEY


class NetworkRepository(private val apiHelper: ApiHelper) {
    suspend fun getCars() = apiHelper.getCars(COUNT, KEY)
}