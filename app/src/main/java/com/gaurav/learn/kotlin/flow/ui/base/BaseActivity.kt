package com.gaurav.learn.kotlin.flow.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.learn.kotlin.flow.data.local.entity.User
import com.gaurav.learn.kotlin.flow.data.model.ApiUser
import com.gaurav.learn.kotlin.flow.databinding.ActivityRecyclerViewBinding
import com.gaurav.learn.kotlin.flow.ui.room.RoomDBActivity

open class BaseActivity : AppCompatActivity() {

    // For API Calls
    private lateinit var adapterForApis: ApiUserAdapter

    // View for SingleNetworkCallActivity
    lateinit var binding: ActivityRecyclerViewBinding

    // For Room DB
    private lateinit var adapterForDB: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )

        if (this is RoomDBActivity) {
            adapterForDB =
                UserAdapter(
                    arrayListOf()
                )
            binding.recyclerView.adapter = adapterForDB
        } else {
            adapterForApis =
                ApiUserAdapter(
                    arrayListOf()
                )

            binding.recyclerView.adapter = adapterForApis
        }

    }

    fun renderList(users: List<ApiUser>) {
        adapterForApis.addData(users)
        adapterForApis.notifyDataSetChanged()
    }

    fun renderDBList(users: List<User>) {
        adapterForDB.addData(users)
        adapterForDB.notifyDataSetChanged()
    }

    open fun setupViewModel() {
        setupViewModel()
    }

    open fun setupObserver() {
        setupObserver()
    }


}