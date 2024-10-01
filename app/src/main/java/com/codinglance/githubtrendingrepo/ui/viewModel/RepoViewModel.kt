package com.codinglance.githubtrendingrepo.ui.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.BatteryManager
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codinglance.githubtrendingrepo.model.DBData
import com.codinglance.githubtrendingrepo.model.Item
import com.codinglance.githubtrendingrepo.model.RepoResponse
import com.codinglance.githubtrendingrepo.repository.Repository
import com.codinglance.githubtrendingrepo.repository.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application)
{
    var repolist: ArrayList<Item> = ArrayList()
    var mSubscriber= MutableLiveData(false)
    private val handler = Handler()
    private lateinit var runnable: Runnable
    private var startTime: Long = 0
    private val durationMillis = TimeUnit.MINUTES.toMillis(5)

    init {
   // startPeriodicApiCalls()

//        // Perform the sequence of operations
//        viewModelScope.launch {
//            // Fetch initial battery information
//            getBatteryInfo(application)
//
//            // Start the intensive background computation
//            startHeavyComputation()
//
//            // Delay for 5 minutes
//            delay(5 * 60 * 1000) // 5 minutes in milliseconds
//
//            // Fetch battery information again after 5 minutes
//            getBatteryInfo(application)
//        }
    }



    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val _repoState: MutableLiveData<ResultState<RepoResponse>> = MutableLiveData()
    val repoState: LiveData<ResultState<RepoResponse>> = _repoState


    private fun startPeriodicApiCalls() {
        startTime = System.currentTimeMillis()

        // Define the Runnable task
        runnable = object : Runnable {
            override fun run() {
                viewModelScope.launch {
                    // Call your API here
                    getRepo()
                }
                // Check if 5 minutes have passed
                if (System.currentTimeMillis() - startTime < durationMillis) {
                    Log.e("viewmodel", "run: task is running ")
                    // Schedule the next call in 30 seconds (adjust as needed)
                    handler.postDelayed(this, TimeUnit.SECONDS.toMillis(30))
                }
            }
        }

        // Start the periodic task
        handler.post(runnable)
    }

//    fun getRepo() {
//        viewModelScope.launch(Dispatchers.IO)
//        {
//            _repoState.postValue(ResultState.Loading)
//
//
//            try {
//
//                val response=repository.getRepo()
//
//                if (response.items.isNotEmpty()) {
//                    _repoState.postValue(ResultState.Success(response))
//                    repolist= response.items as ArrayList<Item>
//                    mSubscriber.postValue(true)
//                    // Map the items to ItemEntity and insert into Room
//                    val itemEntities = repolist.map { mapToItemEntity(it) }
//                    repository.insert(itemEntities)
//                }
//            }
//
//            catch (e:Exception){
//                _repoState.postValue(ResultState.Error(e.localizedMessage.toString()))
//
//            }
//
//        }
//    }

//remove try catch to get room migration crash
    fun getRepo() {
        viewModelScope.launch(Dispatchers.IO) {
            _repoState.postValue(ResultState.Loading)

            val response = repository.getRepo()

            if (response.items.isNotEmpty()) {
                _repoState.postValue(ResultState.Success(response))
                repolist = response.items as ArrayList<Item>
                mSubscriber.postValue(true)

                // Map the items to ItemEntity and insert into Room
                val itemEntities = repolist.map { mapToItemEntity(it) }
                repository.insert(itemEntities)
            }
        }
    }


    fun startHeavyComputation() {
        viewModelScope.launch {
            val endTime = System.currentTimeMillis() + 5 * 60 * 1000 // 5 minutes from now
            while (System.currentTimeMillis() < endTime) {
                performHeavyComputation()
                // Optionally add a delay if you want to control the frequency of the computation
                // delay(1000) // Adjust delay to control frequency if needed
            }
        }
    }
    fun mapToItemEntity(item: Item): DBData {
        return DBData(
            name = item.name,
            owner = item.owner.toString(),
            description = item.description
        )
    }

    fun multiplicationOfTwoNumber(num1: Int, num2: Int): Int {
        return num1 * num2
    }
    private suspend fun performHeavyComputation() {
        withContext(Dispatchers.Default) {
            // Simulate continuous heavy computation
            while (true) {
                val timeTaken = measureTimeMillis {
                    val result = generatePrimes(100000)
                    println("Computation result size: ${result.size}")
                }
                println("Computation time: $timeTaken ms")

                // Break the loop if the total time exceeds 5 minutes
                val endTime = System.currentTimeMillis() + 5 * 60 * 1000
                if (System.currentTimeMillis() > endTime) break
            }
        }
    }

     fun generatePrimes(n: Int): List<Int> {
        val primes = mutableListOf<Int>()
        for (i in 2..n) {
            var isPrime = true
            for (j in 2 until i) {
                if (i % j == 0) {
                    isPrime = false
                    break
                }
            }
            if (isPrime) primes.add(i)
        }
        return primes
    }
    fun getBatteryInfo(context: Context) {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val batteryStatus = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
        val batteryVoltage = batteryManager.getIntProperty(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE)

        Log.e("Battery", "Battery Level: $batteryLevel%")
        Log.e("Battery","Battery Status: $batteryStatus")
        Log.e("Battery","Battery Voltage: ${batteryVoltage / 1000.0}V")
    }
}