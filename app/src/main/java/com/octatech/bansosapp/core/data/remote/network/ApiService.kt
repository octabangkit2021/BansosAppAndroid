package com.octatech.bansosapp.core.data.remote.network

import com.octatech.bansosapp.core.data.remote.response.OCRResponse
import com.octatech.bansosapp.core.data.remote.response.OCRSendModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("ocr")
    fun getOCR(@Body body : OCRSendModel): Call<OCRResponse>

}