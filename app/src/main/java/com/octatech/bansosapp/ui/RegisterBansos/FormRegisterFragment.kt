package com.octatech.bansosapp.ui.RegisterBansos

import android.os.Bundle
import android.util.Log
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
import id.ionbit.ionalert.IonAlert

private const val KODE_BANSOS = "idBansos"
class FormRegisterFragment : Fragment() {

    private var _binding: FragmentFormRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var registerViewModel: RegisterViewModel;
    private var idBansos : String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idBansos = it.getString(KODE_BANSOS)
        }
    }

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
            binding.registerEtIdbansos.setText(idBansos)
            val facroty = ViewModelFactory.getInstance(requireContext())
            registerViewModel = ViewModelProvider(requireActivity(), facroty)[RegisterViewModel::class.java]

            binding.btnNextRegister.setOnClickListener {
                if(binding.registerEtNoktp.text.isNullOrBlank() || binding.registerEtPekerjaan.text.isNullOrBlank() || binding.registerEtPendapatanPerbulan.text.isNullOrBlank() || binding.registerEtTanggungan.text.isNullOrBlank()){
                    Toast.makeText(requireContext(), "Tidak Boleh Ada Yang Kosong", Toast.LENGTH_LONG).show()
                }else {
                    var db = FirebaseFirestore.getInstance()
                    db.collection("history_pengajuan").document(binding.registerEtNoktp.text.toString()).get().addOnSuccessListener {
                            document ->
                        if(document != null){
                            Log.d("HASIL HISTORY", "onViewCreated: " + document.getString("list_pengajuan"))
                            if(document.getString("pengajuan_active") != null){
                                IonAlert(requireContext(), IonAlert.WARNING_TYPE).setTitleText("PERHATIAN").setContentText("Pengajuan Dengan nomor KTP yang sama sudah dilakukan, mohon tunggu pengajuan sebelumnya").show()
                            } else {
                                var fragment = KtpRegisterFragment.newInstance(binding.registerEtNoktp.text.toString(), binding.registerEtPekerjaan.text.toString(),binding.registerEtPendapatanPerbulan.text.toString(),binding.registerEtTanggungan.text.toString(), idBansos!!, binding.registerEtNohp.text.toString() )
                                fragmentManager?.beginTransaction()?.replace(R.id.fl_register, fragment)?.commit()
                            }
                        } else {
                            var fragment = KtpRegisterFragment.newInstance(binding.registerEtNoktp.text.toString(), binding.registerEtPekerjaan.text.toString(),binding.registerEtPendapatanPerbulan.text.toString(),binding.registerEtTanggungan.text.toString(), idBansos!!, binding.registerEtNohp.text.toString() )
                            fragmentManager?.beginTransaction()?.replace(R.id.fl_register, fragment)?.commit()
                        }
                    }


                }
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            FormRegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(KODE_BANSOS, param1)
                }
            }
    }
}