package com.bangkit.scade.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.scade.R
import com.bangkit.scade.ui.home.HomeActivity
import com.bangkit.scade.ui.login.LoginActivity
import com.bangkit.scade.viewmodel.ViewModelFactory

class MainSplashActivity : AppCompatActivity() {
    private lateinit var handler: Handler

    private lateinit var viewModel: MainSplashViewModel
    private var exist: Boolean = false
    private lateinit var token: String
    private var session: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[MainSplashViewModel::class.java]
        viewModel.dataSession.observe(this, { result ->
            token = result.tokenSection
        })
        viewModel.session.observe(this, { result ->
            Log.d("inisessionmessage:", session)
            if (result.message != null) {
                session = result.message
                Log.d("inisessionmessage:", session)
            }
        })

        viewModel.checkExist().observe(this, {
            exist = it
            Log.d("inicheckexist", exist.toString())
            if (exist) {
                viewModel.setDataSession().observe(this, { entity ->
                    token = entity.tokenSection
                    Log.d("inichecktokenac", entity.tokenSection)
                    viewModel.checkSession(token)
                    viewModel.session.observe(this, { result ->
                        session = result.message.toString()
                        Log.d("inisession", session)
                    })
                })
            }

        })




        handler = Handler(mainLooper)
        handler.postDelayed(
            {

                if (exist) { //sudah pernah login
                    //check session expired atau tidak
                    //ambil token dari database
                    if (session.equals("fetch data successfully")) { //session masih oke
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else { //session expired
                        Toast.makeText(this, getString(R.string.session_end), Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else { //belum pernah login
                    Toast.makeText(this, "No login history", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
/*
                //check jika exist berarti sudah pernah login
                if (repository.checkDataExist(1)) {
                    // tembak 1 API yg butuh auth untuk check session expired atau tidak
                    // check message
                    // if success lanjut home
                    // else langsung masuk login dengan Toast "Sesi Anda berakhir, harap login kembali"
                    val token = repository.getDataCheck()
                    var session: SessionResponse? = null
                    GlobalScope.launch {
                        session = token.value?.let { repository.checkSession(it.tokenSection) }
                    }
                    if (session?.message == "fetch data successfully") {
                        Toast.makeText(this, getString(R.string.session_end), Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }


                } else { //belum pernah login langsung masuk login activity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } */

            }, 2000
        )

    }
}