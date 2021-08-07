package com.octatech.bansosapp.core.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BansosResponse(
    var bansosId :String,
    var bansosName : String,
    var bansosDeskripsi : String,
    var bansosBerlaku : String,
    var bansosPersyaratan : String,
    var bansosGambar : String,
    var bansosIsi : String
) : Parcelable