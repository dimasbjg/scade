package com.bangkit.scade.ui.home.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bangkit.scade.databinding.FragmentHomeBinding
import com.bangkit.scade.ui.hospital.splash.HospitalSplashActivity
import com.bangkit.scade.ui.skin_check.CheckSkinActivity
import com.bangkit.scade.ui.skin_check.splash.CheckSplashActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnImgSkin.setOnClickListener{
            val intent = Intent(activity, CheckSplashActivity::class.java)
            startActivity(intent)
        }

        binding.btnImgHospital.setOnClickListener {
            val intent = Intent(activity, HospitalSplashActivity::class.java)
            startActivity(intent)
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}