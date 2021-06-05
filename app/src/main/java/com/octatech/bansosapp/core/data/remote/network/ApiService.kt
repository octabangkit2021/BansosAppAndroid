package com.octatech.bansosapp.core.data.remote.network

import com.octatech.bansosapp.core.data.remote.response.OCRResponse
import com.octatech.bansosapp.core.data.remote.response.OCRSendModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type: application/json")
    @GET("ocr")
    fun getOCR(@Body body : OCRSendModel): Call<OCRResponse>

}