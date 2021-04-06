package com.example.tocarcar.utility

import com.example.tocarcar.entity.Car
import com.example.tocarcar.entity.Posting
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Utils
 * @author Harman
 */
class JsonHelper {
    companion object{
        fun getMoshiCarAdapter(): com.squareup.moshi.JsonAdapter<Car> {
            val carType = Types.newParameterizedType(Car::class.java)
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            return moshi.adapter(carType)
        }

        fun getMoshiPostingAdapter(): com.squareup.moshi.JsonAdapter<Posting> {
            val postingType = Types.newParameterizedType(Posting::class.java)
            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            return moshi.adapter(postingType)
        }

        fun getUpdateRequestBody(myQuery: String, updateString: String): String{
            return "{ " +
                        "\"myQuery\": $myQuery, " +
                        "\"newValues\": {" +
                            "\"\$set\": {" +
                                "$updateString" +
                            "}" +
                        "}" +
                    "}"
        }
    }
}