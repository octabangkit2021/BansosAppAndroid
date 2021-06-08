package com.octatech.bansosapp.ui.RegisterBansos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.domain.model.Bansos
import com.octatech.bansosapp.core.domain.model.Registrasi
import com.octatech.bansosapp.databinding.ActivityRegisterBansosBinding


class RegisterBansosActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    lateinit var registrasi : Registrasi;


    private lateinit var binding : ActivityRegisterBansosBinding
    var position : Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_bansos)

        binding = ActivityRegisterBansosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getParcelableExtra<Bansos>(EXTRA_DATA)
        print(data);
        Glide.with(this).load(data?.bansosGambar).into(binding.imgBanner)

        if(savedInstanceState == null){
            var fragment = FormRegisterFragment.newInstance(data!!.bansosId)
            supportFragmentManager.beginTransaction().replace(R.id.fl_register, fragment).commit()
        }

    }
}