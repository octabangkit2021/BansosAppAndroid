package com.octatech.bansosapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.domain.model.Bansos
import com.octatech.bansosapp.databinding.ActivityDetailBinding
import com.octatech.bansosapp.ui.RegisterBansos.RegisterBansosActivity

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }


    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailBansos = intent.getParcelableExtra<Bansos>(EXTRA_DATA)
        showDetailbansos(detailBansos)
        binding.btnAjukanBansos.setOnClickListener {
            val intent = Intent(this, RegisterBansosActivity::class.java)
            intent.putExtra(RegisterBansosActivity.EXTRA_DATA, detailBansos)
            startActivity(intent)
        }
    }

    private fun showDetailbansos(bansos: Bansos?){
        bansos?.let {
            binding.tvTitleBansos.text = it.bansosName
            binding.tvWaktuBansos.text = it.bansosBerlaku
            binding.tvKetentuanBansos.text  = it.bansosDeskripsi
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(this)
                .load(it.bansosGambar)
                .placeholder(circularProgressDrawable)
                .into(binding.imgBanner)
            binding.tvIsiBansos.text = it.bansosIsi.replace(",", "\n")
            binding.tvPersyaratanBansos.text = it.bansosPersyaratan.replace(",", "\n")
        }
    }
}