package com.octatech.bansosapp.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.octatech.bansosapp.R
import com.octatech.bansosapp.databinding.ActivityHomePageBinding
import com.octatech.bansosapp.ui.news.NewsFragment
import com.octatech.bansosapp.ui.profile.ProfileFragment
import nl.joery.animatedbottombar.AnimatedBottomBar


class HomePage : AppCompatActivity() {

    private lateinit var binding : ActivityHomePageBinding
    private var mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        mAuth.signInAnonymously().addOnCompleteListener{
            if(it.isComplete){
                val user = mAuth.currentUser
                Log.d("LOGIN", "Login As " + user)
            } else {
                Log.w("LOGIN", "signInWithCustomToken:failure", it.getException());
                Toast.makeText(this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(R.id.fl_home, HomeFragment()).commit()
        }

        binding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                var fragment: Fragment? = null
                when(newIndex){
                    0 -> {
                        fragment = HomeFragment()
                    }
                    1 -> {
                        fragment = NewsFragment()
                    }
                    2 -> {
                        fragment = ProfileFragment()
                    }
                }
                if(fragment != null){
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fl_home, fragment)
                    transaction.commit()
                }
            }

            // An optional method that will be fired whenever an already selected tab has been selected again.
            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        })

    }
}