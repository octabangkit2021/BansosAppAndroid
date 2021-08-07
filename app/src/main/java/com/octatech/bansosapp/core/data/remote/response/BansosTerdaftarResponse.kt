package com.octatech.bansosapp.core.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BansosTerdaftarResponse (
    var pengajuan_active : String,
    var qr_code : String,
    var status : String
        ) : Parcelable