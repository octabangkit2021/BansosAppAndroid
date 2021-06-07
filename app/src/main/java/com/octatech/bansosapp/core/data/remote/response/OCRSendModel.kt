package com.octatech.bansosapp.core.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OCRSendModel {
    @SerializedName("url")
    @Expose
    var url : String ? = null
}