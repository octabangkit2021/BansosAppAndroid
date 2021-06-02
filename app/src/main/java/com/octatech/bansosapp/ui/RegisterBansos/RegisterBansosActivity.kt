package com.octatech.bansosapp.ui.RegisterBansos

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.github.drjacky.imagepicker.ImagePicker
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.domain.model.Bansos
import com.octatech.bansosapp.databinding.ActivityRegisterBansosBinding
import java.net.URI

class RegisterBansosActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private lateinit var binding : ActivityRegisterBansosBinding
    private lateinit var imageUrl : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_bansos)

        binding = ActivityRegisterBansosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<Bansos>(EXTRA_DATA)
        print(data);
        Glide.with(this).load(data?.bansosGambar).into(binding.imgBanner)

        binding.btnFotoKtp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_foto_ktp -> {
                print("CLICKED")
                val intent = Intent()
                intent.type = "image/"
                intent.action = Intent.ACTION_GET_CONTENT

                startActivityForResult(intent, 100)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK){
            imageUrl = data?.data!!
            binding.ivFotoKtp.setImageURI(imageUrl)
        }
    }
}