package com.octatech.bansosapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.octatech.bansosapp.R
import com.octatech.bansosapp.databinding.ActivityLoginBinding
import com.octatech.bansosapp.ui.admin.HomeAdmin
import com.octatech.bansosapp.ui.home.HomePage

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            if(binding.edtNomorKtp.text.contentEquals("admin") && binding.edtPassword.text.contentEquals("admin")){
                val intent = Intent(this, HomeAdmin::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}