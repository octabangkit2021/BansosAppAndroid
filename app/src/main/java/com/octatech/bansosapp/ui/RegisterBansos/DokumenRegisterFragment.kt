package com.octatech.bansosapp.ui.RegisterBansos

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.octatech.bansosapp.core.data.remote.response.ApiResponse
import com.octatech.bansosapp.core.ui.ViewModelFactory
import com.octatech.bansosapp.databinding.FragmentDokumenRegisterBinding
import com.octatech.bansosapp.ui.home.HomePage
import pl.aprilapps.easyphotopicker.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


private const val ARG_PARAM1 = "nomorKTP"
private const val NOMOR_KTP = "nomorKTP"
private const val PEKERJAAN = "pekerjaan"
private const val PENDAPATAN = "pendapatan"
private const val TANGGUNGAN = "tanggungan"
private const val KODE_BANSOS = "kodeBansos"
private const val NO_HP = "nomorHP"
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
    private var kodeBansos : String? = null;
    private var imageDokumen : String? = null;
    private var imageQRCODE : String? = null;
    private var noHP : String? = null;
    private var bitmap: Bitmap? = null
    private var qrgEncoder: QRGEncoder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nomorKTP = it.getString(NOMOR_KTP)
            pekerjaan = it.getString(PEKERJAAN)
            pendapatan = it.getString(PENDAPATAN)
            tanggungan = it.getString(TANGGUNGAN)
            imageKTP = it.getString(IMAGEKTP)
            imageKK = it.getString(IMAGEKK)
            kodeBansos = it.getString(KODE_BANSOS)
            noHP = it.getString(NO_HP)
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
            var db = FirebaseFirestore.getInstance()
            var dataArray = ArrayList<String>();
            generateQRCode(nomorKTP)
            saveMediaToStorage(bitmap!!, nomorKTP!!);
            var listPengajuan = ArrayList<String?>()
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
                listPengajuan.add(kodeBansos)
                var hasil = hashMapOf(
                    "nomor_KTP" to nomorKTP,
                    "pekerjaan" to pekerjaan,
                    "pendapatan" to pendapatan,
                    "tunjangan" to tanggungan,
                    "image_KTP" to imageKTP,
                    "image_KK" to imageKK,
                    "image_Dokumen" to imageDokumen,
                    "kode_Bansos" to kodeBansos,
                    "nomor_hp" to noHP,
                )
//                SET DAFTAR BANSOS
                db.collection("daftar_bansos").document("$nomorKTP")
                    .set(hasil)
                    .addOnSuccessListener { "Simpan Data berhasil" }
                    .addOnFailureListener { e -> "Simpan Data Gagal" }

                var updateHistory = hashMapOf(
                    "pengajuan_active" to kodeBansos,
                    kodeBansos to imageQRCODE,
                    "status" to "pengajuan"
                )
//                SET HISTORY LIST
                db.collection("history_pengajuan").document("$nomorKTP")
                    .set(updateHistory)
                    .addOnSuccessListener { "Fetch Data berhasil" }
                    .addOnFailureListener { e -> "Fetch Data Gagal" }

                binding.viewSuccess.root.visibility = View.VISIBLE
                binding.scrollDokumen.visibility = View.GONE
                binding.btnUploadRegister.visibility = View.GONE
                binding.viewSuccess.btnConfirmSuccess.setOnClickListener {
                    var intent = Intent(requireContext(), HomePage::class.java)
                    startActivity(intent)
                }
            }

        }
    }

    fun generateQRCode(ktp : String?){
        val manager = getActivity()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = manager.defaultDisplay
        val point = Point();
        display.getSize(point)
        val width = point.x
        val height = point.y
        var dimen = if (width < height) width else height

        dimen = dimen * 3/4

        qrgEncoder = QRGEncoder(ktp, null, QRGContents.Type.TEXT, dimen);

        try {
            bitmap = qrgEncoder!!.bitmap
            binding.ivBarcode.setImageBitmap(bitmap)
        } catch (e: Exception){
            Log.e("QR", "generateQRCode: " + e.message );
        }
    }

    fun saveMediaToStorage(bitmap: Bitmap, nomorKTP : String) {
        //Generating a file name
        val filename = "${nomorKTP}.PNG"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/PNG")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
                var storageReference = FirebaseStorage.getInstance().getReference(nomorKTP +"_QRCODE")
                storageReference.putFile(imageUri!!).addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener {
                        imageQRCODE = it.toString()
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
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
            var uri = Uri.fromFile(image)
            var storageReference = FirebaseStorage.getInstance().getReference(nomorKTP +"_QRCODE")
            storageReference.putFile(uri).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    imageQRCODE = it.toString()
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

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Toast.makeText(requireActivity(), "BARCODE SUDAH DISIMPAN DI GALERY", Toast.LENGTH_LONG).show()
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
                    var storageReference = FirebaseStorage.getInstance().getReference(nomorKTP +"_DP")
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
        fun newInstance(param1: String, param2: String, param3: String, param4: String, param5 : String, param6: String, param7 : String, param8 : String) =
            DokumenRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(NOMOR_KTP, param1)
                    putString(PEKERJAAN, param2)
                    putString(PENDAPATAN, param3)
                    putString(TANGGUNGAN, param4)
                    putString(IMAGEKTP, param5)
                    putString(IMAGEKK, param6)
                    putString(KODE_BANSOS, param7)
                    putString(NO_HP, param8)
                }
            }
    }
}