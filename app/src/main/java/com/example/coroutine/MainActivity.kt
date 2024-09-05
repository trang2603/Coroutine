package com.example.coroutine

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coroutine.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.logging.Logger
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var result: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.edt.setOnClickListener {
            requestDataWithSuspend()
        }

        binding.btnStart.setOnClickListener {
            if (!result) {
                val scope = CoroutineScope(Dispatchers.IO)
                scope.launch {
                    val jobs = mutableListOf<Deferred<Boolean>>()
                    val timeStart = System.currentTimeMillis()
                    repeat(4) {
                        val job = scope.async {
                            val delayTime = Random.nextLong(1000, 7000)
                            Log.e("job", "Job start $it done after $delayTime")
                            Thread.sleep(delayTime)
                            Log.e("job", "Job running$it done after ${delayTime / 1000}s")
                            true
                        }
                        jobs.add(job)
                    }
                    jobs.awaitAll()
                    runOnUiThread {
                        binding.icUncheck1.setImageResource(R.drawable.ic_checked)
                        binding.icUncheck2.setImageResource(R.drawable.ic_checked)
                        binding.icUncheck3.setImageResource(R.drawable.ic_checked)
                        binding.icUncheck4.setImageResource(R.drawable.ic_checked)
                    }
                    Log.e("job", "${(System.currentTimeMillis() - timeStart) / 1000}s")
                    runOnUiThread {
                        binding.btnStart.setText("Done async awaitAll")
                    }
                }
            }
        }

        binding.btnStartWithContext.setOnClickListener {
            if (!result) {
                val scope = CoroutineScope(Dispatchers.IO)
                val timeStart = System.currentTimeMillis()
                scope.launch {
                    repeat(10) {
                        val result = withContext(context = Dispatchers.IO, block = {
                            val delayTime = Random.nextLong(1000, 3000)
                            Log.e("job withContext", "Job start $it done after $delayTime")
                            Thread.sleep(delayTime)
                            Log.e(
                                "job withContext",
                                "Job running$it done after ${delayTime / 1000}s"
                            )
                            //return result
                            delayTime
                        })
                    }
                    Log.e("job withContext", "${(System.currentTimeMillis() - timeStart) / 1000}s")
                    runOnUiThread {
                        binding.btnStartWithContext.setText("Done withContetx")
                    }
                }
            }
        }

        /*binding.btnLaunch.setOnClickListener {
            if (!result) {
                val scope = CoroutineScope(Dispatchers.IO)
                val timeStart = System.currentTimeMillis()
                repeat(10) {
                    startTask(scope, onResult = {

                    })
                    runOnUiThread {
                        binding.btnLaunch.setText("Done Launch")
                    }
                }
            }
        }*/

        binding.btnAsyncAwait.setOnClickListener {
            if (!result) {
                val scope = CoroutineScope(Dispatchers.IO)
                val timeStart = System.currentTimeMillis()
                scope.launch {
                    val jobs = mutableListOf<Deferred<Boolean>>()
                    repeat(10) {
                        val job = scope.async {
                            val delayTime = Random.nextLong(1000, 7000)
                            Log.e("job async await", "Job start $it done after $delayTime")
                            Thread.sleep(delayTime)
                            Log.e(
                                "job async await",
                                "Job running$it done after ${delayTime / 1000}s"
                            )
                            true
                        }
                        jobs.add(job)
                    }
                    jobs.forEach { it.await() }
                    Log.e("job async await", "${(System.currentTimeMillis() - timeStart) / 1000}s")
                    runOnUiThread {
                        binding.btnAsyncAwait.setText("Done Async Await")
                    }
                }
            }
        }
    }

    private fun requestDataWithSuspend() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            Log.e("running", "${Thread.currentThread().name}")
            delay(1000L)
            result = true
        }

    }

    /*private fun startTask(scope: CoroutineScope, onResult: (Long) -> Unit) {
        scope.launch {
//                    val jobs = mutableListOf<Job>()
//                        val job = launch {
            val delayTime = Random.nextLong(1000, 7000)
            Log.e("job launch", "Job start $it done after $delayTime")
            Thread.sleep(delayTime)
            Log.e("job launch", "Job running$it done after ${delayTime / 1000}s")
            onResult.invoke(delayTime)
//                        }
//                        jobs.add(job)
//                    }
//                    jobs.forEach {it.join()}
//                    Log.e("job launch", "${(System.currentTimeMillis() - timeStart) / 1000}s")
        }
    }*/
}