package com.octatech.bansosapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.data.Resource
import com.octatech.bansosapp.core.ui.HomeBansosAdapter
import com.octatech.bansosapp.core.ui.ViewModelFactory
import com.octatech.bansosapp.databinding.ActivityHomePageBinding
import com.octatech.bansosapp.ui.detail.DetailActivity


class HomePage : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding : ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        val homeBansosAdapter = HomeBansosAdapter()
        homeBansosAdapter.onItemClick = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, it)
            startActivity(intent)
        }
        homeViewModel.listBansos.observe(this, {
                if(it != null){
                    when(it){
                        is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is Resource.Success -> {
                                binding.progressBar.visibility = View.GONE
                                homeBansosAdapter.setData(it.data)
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.viewError.root.visibility = View.VISIBLE
                            binding.viewError.descError.text = it.message ?: getString(R.string.error_message)
                        }
                    }
                }
        })
        with(binding.rvListBansos) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = homeBansosAdapter
        }
    }
}