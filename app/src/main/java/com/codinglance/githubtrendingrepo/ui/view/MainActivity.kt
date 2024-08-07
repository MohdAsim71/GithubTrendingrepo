package com.codinglance.githubtrendingrepo.ui.view

import android.app.ProgressDialog
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinglance.githubtrendingrepo.BaseApplication
import com.codinglance.githubtrendingrepo.R
import com.codinglance.githubtrendingrepo.adapter.RepoAdapter
import com.codinglance.githubtrendingrepo.databinding.ActivityMainBinding
import com.codinglance.githubtrendingrepo.model.Item
import com.codinglance.githubtrendingrepo.repository.ResultState
import com.codinglance.githubtrendingrepo.ui.viewModel.RepoViewModel
import com.facebook.battery.metrics.composite.CompositeMetrics
import com.facebook.battery.metrics.composite.CompositeMetricsCollector
import com.facebook.battery.metrics.cpu.CpuMetrics
import com.facebook.battery.metrics.cpu.CpuMetricsCollector
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    val sCollector: CpuMetricsCollector = CpuMetricsCollector()
    private val mInitialMetrics: CpuMetrics = sCollector.createMetrics()
    private val mFinalMetrics: CpuMetrics = sCollector.createMetrics()
    private lateinit var  binding: ActivityMainBinding
    private val repoViewModel: RepoViewModel by viewModels()
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var repoAdapter: RepoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = layoutInflater.inflate(R.layout.activity_main, null, true)
        binding = DataBindingUtil.bind(rootView)!!
        setContentView(rootView)
        supportActionBar?.hide()
        binding.viewModel= repoViewModel
        initViewModel()
        binding.searchEt.clearFocus()

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

    
    private fun initViewModel() {
        repoViewModel.repoState.observe(this){
            when(it){
                ResultState.Loading -> {
                    sCollector.getSnapshot(mInitialMetrics);
                    Log.d("InitialBatteryMetrics", mInitialMetrics.toString());
                    displayProgressAnimation()
                }
                is ResultState.Success ->{
                    hideProgressAnimation()
                    binding.repoRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    repoAdapter = RepoAdapter(this,repoViewModel.repolist)
                    binding.repoRecyclerview.adapter = repoAdapter
                    sCollector.getSnapshot(mFinalMetrics);
                    Log.d("FinalBatteryMetrics", mFinalMetrics.toString());
                    getBatteryMetrics(mInitialMetrics,mFinalMetrics,"MainActivity")
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

    private fun getBatteryMetrics(cpuInitialMetrics:CpuMetrics ,cpuFinalMetrics:CpuMetrics,s:String) {
        Log.d("$s+Metrics", cpuFinalMetrics.diff(cpuInitialMetrics).toString());

    }
}