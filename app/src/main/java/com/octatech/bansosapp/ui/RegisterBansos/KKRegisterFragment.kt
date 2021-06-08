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
import com.octatech.bansosapp.core.ui.ViewModelFactory
import com.octatech.bansosapp.databinding.FragmentKKRegisterBinding
import com.octatech.bansosapp.databinding.FragmentKtpRegisterBinding
import pl.aprilapps.easyphotopicker.*
import java.io.File


private const val NOMOR_KTP = "nomorKTP"
private const val PEKERJAAN = "pekerjaan"
private const val PENDAPATAN = "pendapatan"
private const val TANGGUNGAN = "tanggungan"
private const val IMAGEKTP = "imageKTP"
private const val KODE_BANSOS = "kodeBansos"
class KKRegisterFragment : Fragment() {

    private var _binding: FragmentKKRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var easyImage: EasyImage;
    private lateinit var registerViewModel: RegisterViewModel
    private var nomorKTP : String? = null;
    private var pekerjaan : String? = null;
    private var pendapatan : String? = null;
    private var tanggungan : String? = null;
    private var imageKTP : String? = null;
    private var kodeBansos : String? = null;
    private var imageUrl : Uri? = null ;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nomorKTP = it.getString(NOMOR_KTP)
            pekerjaan = it.getString(PEKERJAAN)
            pendapatan = it.getString(PENDAPATAN)
            tanggungan = it.getString(TANGGUNGAN)
            imageKTP = it.getString(IMAGEKTP)
            kodeBansos = it.getString(KODE_BANSOS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKKRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(view != null){
            binding.btnKkUpload.setOnClickListener {
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
            binding.btnNextRegisterKk.setOnClickListener {
                val fragment = DokumenRegisterFragment.newInstance(nomorKTP!!, pekerjaan!!, pendapatan!!, tanggungan!!, imageKTP!!, imageUrl.toString(), kodeBansos!!)
                fragmentManager?.beginTransaction()?.replace(R.id.fl_register, fragment)?.commit()
            }

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
                    var storageReference = FirebaseStorage.getInstance().getReference(nomorKTP+"_KK")
                    storageReference.putFile(uri).addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener {
                            imageUrl = it
                            binding.ivKtpRegister.setImageURI(Uri.fromFile(imgFile))
                        }
                    }.addOnFailureListener{
                        Log.e("ERROR UPLOAD", "onMediaFilesPicked: " + it.message )
                    }.addOnProgressListener {
                        binding.progressKk.visibility = View.VISIBLE
                        binding.btnKkUpload.visibility = View.GONE
                    }.addOnCompleteListener{
                        binding.progressKk.visibility = View.GONE
                        binding.btnKkUpload.visibility = View.VISIBLE
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
        fun newInstance(param1: String, param2: String, param3: String, param4: String, param5 : String , param6 : String) =
            KKRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(NOMOR_KTP, param1)
                    putString(PEKERJAAN, param2)
                    putString(PENDAPATAN, param3)
                    putString(TANGGUNGAN, param4)
                    putString(IMAGEKTP, param5)
                    putString(KODE_BANSOS, param6)
                }
            }
    }
}