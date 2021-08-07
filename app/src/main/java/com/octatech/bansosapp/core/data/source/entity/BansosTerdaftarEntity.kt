package com.octatech.bansosapp.core.data.source.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "BansosTerdaftarList")
data class BansosTerdaftarEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "pengajuan_active")
    var pengajuan_active :String,

    @ColumnInfo(name = "qr_code")
    var qr_code : String,

    @ColumnInfo(name = "status")
    var status : String,
) : Parcelable