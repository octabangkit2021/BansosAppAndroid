package com.octatech.bansosapp.ui.RegisterBansos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.octatech.bansosapp.R
import com.octatech.bansosapp.core.ui.ViewModelFactory
import com.octatech.bansosapp.databinding.FragmentFormRegisterBinding

class FormRegisterFragment : Fragment() {

    private var _binding: FragmentFormRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerViewModel: RegisterViewModel;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFormRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity != null){
            val facroty = ViewModelFactory.getInstance(requireContext())
            registerViewModel = ViewModelProvider(requireActivity(), facroty)[RegisterViewModel::class.java]

            binding.btnNextRegister.setOnClickListener {
                var fragment = KtpRegisterFragment.newInstance(binding.registerEtNoktp.text.toString(), binding.registerEtPekerjaan.text.toString(),binding.registerEtPendapatanPerbulan.text.toString(),binding.registerEtTanggungan.text.toString() )
                fragmentManager?.beginTransaction()?.replace(R.id.fl_register, fragment)?.commit()
            }
        }
    }
}