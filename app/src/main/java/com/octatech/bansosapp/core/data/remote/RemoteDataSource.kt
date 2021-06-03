package com.octatech.bansosapp.core.data.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.octatech.bansosapp.core.data.remote.response.ApiResponse
import com.octatech.bansosapp.core.data.remote.response.BansosResponse


class RemoteDataSource() {
    var db = FirebaseFirestore.getInstance()
    fun getAllBansos(): LiveData<ApiResponse<List<BansosResponse>>> {
        val resultData = MutableLiveData<ApiResponse<List<BansosResponse>>>()
        db.collection("list_bansos").get().addOnSuccessListener {
            var dataArray = ArrayList<BansosResponse>();
            dataArray.clear()
            for(document in it){
                dataArray.add(BansosResponse(document.data.get("id_bansos") as String, document.data.get("nama_bansos") as String, document.data.get("deskripsi_bansos") as String, document.data.get("berlaku_bansos") as String, document.data.get("persyaratan") as String, document.data.get("gambar_bansos") as String, document.data.get("isi") as String ))
            }
            Log.e("TRAP", "getAllTourism: " + dataArray )
            resultData.value = if (dataArray != null) ApiResponse.Success(dataArray) else ApiResponse.Empty
        }
        return resultData
    }

    fun uploadPhoto(fileName : String, file : Uri ){
        var storageReference = FirebaseStorage.getInstance().getReference("$fileName")
        var result : Uri
        storageReference.putFile(file).addOnSuccessListener {
            storageReference.downloadUrl.addOnSuccessListener {
                    var uri : Uri = it
                    result = uri;

            }
        }.addOnFailureListener{

        }
    }
}