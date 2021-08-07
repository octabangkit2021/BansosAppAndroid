package com.octatech.bansosapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.octatech.bansosapp.R
import com.octatech.bansosapp.databinding.ActivityLoginBinding
import com.octatech.bansosapp.ui.admin.HomeAdmin
import com.octatech.bansosapp.ui.home.HomePage
import com.octatech.bansosapp.ui.registerUser.RegisterActivity
import id.ionbit.ionalert.IonAlert
import io.easyprefs.Prefs

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Prefs.initializeApp(this)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            val nomor_ktp = binding.edtNomorKtp.text.toString()
            val password_login = binding.edtPassword.text.toString()
            setLoading(true)
            if(nomor_ktp.isNullOrEmpty() && password_login.isNullOrEmpty()){
                IonAlert(this, IonAlert.WARNING_TYPE)
                    .setTitleText("Ooppss..")
                    .setContentText("Silahkan isi nomor ktp dan password, lalu tekan masuk..")
                    .show()
                return@setOnClickListener
            }
            val db = FirebaseFirestore.getInstance();
            db.collection("users").document(nomor_ktp).get().addOnCompleteListener{
                if(it.isSuccessful){
                    val doc = it.getResult();
                    val password = doc.getString("password")
                    val name = doc.getString("nama")
                    val level  = doc.getString("level")
                    val nomor_ktp = doc.getString("no_ktp")
                    name?.let { it1 -> Prefs.write().content("NAMA", it1).apply() }
                    nomor_ktp?.let { it2 -> Prefs.write().content("NOMOR_KTP", it2).apply() }
                    if(password != null){
                        if (password.equals(password_login)){
                            setLoading(false)
                            if(level.equals("MASYARAKAT")){
                                val intent = Intent(this, HomePage::class.java)
                                startActivity(intent)
                            finish()
                            } else {
                                val intent = Intent(this, HomeAdmin::class.java)
                                startActivity(intent)
                            finish()
                            }
                        } else {
                            setLoading(false)
                                IonAlert(this, IonAlert.ERROR_TYPE)
                                .setTitleText("Login Gagal")
                                .setContentText("Silahkan Periksa Nomor KTP atau password anda")
                                .show()
                        }
                    }
                }
            }.addOnFailureListener {
                IonAlert(this, IonAlert.ERROR_TYPE)
                    .setTitleText("GAGAL")
                    .setContentText("Nomor KTP Tidak Ditemukan, Periksa kembali nomor KTP anda")
                    .show()
            }
        }

        binding.btnDaftar.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setLoading(b: Boolean) {
        if(b){
            binding.btnDaftar.visibility = View.GONE
            binding.btnLogin.visibility = View.GONE
            binding.lupaPassword.visibility = View.GONE
            binding.progressLogin.visibility = View.VISIBLE
        } else {
            binding.btnDaftar.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.VISIBLE
            binding.lupaPassword.visibility = View.VISIBLE
            binding.progressLogin.visibility = View.GONE
        }
    }
}