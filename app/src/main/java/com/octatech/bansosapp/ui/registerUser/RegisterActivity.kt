package com.octatech.bansosapp.ui.registerUser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.octatech.bansosapp.R
import com.octatech.bansosapp.databinding.ActivityRegisterBinding
import com.octatech.bansosapp.ui.login.LoginActivity
import id.ionbit.ionalert.IonAlert


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnDaftarUser.setOnClickListener {
            onLoad(true)
            val nomor_ktp = binding.edtNomorKtp.text
            val nama_lengkap = binding.edtNamaLengkap.text
            val nomor_telepon = binding.edtNomorPhone.text
            val email = binding.edtEmail.text
            val tanggal_lahir = binding.edtTanggalLahir.text
            val password = binding.edtPassword.text
            val repassword = binding.edtRepassword.text

            val db = FirebaseFirestore.getInstance();
            if(!validateNomorKTP() && !validateEmail() && !validatePassword()){
                return@setOnClickListener
            } else {
                var hasil = hashMapOf(
                    "email" to email.toString(),
                    "nama" to nama_lengkap.toString(),
                    "no_ktp" to nomor_ktp.toString(),
                    "no_telepon" to nomor_telepon.toString(),
                    "password" to password.toString(),
                    "tanggal_lahir" to tanggal_lahir.toString(),
                    "level" to "MASYARAKAT"
                )
                db.collection("users").document(nomor_ktp.toString())
                    .set(hasil)
                    .addOnSuccessListener {
                        IonAlert(this, IonAlert.SUCCESS_TYPE)
                            .setTitleText("Berhasil")
                            .setContentText("Pendaftaran berhasil")
                            .setConfirmClickListener {
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                                it.dismissWithAnimation()
                            }
                            .show()
                        onLoad(false)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Daftar user Gagal, Silahkan periksa koneksi anda", Toast.LENGTH_SHORT).show()
                        IonAlert(this, IonAlert.ERROR_TYPE)
                            .setTitleText("Gagal")
                            .setContentText("Pendaftaran gagal, Mohon periksa koneksi anda")
                            .show()
                        onLoad(false)
                    }
            }
        }
    }

    private fun onLoad(boolean: Boolean) {
        if(boolean){
            binding.progressDaftarUser.visibility = View.VISIBLE
            binding.btnDaftarUser.visibility = View.GONE
        } else {
            binding.progressDaftarUser.visibility = View.GONE
            binding.btnDaftarUser.visibility = View.VISIBLE
        }
    }

    private fun validateNomorKTP() : Boolean {
       val nomor_ktp = binding.edtNomorKtp.text
       return nomor_ktp?.length ?: 0 == 16
   }

    private fun validateEmail() : Boolean{
        val email = binding.edtEmail.text
        return email?.contains("@") == true
    }

    private fun validatePassword() : Boolean{
        val password = binding.edtPassword.text
        val repassword = binding.edtRepassword.text

        return password?.equals(repassword) == true
    }
}