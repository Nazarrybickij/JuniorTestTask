package com.nazarrybickij.juniortesttask.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    @SerializedName("car_make")
    val carMake: String,
    @SerializedName("car_model")
    val carModel: String,
    @SerializedName("car_model_year")
    val carModelYear: Int,
    val description: String,
    val id: Int,
    val image: String,
    val video: String
):Parcelable