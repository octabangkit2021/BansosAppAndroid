package com.octatech.bansosapp.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.octatech.bansosapp.R
import com.octatech.bansosapp.databinding.ActivityHomeAdminBinding
import com.octatech.bansosapp.ui.news.NewsFragment
import com.octatech.bansosapp.ui.profile.ProfileFragment
import nl.joery.animatedbottombar.AnimatedBottomBar
import org.json.JSONException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class HomeAdmin : AppCompatActivity(){
    private lateinit var binding: ActivityHomeAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.fl_home_admin, HomeAdminFragment()).commit()
        }

        binding.bottomBar.setOnTabSelectListener(object :AnimatedBottomBar.OnTabSelectListener{
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                var fragment: Fragment? =null
                when(newIndex){
                    0 ->{
                        fragment = HomeAdminFragment()
                    }
                    1 -> {
                        fragment = NewsFragment()
                    }
                    2->{
                        fragment = ProfileFragment()
                    }
                }
                if (fragment != null){
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl_home_admin, fragment)
                    transaction.commit()
                }
            }


            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }

        })

    }
}