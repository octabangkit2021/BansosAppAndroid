package com.octatech.bansosapp.ui.RegisterBansos

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.octatech.bansosapp.core.ui.ViewModelFactory
import com.octatech.bansosapp.databinding.FragmentDokumenRegisterBinding
import com.octatech.bansosapp.ui.home.HomePage
import pl.aprilapps.easyphotopicker.*
import java.io.File


private const val ARG_PARAM1 = "nomorKTP"
private const val NOMOR_KTP = "nomorKTP"
private const val PEKERJAAN = "pekerjaan"
private const val PENDAPATAN = "pendapatan"
private const val TANGGUNGAN = "tanggungan"
private const val IMAGEKTP = "imageKTP"
private const val IMAGEKK = "imageKK"
private val savePath = Environment.getExternalStorageState() + "/QRCode/"
class DokumenRegisterFragment : Fragment() {

    private var _binding: FragmentDokumenRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var easyImage: EasyImage;
    private lateinit var registerViewModel: RegisterViewModel
    private var nomorKTP : String? = null;
    private var pekerjaan : String? = null;
    private var pendapatan : String? = null;
    private var tanggungan : String? = null;
    private var imageKTP : String? = null;
    private var imageKK : String? = null;
    private var imageDokumen : String? = null;
    private var bitmap: Bitmap? = null
    private val qrgEncoder: QRGEncoder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nomorKTP = it.getString(NOMOR_KTP)
            pekerjaan = it.getString(PEKERJAAN)
            pendapatan = it.getString(PENDAPATAN)
            tanggungan = it.getString(TANGGUNGAN)
            imageKTP = it.getString(IMAGEKTP)
            imageKK = it.getString(IMAGEKK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDokumenRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(view != null){
            binding.btnDocumenRegister.setOnClickListener {
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
            binding.btnUploadRegister.setOnClickListener {
                var hasil = hashMapOf(
                    "nomorKTP" to nomorKTP,
                    "pekerjaan" to pekerjaan,
                    "pendapatan" to pendapatan,
                    "tunjangan" to tanggungan,
                    "imagektp" to imageKTP,
                    "imageKK" to imageKK,
                    "imageDokumen" to imageDokumen
                )
                var db = FirebaseFirestore.getInstance()
                db.collection("daftar_bansos").document("$nomorKTP")
                    .set(hasil)
                    .addOnSuccessListener { "Fetch Data berhasil" }
                    .addOnFailureListener { e -> "Fetch Data Gagal" }
                val qrgEncoder =
                    QRGEncoder(nomorKTP, null, QRGContents.Type.TEXT, 200)
                bitmap = qrgEncoder.getBitmap();
                val save = QRGSaver().save(
                    savePath,
                    nomorKTP,
                    bitmap,
                    QRGContents.ImageType.IMAGE_PNG
                )
                val result = if (save) "Image Saved" else "Image Not Saved"
                Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                var intent = Intent(requireContext(), HomePage::class.java)
                startActivity(intent)

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
                    var storageReference = FirebaseStorage.getInstance().getReference(nomorKTP +"DP")
                    storageReference.putFile(uri).addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener {
                            imageDokumen = it.toString()
                            binding.ivKtpRegister.setImageURI(Uri.fromFile(imgFile))
                        }
                    }.addOnFailureListener{
                        Log.e("ERROR UPLOAD", "onMediaFilesPicked: " + it.message )
                    }.addOnProgressListener {
                        binding.progressDokumen.visibility = View.VISIBLE
                        binding.btnDocumenRegister.visibility = View.GONE
                    }.addOnCompleteListener{
                        binding.progressDokumen.visibility = View.GONE
                        binding.btnDocumenRegister.visibility = View.VISIBLE
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
        fun newInstance(param1: String, param2: String, param3: String, param4: String, param5 : String, param6: String) =
            DokumenRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(NOMOR_KTP, param1)
                    putString(PEKERJAAN, param2)
                    putString(PENDAPATAN, param3)
                    putString(TANGGUNGAN, param4)
                    putString(IMAGEKTP, param5)
                    putString(IMAGEKK, param6)
                }
            }
    }
}