package com.octatech.bansosapp.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bansos(
    var bansosId :String,
    var bansosName : String,
    var bansosDeskripsi : String,
    var bansosBerlaku : String,
    var bansosPersyaratan : String,
    var bansosGambar : String,
    var bansosIsi : String
) : Parcelable