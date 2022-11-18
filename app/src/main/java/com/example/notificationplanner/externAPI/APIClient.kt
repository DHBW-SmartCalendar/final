package com.example.notificationplanner.externAPI


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class APIClient {
    companion object {
        fun request(baseURL: BaseURL, call: (APICollection) -> Unit) {
            val api = Retrofit.Builder()
                .baseUrl(baseURL.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APICollection::class.java)
            call.invoke(api)
        }
    }
}





