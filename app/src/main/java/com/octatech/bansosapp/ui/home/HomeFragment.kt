package com.octatech.bansosapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Preconditions
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.data.Resource
import com.octatech.bansosapp.core.data.remote.response.ApiResponse
import com.octatech.bansosapp.core.data.remote.response.BansosTerdaftarResponse
import com.octatech.bansosapp.core.ui.HomeBansosAdapter
import com.octatech.bansosapp.core.ui.ViewModelFactory
import com.octatech.bansosapp.databinding.FragmentHomeBinding
import com.octatech.bansosapp.ui.RegisterBansos.*
import com.octatech.bansosapp.ui.detail.DetailActivity
import com.octatech.bansosapp.ui.qr_code_show.showQRCodeActivity
import io.easyprefs.Prefs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var nama : String? = null;
    private var qr_code : String? = null;
    private var nomor_ktp : String? = null;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c: Calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate: String = df.format(c.time)
        binding.tvDateHeader.setText(formatTanggal(formattedDate))
        nama = Prefs.read().content("NAMA", "OCTAVIAN")
        nomor_ktp = Prefs.read().content("NOMOR_KTP", "")
        if(activity != null){
            val factory = ViewModelFactory.getInstance(requireActivity())
            homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

            val homeBansosAdapter = HomeBansosAdapter()
            homeBansosAdapter.onItemClick = {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, it)
                startActivity(intent)
            }
            binding.btnLihatQrTerdaftar.setOnClickListener {
               val intent = Intent(requireContext(), showQRCodeActivity::class.java);
                intent.putExtra(showQRCodeActivity.QR_CODE, qr_code);
                startActivity(intent)
            }
            binding.tvHeaderUsername.setText(nama.toString().uppercase())
            homeViewModel.listBansos.observe(viewLifecycleOwner, {
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

            var db = FirebaseFirestore.getInstance();

            db.collection("history_pengajuan").document(nomor_ktp!!).get().addOnSuccessListener {
                if(it.getString("pengajuan_active").isNullOrEmpty()){
                    val circularProgressDrawable = CircularProgressDrawable(requireContext())
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()
                    binding.tvStatusTerdaftar.setText("Belum ada pengajuan..")
                    binding.btnLihatQrTerdaftar.visibility = View.GONE
                } else {
                    if(it.getString("status").equals("Sedang Diajuka")){
                        val circularProgressDrawable = CircularProgressDrawable(requireContext())
                        circularProgressDrawable.strokeWidth = 5f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()
                        binding.tvStatusTerdaftar.setText(it.getString("status"))
                        Glide.with(this)
                            .load(it.getString("qr_code"))
                            .placeholder(circularProgressDrawable)
                            .into(binding.imgQrTerdaftar)
                        qr_code = it.getString("qr_code");
                    } else {
                        val circularProgressDrawable = CircularProgressDrawable(requireContext())
                        circularProgressDrawable.strokeWidth = 5f
                        circularProgressDrawable.centerRadius = 30f
                        circularProgressDrawable.start()
                        binding.tvStatusTerdaftar.setText("Belum ada pengajuan..")
                        binding.btnLihatQrTerdaftar.visibility = View.GONE
                    }
                }
            }
            with(binding.rvListBansos) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = homeBansosAdapter
            }
        }
    }

    private fun formatTanggal(data : String) : String{
        var tanggal = data.split("-")[2]
        var bulan = data.split("-")[1]
        var tahun = data.split("-")[0]
        Log.e("DEBUG BULAN", "formatTanggal: " + bulan)
        var bulan_hasil = when(bulan.toString()){
                "01" -> "Januari"
                "02" -> "Februari"
            "03" -> "Maret"
            "04" -> "April"
            "05" -> "Mei"
            "06" -> "Juni"
            "07" -> "Juli"
            "08" -> "Agustus"
            "09"-> "September"
            "10" -> "Oktober"
            "11"->"November"
            "12" -> "Desember"
            else -> false
        }
        return "$tanggal-$bulan_hasil-$tahun"
    }
}