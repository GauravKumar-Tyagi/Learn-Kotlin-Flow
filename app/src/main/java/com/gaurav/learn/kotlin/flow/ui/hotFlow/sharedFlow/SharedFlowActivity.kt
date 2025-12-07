package com.gaurav.learn.kotlin.flow.ui.hotFlow.sharedFlow

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gaurav.learn.kotlin.flow.R
import com.gaurav.learn.kotlin.flow.data.api.ApiHelperImpl
import com.gaurav.learn.kotlin.flow.data.api.RetrofitBuilder
import com.gaurav.learn.kotlin.flow.data.local.DatabaseBuilder
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelperImpl
import com.gaurav.learn.kotlin.flow.databinding.ActivityLongRunningTaskBinding
import com.gaurav.learn.kotlin.flow.ui.base.ViewModelFactory
import com.gaurav.learn.kotlin.flow.utils.DefaultDispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SharedFlowActivity : AppCompatActivity() {

    private lateinit var viewModel: SharedFlowViewModel
    private lateinit var binding: ActivityLongRunningTaskBinding
    private val TAG = "~~~~~SharedFlowActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLongRunningTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTextView()
        setupViewModel()
        setupObserver()
        setupTask()
    }

    private fun setupTextView() {
        binding.progressBar.visibility = View.GONE
        binding.textView.text = getString(R.string.check_logcat)
        binding.textView.visibility = View.VISIBLE
    }

    private fun setupTask() {
        viewModel.executeTask()
    }

    /**
     * It is recommended to use repeatOnLifecycle for both StateFlow and SharedFlow in
     * UI components (like Fragments/Activities) to ensure collection only happens when
     * the lifecycle is at least in a certain state (e.g., STARTED).
     * This prevents memory leaks and unnecessary work when the UI is not visible.
     */
    private fun setupObserver() {

        lifecycleScope.launch {

            /*
            If you do not use delay(5000) below here then you will get below in the logs
                1. Collected Value is :: SomeLongRunningTaskStarted
                1. Collected Value is :: SomeLongRunningTaskStarted
                Updated Value is :: Downloading File 1
                1. Collected Value is :: Downloading File 1
                Updated Value is :: Downloading File 2
                1. Collected Value is :: Downloading File 2
                2. Collected Value is :: Downloading File 2
                and so on...
            If you use delay(5000) below here then you will get below in the logs
                Updated Value is :: Downloading File 1
                Updated Value is :: Downloading File 2
                1. Collected Value is :: Downloading File 2
                2. Collected Value is :: Downloading File 2
                Updated Value is :: Downloading File 3
                and so on...
           */

            //delay(5000)

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect {
                    Log.d(TAG, "1. Collected Value is :: $it")
                }
            }

        }

        lifecycleScope.launch {
            delay(5000)
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect {
                    Log.d(TAG, "2. Collected Value is :: $it")
                }
            }
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                DefaultDispatcherProvider()
            )
        )[SharedFlowViewModel::class.java]
    }

}
