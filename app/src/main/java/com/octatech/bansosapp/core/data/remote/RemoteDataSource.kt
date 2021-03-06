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
import com.octatech.bansosapp.core.data.remote.network.ApiConfig
import com.octatech.bansosapp.core.data.remote.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
            Log.e("TRAP", "getAllBansos: " + dataArray )
            resultData.value = if (dataArray != null) ApiResponse.Success(dataArray) else ApiResponse.Empty
        }
        return resultData
    }

    fun getBansosTerdaftar(nomor_ktp: String): LiveData<ApiResponse<List<BansosTerdaftarResponse>>> {
        val resultData = MutableLiveData<ApiResponse<List<BansosTerdaftarResponse>>>()
        db.collection("history_pengajuan").document(nomor_ktp).get().addOnSuccessListener {
            var dataArray = ArrayList<BansosTerdaftarResponse>();
            dataArray.clear()
            if(it.getString("pengajuan_active").isNullOrEmpty()){
                dataArray.add(BansosTerdaftarResponse("", "", "Tidak Ada Pengajuan"))
            } else {
                dataArray.add(BansosTerdaftarResponse(it.getString("pengajuan_active") as String, it.getString("qr_code") as String, it.getString("status") as String))
            }
            Log.e("TRAP", "getAllBansos: " + dataArray )
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

    fun getOCR(link : String) : LiveData<ApiResponse<OCRResponse>>{
        val resultData = MutableLiveData<ApiResponse<OCRResponse>>()

        val client = ApiConfig.provideApiService()
        var model = OCRSendModel()
        model.url = link
        client.getOCR(model).enqueue(object : Callback<OCRResponse>{
            override fun onResponse(call: Call<OCRResponse>, response: Response<OCRResponse>) {
               if(response.isSuccessful){
                    val data = response.body()
                   resultData.value = if (data != null) ApiResponse.Success(data) else ApiResponse.Empty
               }
            }

            override fun onFailure(call: Call<OCRResponse>, t: Throwable) {
                resultData.value = ApiResponse.Error(t.message.toString())
                Log.e("RemoteDataSource", "onFailure: " + t.message.toString(), )
            }

        })
        return resultData
    }
}