package com.octatech.bansosapp.core.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OCRSendModel {
    @SerializedName("link")
    @Expose
    var link : String ? = null
}