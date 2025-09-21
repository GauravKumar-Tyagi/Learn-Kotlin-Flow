package com.gaurav.learn.kotlin.flow.ui.retrofit.single

import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gaurav.learn.kotlin.flow.data.api.ApiHelperImpl
import com.gaurav.learn.kotlin.flow.data.api.RetrofitBuilder
import com.gaurav.learn.kotlin.flow.data.local.DatabaseBuilder
import com.gaurav.learn.kotlin.flow.data.local.DatabaseHelperImpl
import com.gaurav.learn.kotlin.flow.ui.base.BaseActivity
import com.gaurav.learn.kotlin.flow.ui.base.UiState
import com.gaurav.learn.kotlin.flow.ui.base.ViewModelFactory
import com.gaurav.learn.kotlin.flow.utils.DefaultDispatcherProvider
import kotlinx.coroutines.launch

class SingleNetworkCallActivity : BaseActivity() {

    private lateinit var viewModel: SingleNetworkCallViewModel

    override fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            renderList(it.data)
                            binding.recyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            //Handle Error
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@SingleNetworkCallActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }


    override fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)),
                DefaultDispatcherProvider()
            )
        )[SingleNetworkCallViewModel::class.java]
    }


}