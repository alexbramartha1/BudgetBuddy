package com.example.tugasreal.model

import com.google.gson.annotations.SerializedName

data class SearchNews(
    @SerializedName("status") val status:String,
    @SerializedName("totalResults") val totalResults:Int,
    @SerializedName("articles") val articles:List<News>,
    @SerializedName("Response") val res:String
)
