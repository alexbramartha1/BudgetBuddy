package com.example.tugasreal

import com.example.tugasreal.model.SearchNews
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("everything?q=keuangan&sortBy=popularity&apiKey=2fcbe4f06c434a2c9785f761cb02a828")
    fun getNews():Call<SearchNews>
}