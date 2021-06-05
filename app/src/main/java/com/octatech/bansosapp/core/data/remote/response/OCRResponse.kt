package com.octatech.bansosapp.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class OCRResponse(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("raw")
	val raw: String
)
