package com.octatech.bansosapp.ui.RegisterBansos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.data.Resource
import com.octatech.bansosapp.core.data.remote.network.ApiConfig
import com.octatech.bansosapp.core.data.remote.response.ApiResponse
import com.octatech.bansosapp.core.data.remote.response.OCRResponse
import com.octatech.bansosapp.core.data.remote.response.OCRSendModel
import com.octatech.bansosapp.core.ui.ViewModelFactory
import com.octatech.bansosapp.databinding.FragmentKtpRegisterBinding
import id.ionbit.ionalert.IonAlert
import pl.aprilapps.easyphotopicker.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.math.log

private const val NOMOR_KTP = "nomorKTP"
private const val PEKERJAAN = "pekerjaan"
private const val PENDAPATAN = "pendapatan"
private const val TANGGUNGAN = "tanggungan"
private const val KODE_BANSOS = "kodeBansos"
private const val NOMOR_HP = "nomorHP"
class KtpRegisterFragment : Fragment() {

    private var _binding: FragmentKtpRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var easyImage: EasyImage;
    private lateinit var registerViewModel: RegisterViewModel
    private var nomorKTP : String? = null;
    private var pekerjaan : String? = null;
    private var pendapatan : String? = null;
    private var tanggungan : String? = null;
    private var nomorHP : String? = null;
    private var imageUrl : Uri? = null ;
    private var kodeBansos : String? = null ;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nomorKTP = it.getString(NOMOR_KTP)
            pekerjaan = it.getString(PEKERJAAN)
            pendapatan = it.getString(PENDAPATAN)
            tanggungan = it.getString(TANGGUNGAN)
            kodeBansos = it.getString(KODE_BANSOS)
            nomorHP = it.getString(NOMOR_HP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKtpRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(view != null){
            binding.btnKtpRegister.setOnClickListener {
                easyImage.openChooser(this)
            }

            binding.btnNextRegisterKtp.isEnabled = false
            easyImage = EasyImage.Builder(requireContext())
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("BansosApp_ktp_")
                .allowMultiple(true)
                .build()

            var factory = ViewModelFactory.getInstance(requireContext())
            registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]


            binding.btnNextRegisterKtp.setOnClickListener {
                val client = ApiConfig.provideApiService()
                var model = OCRSendModel()
                model.url = imageUrl.toString()
                setLoading(true)
                binding.btnNextRegisterKtp.isEnabled = false
                client.getOCR(model).enqueue(object : Callback<OCRResponse> {
                    override fun onResponse(call: Call<OCRResponse>, response: Response<OCRResponse>) {
                        if(response.isSuccessful){
                            setLoading(false)
                            val data = response.body()
                            if(nomorKTP != data?.result){
                                setLoading(false)
                                IonAlert(requireContext(), IonAlert.ERROR_TYPE)
                                    .setTitleText("Gagal")
                                    .setContentText("Pendaftaran gagal, Mohon periksa koneksi anda")
                                    .show()
                            } else {
                                val fragment = KKRegisterFragment.newInstance(nomorKTP!!, pekerjaan.toString(), pendapatan.toString(), tanggungan.toString(), imageUrl.toString(), kodeBansos!! , nomorHP!!)
                                fragmentManager?.beginTransaction()?.replace(R.id.fl_register, fragment)?.commit()
                            }
                        }
                    }

                    override fun onFailure(call: Call<OCRResponse>, t: Throwable) {
                        Log.e("RemoteDataSource", "onFailure: " + t.message.toString(), )
                    }

                })
            }
        }
    }

    fun setLoading(load: Boolean){
       if(load){
           binding.loadingViewKtp.root.visibility = View.VISIBLE
           binding.btnKtpRegister.visibility = View.GONE
           binding.ivKtpRegister.visibility = View.GONE
           binding.textView5.visibility = View.GONE
           binding.progressKtp.visibility = View.GONE
       } else {
           binding.loadingViewKtp.root.visibility = View.GONE
           binding.btnKtpRegister.visibility = View.VISIBLE
           binding.ivKtpRegister.visibility = View.VISIBLE
           binding.textView5.visibility = View.VISIBLE
           binding.progressKtp.visibility = View.GONE
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            requireActivity(),
            object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    val imgFile = File(imageFiles.get(0).file.toString())
                    var uri = Uri.fromFile(imgFile)
                    var storageReference = FirebaseStorage.getInstance().getReference(nomorKTP+"_KTP")
                    storageReference.putFile(uri).addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener {
                            imageUrl = it
                            binding.ivKtpRegister.setImageURI(Uri.fromFile(imgFile))
                            binding.btnNextRegisterKtp.isEnabled = true
                        }
                    }.addOnFailureListener{
                        Log.e("ERROR UPLOAD", "onMediaFilesPicked: " + it.message )
                    }.addOnProgressListener {
                        binding.progressKtp.visibility = View.VISIBLE
                        binding.btnKtpRegister.visibility = View.GONE
                    }.addOnCompleteListener{
                        binding.progressKtp.visibility = View.GONE
                        binding.btnKtpRegister.visibility = View.VISIBLE
                    }
                }

                override fun onImagePickerError(error: Throwable, source: MediaSource) {
                    //Some error handling
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) {
                    //Not necessary to remove any files manually anymore
                }
            })
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String, param4: String, param5 : String, param6 : String) =
            KtpRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(NOMOR_KTP, param1)
                    putString(PEKERJAAN, param2)
                    putString(PENDAPATAN, param3)
                    putString(TANGGUNGAN, param4)
                    putString(KODE_BANSOS, param5)
                    putString(NOMOR_HP, param6)
                }
            }
    }
}