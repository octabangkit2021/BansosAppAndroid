package com.octatech.bansosapp.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Registrasi(
    var nomorKtp : Int?,
    var pekerjaan : String?,
    var pendapatan : Int?,
    var tanggungan : Int?,
) : Parcelable {
    fun copyWith(nomorKtp: Int?, pekerjaan: String?, pendapatan: Int?, tanggungan: Int?, imageLinkPendukung: String?, imageLinkKK: String?, imageLinkKtp: String?): Registrasi? {
        return Registrasi(
            nomorKtp  = nomorKtp ?: this.nomorKtp,
            pekerjaan = pekerjaan ?: this.pekerjaan,
            pendapatan = pendapatan ?: this.pendapatan,
            tanggungan = tanggungan ?: this.tanggungan,
        )
    }
}
