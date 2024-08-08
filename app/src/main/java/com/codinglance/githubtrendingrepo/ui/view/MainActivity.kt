package com.codinglance.githubtrendingrepo.ui.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinglance.githubtrendingrepo.R
import com.codinglance.githubtrendingrepo.adapter.RepoAdapter
import com.codinglance.githubtrendingrepo.databinding.ActivityMainBinding
import com.codinglance.githubtrendingrepo.model.Item
import com.codinglance.githubtrendingrepo.repository.ResultState
import com.codinglance.githubtrendingrepo.ui.viewModel.RepoViewModel
import com.facebook.battery.metrics.cpu.CpuFrequencyMetrics
import com.facebook.battery.metrics.cpu.CpuFrequencyMetricsCollector
import com.facebook.battery.metrics.cpu.CpuMetrics
import com.facebook.battery.metrics.cpu.CpuMetricsCollector
import com.facebook.battery.metrics.devicebattery.DeviceBatteryMetrics
import com.facebook.battery.metrics.devicebattery.DeviceBatteryMetricsCollector
import com.facebook.battery.metrics.healthstats.HealthStatsMetrics
import com.facebook.battery.metrics.healthstats.HealthStatsMetricsCollector
import com.facebook.battery.metrics.network.NetworkMetrics
import com.facebook.battery.metrics.network.NetworkMetricsCollector
import com.facebook.battery.metrics.time.TimeMetrics
import com.facebook.battery.metrics.time.TimeMetricsCollector
import com.facebook.battery.reporter.devicebattery.DeviceBatteryMetricsReporter

import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    val sCollector: CpuMetricsCollector = CpuMetricsCollector()
    private val mInitialMetrics: CpuMetrics = sCollector.createMetrics()
    private val mFinalMetrics: CpuMetrics = sCollector.createMetrics()

    private val timeCollector: TimeMetricsCollector = TimeMetricsCollector()
    private val timeInitialMetrics: TimeMetrics = timeCollector.createMetrics()
    private val timeFinalMetrics: TimeMetrics = timeCollector.createMetrics()

    private val cpuFrequencyCollector: CpuFrequencyMetricsCollector = CpuFrequencyMetricsCollector()
    private val cpuFrequencyInitialMetrics: CpuFrequencyMetrics = cpuFrequencyCollector.createMetrics()
    private val cpuFrequencyFinalMetrics: CpuFrequencyMetrics = cpuFrequencyCollector.createMetrics()

    @RequiresApi(Build.VERSION_CODES.N)
    lateinit var healthCollector: HealthStatsMetricsCollector
    @RequiresApi(Build.VERSION_CODES.N)
    lateinit var  healthInitialMetrics: HealthStatsMetrics
    @RequiresApi(Build.VERSION_CODES.N)
    lateinit var healthFinalMetrics: HealthStatsMetrics

    lateinit var networkCollector: NetworkMetricsCollector
    lateinit var networkInitialMetrics: NetworkMetrics
    lateinit var networkFinalMetrics: NetworkMetrics

    lateinit var deviceBatteryCollector: DeviceBatteryMetricsCollector
    private lateinit var batteryMetricsReporter: DeviceBatteryMetricsReporter

    lateinit var mInitialDeviceMetrics: DeviceBatteryMetrics
    lateinit var mFinalDeviceMetrics: DeviceBatteryMetrics

    private lateinit var  binding: ActivityMainBinding
    private val repoViewModel: RepoViewModel by viewModels()
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var repoAdapter: RepoAdapter
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = layoutInflater.inflate(R.layout.activity_main, null, true)
        binding = DataBindingUtil.bind(rootView)!!
        setContentView(rootView)
        supportActionBar?.hide()
        binding.viewModel= repoViewModel
        initViewModel()
        binding.searchEt.clearFocus()
        healthCollector = HealthStatsMetricsCollector(this)
        healthInitialMetrics =healthCollector.createMetrics()
        healthFinalMetrics =healthCollector.createMetrics()

        networkCollector = NetworkMetricsCollector(this)
        networkInitialMetrics = networkCollector.createMetrics()
        networkFinalMetrics = networkCollector.createMetrics()

        deviceBatteryCollector = DeviceBatteryMetricsCollector(this)
        batteryMetricsReporter = DeviceBatteryMetricsReporter()

        mInitialDeviceMetrics = deviceBatteryCollector.createMetrics()
        mFinalDeviceMetrics = deviceBatteryCollector.createMetrics()



        binding.searchEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                filters(s.toString())

            }

            override fun afterTextChanged(s: Editable?) {
                // Do Nothing

            }

        })

    }

    
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    private fun initViewModel() {
        repoViewModel.repoState.observe(this){
            when(it){
                ResultState.Loading -> {
                    sCollector.getSnapshot(mInitialMetrics);
                    //Log.d("InitialBatteryMetrics", mInitialMetrics.toString());
                    //Log.d("Initial C.P.U. Metrics", mInitialMetrics.toString());
                    //Log.d("Initial Network Metrics", networkInitialMetrics.toString());
                    //Log.d("Initial Time Metrics", timeInitialMetrics.toString());
                    //Log.d("Initial CpuFrequency Metrics", cpuFrequencyInitialMetrics.toString());
                   // Log.d("Initial Health Metrics", healthInitialMetrics.toString());
                    Log.d("Initial Battery Metrics", mInitialDeviceMetrics.toString());
                    Log.d("Initial Battery Metrics", batteryMetricsReporter.toString());
                    displayProgressAnimation()
                }
                is ResultState.Success ->{
                    hideProgressAnimation()
                    binding.repoRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    repoAdapter = RepoAdapter(this,repoViewModel.repolist)
                    binding.repoRecyclerview.adapter = repoAdapter
                    sCollector.getSnapshot(mFinalMetrics);
//                    Log.d("Final C.P.U. Metrics", mFinalMetrics.toString());
//                    Log.d("Final Network Metrics", networkFinalMetrics.toString());
//                    Log.d("Final Time Metrics", timeFinalMetrics.toString());
//                    Log.d("Final CpuFrequency Metrics", cpuFrequencyFinalMetrics.toString());
                  //  Log.d("Final Batter Metrics", mFinalDeviceMetrics.toString());

//                    Log.d("C.P.U. Metrics", mFinalMetrics.diff(mInitialMetrics).toString());
//                    Log.d("Network Metrics", networkFinalMetrics.diff(networkInitialMetrics).toString());
//                    Log.d("Time Metrics", timeFinalMetrics.diff(timeInitialMetrics).toString());
//                    Log.d("C.P.U Frequency Metrics", cpuFrequencyFinalMetrics.diff(cpuFrequencyInitialMetrics).toString());
//                    Log.d("Health Metrics", healthFinalMetrics.diff(healthInitialMetrics).toString());
                   // Log.d("Battery Metrics", mFinalDeviceMetrics.diff(mInitialDeviceMetrics).toString());
                }
                is ResultState.Error ->{
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

                }

            }
        }
    }
    private fun filters(text: String) {
        val filterdRepo: ArrayList<Item> = ArrayList()

        if (text.isNotEmpty()) {
            filterdRepo.addAll((repoViewModel).repolist.filter {
                it.name.lowercase().contains(text.lowercase()) || it.name.lowercase().contains(text.lowercase())
            }.toList())
        }
        else{
            filterdRepo.addAll((repoViewModel).repolist)
        }
        repoAdapter.filterList(filterdRepo)
    }

    private fun displayProgressAnimation() {

        // only create one
        if(!this::mProgressDialog.isInitialized) {
            // start progress Dialog animation:
            mProgressDialog = ProgressDialog.show(
                this,
                null,
                "LOADING...",
                false
            )
        } else {
            mProgressDialog.show()
        }
    }


    private fun hideProgressAnimation() {
        if(this::mProgressDialog.isInitialized) {
            mProgressDialog.dismiss()

        }
    }


}