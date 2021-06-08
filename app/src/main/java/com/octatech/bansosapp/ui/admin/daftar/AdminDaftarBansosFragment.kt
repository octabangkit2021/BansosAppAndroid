package com.octatech.bansosapp.ui.admin.daftar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.data.Resource
import com.octatech.bansosapp.core.ui.HomeBansosAdapter
import com.octatech.bansosapp.core.ui.ViewModelFactory
import com.octatech.bansosapp.databinding.FragmentAdminDaftarBansosBinding
import com.octatech.bansosapp.databinding.FragmentHomeBinding
import com.octatech.bansosapp.databinding.FragmentKtpRegisterBinding
import com.octatech.bansosapp.ui.detail.DetailActivity

class AdminDaftarBansosFragment : Fragment() {
    private lateinit var daftarViewModel: DaftarViewModel
    private var _binding : FragmentAdminDaftarBansosBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminDaftarBansosBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity != null){
            val factory = ViewModelFactory.getInstance(requireActivity())
            daftarViewModel = ViewModelProvider(this, factory)[DaftarViewModel::class.java]

            val homeBansosAdapter = HomeBansosAdapter()
            homeBansosAdapter.onItemClick = {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, it)
                startActivity(intent)
            }
            daftarViewModel.listBansos.observe(viewLifecycleOwner, {
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
}