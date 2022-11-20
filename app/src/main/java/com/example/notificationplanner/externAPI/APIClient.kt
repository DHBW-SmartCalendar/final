package com.example.notificationplanner.externAPI


import com.example.notificationplanner.data.NotificationType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class APIClient {
    companion object {
        fun request(APIType: NotificationType, call: (APICollection) -> Unit) {
            val api = Retrofit.Builder()
                .baseUrl(APIType.url!!)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APICollection::class.java)
            call.invoke(api)
        }
    }
}





