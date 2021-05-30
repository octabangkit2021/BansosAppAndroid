package com.octatech.bansosapp.core.data.source.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "BansosList")
data class BansosEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "bansoId")
    var bansosId :String,

    @ColumnInfo(name = "bansosName")
    var bansosName : String,

    @ColumnInfo(name = "bansosDeskripsi")
    var bansosDeskripsi : String,

    @ColumnInfo(name = "bansosBerlaku")
    var bansosBerlaku : String,

    @ColumnInfo(name = "bansosPersyaratan")
    var bansosPersyaratan : String,

    @ColumnInfo(name = "bansosGambar")
    var bansosGambar : String,

    @ColumnInfo(name = "bansosIsi")
    var bansosIsi : String,
) : Parcelable
