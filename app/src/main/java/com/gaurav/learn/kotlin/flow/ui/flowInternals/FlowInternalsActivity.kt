package com.gaurav.learn.kotlin.flow.ui.flowInternals

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
import com.gaurav.learn.kotlin.flow.ui.base.ViewModelFactory
import com.gaurav.learn.kotlin.flow.utils.DefaultDispatcherProvider
import kotlinx.coroutines.launch


class FlowInternalsActivity : AppCompatActivity() {

    private val TAG = "~~~~~FlowInternalsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_internals)
        testFlow("Gaurav")
        testFlow("Harish")
    }

    private fun testFlow(fName: String) {

        val firstName = fName

        Flow(firstName)
            .map {
                return@map mapToFullName(it)
            }
            .map {
                return@map mapToUserName(it)
            }
            .filter {
                return@filter filterName(it)
            }
            .collect {
                Log.e(TAG, it?:"NULL Collected as name is not started with 'g'")
            }

    }

    private fun mapToFullName(firstName: String): String {
        return "$firstName Tyagi"
    }

    private fun mapToUserName(fullName: String): String {
        return fullName
            .replace(" ", "")
            .lowercase()
    }

    private fun filterName(fullName: String): Boolean {
        return fullName.startsWith("g",true)
    }

}