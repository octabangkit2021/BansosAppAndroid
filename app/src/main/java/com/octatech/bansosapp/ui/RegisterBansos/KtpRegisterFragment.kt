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
class KtpRegisterFragment : Fragment() {

    private var _binding: FragmentKtpRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var easyImage: EasyImage;
    private lateinit var registerViewModel: RegisterViewModel
    private var nomorKTP : String? = null;
    private var pekerjaan : String? = null;
    private var pendapatan : String? = null;
    private var tanggungan : String? = null;
    private var imageUrl : Uri? = null ;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nomorKTP = it.getString(NOMOR_KTP)
            pekerjaan = it.getString(PEKERJAAN)
            pendapatan = it.getString(PENDAPATAN)
            tanggungan = it.getString(TANGGUNGAN)
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

            easyImage = EasyImage.Builder(requireContext())
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("BansosApp_ktp_")
                .allowMultiple(true)
                .build()

            var factory = ViewModelFactory.getInstance(requireContext())
            registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
            Toast.makeText(requireActivity(), nomorKTP, Toast.LENGTH_LONG).show()


            binding.btnNextRegisterKtp.setOnClickListener {
                val client = ApiConfig.provideApiService()
                var model = OCRSendModel()
                model.url = imageUrl.toString()
                client.getOCR(model).enqueue(object : Callback<OCRResponse> {
                    override fun onResponse(call: Call<OCRResponse>, response: Response<OCRResponse>) {
                        if(response.isSuccessful){
                            val data = response.body()
                            Log.d("HASILOCR", "onResponse: " + data)
                        }
                    }

                    override fun onFailure(call: Call<OCRResponse>, t: Throwable) {
                        Log.e("RemoteDataSource", "onFailure: " + t.message.toString(), )
                    }

                })
//                registerViewModel.getOCR(imageUrl.toString()).observe(viewLifecycleOwner, {
//                    if(it !== null){
//                        when(it){
//                            is Resource.Loading ->  setLoading(true)
//                            is Resource.Success -> {
//                                Toast.makeText(requireContext(), it.data?.result, Toast.LENGTH_LONG).show()
//                                setLoading(false)
//                                val fragment = KKRegisterFragment.newInstance(nomorKTP!!, pekerjaan.toString(), pendapatan.toString(), tanggungan.toString(), imageUrl.toString() )
//                                fragmentManager?.beginTransaction()?.replace(R.id.fl_register, fragment)?.commit()
//                            }
//                            is Resource.Error -> print("ERROR GET DATA")
//                        }
//                    }
//                })
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
                    var storageReference = FirebaseStorage.getInstance().getReference(nomorKTP+"KTP_")
                    storageReference.putFile(uri).addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener {
                            imageUrl = it
                            binding.ivKtpRegister.setImageURI(Uri.fromFile(imgFile))
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
        fun newInstance(param1: String, param2: String, param3: String, param4: String) =
            KtpRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(NOMOR_KTP, param1)
                    putString(PEKERJAAN, param2)
                    putString(PENDAPATAN, param3)
                    putString(TANGGUNGAN, param4)
                }
            }
    }
}