package com.gaurav.learn.kotlin.flow.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gaurav.learn.kotlin.flow.R
import com.gaurav.learn.kotlin.flow.ui.coldFlow.ColdFlowActivity
import com.gaurav.learn.kotlin.flow.ui.flowon.FlowOnActivity
import com.gaurav.learn.kotlin.flow.ui.hotFlow.sharedFlow.SharedFlowActivity
import com.gaurav.learn.kotlin.flow.ui.hotFlow.stateFlow.StateFlowActivity
import com.gaurav.learn.kotlin.flow.ui.retrofit.single.SingleNetworkCallActivity
import com.gaurav.learn.kotlin.flow.ui.room.RoomDBActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startSingleNetworkCallActivity(view: View) {
        startActivity(Intent(this@MainActivity, SingleNetworkCallActivity::class.java))
    }

    fun startSeriesNetworkCallsActivity(view: View) {
        //startActivity(Intent(this@MainActivity, SeriesNetworkCallsActivity::class.java))
    }

    fun startParallelNetworkCallsActivity(view: View) {
        //startActivity(Intent(this@MainActivity, ParallelNetworkCallsActivity::class.java))
    }

    fun startRoomDatabaseActivity(view: View) {
        startActivity(Intent(this@MainActivity, RoomDBActivity::class.java))
    }

    fun startCatchActivity(view: View) {
        //startActivity(Intent(this@MainActivity, CatchActivity::class.java))
    }

    fun startEmitAllActivity(view: View) {
        //startActivity(Intent(this@MainActivity, EmitAllActivity::class.java))
    }

    fun startCompletionActivity(view: View) {
        //startActivity(Intent(this@MainActivity, CompletionActivity::class.java))
    }

    fun startLongRunningTaskActivity(view: View) {
        //startActivity(Intent(this@MainActivity, LongRunningTaskActivity::class.java))
    }

    fun startTwoLongRunningTasksActivity(view: View) {
        //startActivity(Intent(this@MainActivity, TwoLongRunningTasksActivity::class.java))
    }

    fun startFilterActivity(view: View) {
        //startActivity(Intent(this@MainActivity, FilterActivity::class.java))
    }

    fun startMapActivity(view: View) {
        //startActivity(Intent(this@MainActivity, MapActivity::class.java))
    }

    fun startReduceActivity(view: View) {
        //startActivity(Intent(this@MainActivity, ReduceActivity::class.java))
    }

    fun startSearchActivity(view: View) {
        //startActivity(Intent(this@MainActivity, SearchActivity::class.java))
    }

    fun startRetryActivity(view: View) {
        //startActivity(Intent(this@MainActivity, RetryActivity::class.java))
    }

    fun startRetryWhenActivity(view: View) {
        //startActivity(Intent(this@MainActivity, RetryWhenActivity::class.java))
    }

    fun startRetryExponentialBackoffActivity(view: View) {
        //startActivity(Intent(this@MainActivity, RetryExponentialBackoffActivity::class.java))
    }

    fun startFlowOnActivity(view: View) {
        startActivity(Intent(this@MainActivity, FlowOnActivity::class.java))
    }

    fun startColdFlowActivity(view: View) {
        startActivity(Intent(this@MainActivity, ColdFlowActivity::class.java))
    }

    fun startStateFlowActivity(view: View) {
        startActivity(Intent(this@MainActivity, StateFlowActivity::class.java))
    }

    fun startSharedFlowActivity(view: View) {
        startActivity(Intent(this@MainActivity, SharedFlowActivity::class.java))
    }

}