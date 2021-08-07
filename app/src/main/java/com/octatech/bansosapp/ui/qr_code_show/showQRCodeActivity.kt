package com.octatech.bansosapp.ui.qr_code_show

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.octatech.bansosapp.R
import com.octatech.bansosapp.databinding.ActivityRegisterBinding
import com.octatech.bansosapp.databinding.ActivityShowQrcodeBinding
import com.octatech.bansosapp.ui.home.HomePage

class showQRCodeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShowQrcodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_qrcode)

        binding = ActivityShowQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var qr_code = intent.getStringExtra(QR_CODE)
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(this)
            .load(qr_code)
            .placeholder(circularProgressDrawable)
            .into(binding.imgQrDetail)

        binding.btnBack.setOnClickListener {
            var intent = Intent(this, HomePage::class.java);
            startActivity(intent)
        }

        binding.ibBack.setOnClickListener{
            var intent = Intent(this, HomePage::class.java);
            startActivity(intent)
        }
    }

    companion object{
        var QR_CODE = "QR_CODE"
    }
}