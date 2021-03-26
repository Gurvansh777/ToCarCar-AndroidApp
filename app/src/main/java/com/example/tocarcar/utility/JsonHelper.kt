package com.example.tocarcar.utility

import com.example.tocarcar.entity.Car
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JsonHelper {
    companion object{
        fun getMoshiCarAdapter(): com.squareup.moshi.JsonAdapter<Car> {
            val carType = Types.newParameterizedType(Car::class.java)
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            return moshi.adapter(carType)
        }
    }
}