package com.bangkit.scade.ui.hospital.detail_hospital

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.scade.BuildConfig
import com.bangkit.scade.R
import com.bangkit.scade.data.source.local.entity.GetDiagnosesEntity
import com.bangkit.scade.data.source.local.entity.HospitalEntity
import com.bangkit.scade.data.source.remote.request.InvoiceRequest
import com.bangkit.scade.databinding.ActivityBookingHospitalBinding
import com.bangkit.scade.ui.splash.EndSplashActivity
import com.bangkit.scade.utils.AlarmReceiver
import com.bangkit.scade.viewmodel.ViewModelFactory
import com.bangkit.scade.vo.Resource
import com.bangkit.scade.vo.Status.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class BookingHospitalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingHospitalBinding
    private lateinit var viewModel: BookingHospitalViewModel
    private lateinit var detailHospital: Resource<HospitalEntity>
    private lateinit var detailDiagnose: Resource<GetDiagnosesEntity>
    private lateinit var alarmReceiver: AlarmReceiver
    private var idDiagnose: Int = 1
    private var idHospital: Int = 1
    private var token: String = ""

    companion object {
        const val EXTRA_ID_HOSPITAL = "extra_id_hospital"
        const val EXTRA_ID_DIAGNOSE = "extra_id_diagnose"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.booking)

        binding = ActivityBookingHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            factory
        )[BookingHospitalViewModel::class.java]

        viewModel.getSession().observe(this, { tokenSession ->
            token = tokenSession.tokenSession
            val extras = intent.extras
            if (extras != null) {
                idDiagnose = extras.getInt(EXTRA_ID_DIAGNOSE)
                idHospital = extras.getInt(EXTRA_ID_HOSPITAL)

                binding.btnCreateBooking.setOnClickListener {
                    val invoiceData = InvoiceRequest(
                        hospital_id = idHospital,
                        diagnose_id = idDiagnose
                    )

                    viewModel.createInvoice(token, invoiceData)

                    viewModel.invoice.observe(this, { result ->
                        when (result.status) {
                            SUCCESS -> {
                                alarmReceiver = AlarmReceiver()
                                alarmReceiver.setOneTimeAlarm(this)
                                val intent = Intent(this, EndSplashActivity::class.java)
                                startActivity(intent)
                            }
                            LOADING -> {

                            }
                            ERROR -> {
                                Toast.makeText(
                                    this,
                                    getString(R.string.error_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })

                }

                viewModel.getDataDiagnose(token, idDiagnose)
                viewModel.getDataHospital(idHospital)

                viewModel.dataDiagnose.observe(this, { result ->
                    when (result.status) {
                        SUCCESS -> {
                            detailDiagnose = result
                            populateDataDiagnose(result)
                        }
                        LOADING -> {
                        }
                        ERROR -> {
                            Toast.makeText(
                                this,
                                "Wrong Diagnose",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                })

                viewModel.dataHospital.observe(this, { result ->
                    when (result.status) {
                        SUCCESS -> {
                            detailHospital = result
                            populateDataHospital(result)
                        }
                        LOADING -> {
                        }
                        ERROR -> {
                            Toast.makeText(
                                this,
                                "Wrong Hospital",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    }
                })
                viewModel.invoice.observe(this, { result ->
                    when (result.status) {
                        SUCCESS -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        }
                        LOADING -> {

                        }
                        ERROR -> {
                            Toast.makeText(this, "fail to book", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        })
    }

    private fun populateDataHospital(data: Resource<HospitalEntity>) {
        with(binding) {
            tvNameHospital.text = (getString(R.string.name_hospital) + " : ${data.data?.name}")
            tvPhoneHospital.text = (getString(R.string.phone) + " : ${data.data?.phone}")
            tvLocationHospital.text =
                (getString(R.string.location) +
                        " : ${data.data?.address}, ${data.data?.city}, ${data.data?.province}")
        }
    }

    private fun populateDataDiagnose(data: Resource<GetDiagnosesEntity>) {
        with(binding) {
            tvNameSpot.text = (getString(R.string.name_spot) + " : ${data.data?.position}")
            tvContentCancer.text = (getString(R.string.name_cancer) + " : ${data.data?.cancerName}")

            Glide.with(this@BookingHospitalActivity)
                .load(BuildConfig.base_url_backend + "diagnoses/image/" + data.data?.cancerImage)
                .apply(RequestOptions())
                .into(imageCancer)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}