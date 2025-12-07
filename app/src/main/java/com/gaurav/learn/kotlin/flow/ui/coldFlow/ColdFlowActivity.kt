package com.gaurav.learn.kotlin.flow.ui.coldFlow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gaurav.learn.kotlin.flow.R
import com.gaurav.learn.kotlin.flow.data.api.ApiHelperImpl
import com.gaurav.learn.kotlin.flow.data.api.RetrofitBuilder
import com.gaurav.learn.kotlin.flow.data.local.DatabaseBuilder
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelperImpl
import com.gaurav.learn.kotlin.flow.databinding.ActivityLongRunningTaskBinding
import com.gaurav.learn.kotlin.flow.ui.base.ViewModelFactory
import com.gaurav.learn.kotlin.flow.utils.DefaultDispatcherProvider


class ColdFlowActivity : AppCompatActivity() {

    private lateinit var viewModel: ColdFlowViewModel
    private lateinit var binding: ActivityLongRunningTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLongRunningTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTextView()
        setupViewModel()
        setupTask()
    }

    private fun setupTextView() {
        binding.progressBar.visibility = View.GONE
        binding.textView.text = getString(R.string.check_logcat)
        binding.textView.visibility = View.VISIBLE
    }

    private fun setupTask() {
        //viewModel.startColdFlowTask1()
        //viewModel.startColdFlowTask2()
        viewModel.startColdFlowTask3()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                DefaultDispatcherProvider()
            )
        )[ColdFlowViewModel::class.java]
    }

}
