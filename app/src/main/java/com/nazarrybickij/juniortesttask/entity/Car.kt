package com.nazarrybickij.juniortesttask.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    val car_make: String,
    val car_model: String,
    val car_model_year: Int,
    val description: String,
    val id: Int,
    val image: String,
    val video: String
):Parcelable