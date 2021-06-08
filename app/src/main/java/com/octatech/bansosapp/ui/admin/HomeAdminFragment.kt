package com.octatech.bansosapp.ui.admin

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.octatech.bansosapp.databinding.FragmentHomeAdminBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class HomeAdminFragment : Fragment(), EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentHomeAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeAdminBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scanMenu.setOnClickListener{
            CameraTask()
        }

        binding.registerMenu.setOnClickListener {
            fragmentManager.beginTransaction().replace()
        }
    }


    private fun hasCameraAccess() : Boolean {
        return EasyPermissions.hasPermissions(requireContext(), android.Manifest.permission.CAMERA)
    }

    private fun CameraTask(){
        if(hasCameraAccess()){
            var qrScanner = IntentIntegrator.forSupportFragment(this)
            qrScanner.setPrompt("Mulai Scan")
            qrScanner.setCameraId(0)
            qrScanner.setOrientationLocked(false)
            qrScanner.setBeepEnabled(true)
            qrScanner.captureActivity = CaptureActivity::class.java
            qrScanner.initiateScan()
        } else {
            EasyPermissions.requestPermissions(
                this, "QR Scanner butuh akses kamera, Tekan Izinkan untuk memulai",
                123,
                android.Manifest.permission.CAMERA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        if(requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            Toast.makeText(requireContext(), "Permission Diizinkan", Toast.LENGTH_LONG).show()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }
}