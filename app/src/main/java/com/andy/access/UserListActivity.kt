package com.andy.access

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andy.access.adapter.UsersAdapter
import com.andy.access.utils.PaginationScrollListener
import com.andy.access.viewmodels.UsersViewModel
import kotlinx.android.synthetic.main.activity_main.*

class UserListActivity : AppCompatActivity() {

    private val layoutManager by lazy { LinearLayoutManager(this) }
    private val usersAdapter by lazy { UsersAdapter() }
    private val usersViewModel by lazy { ViewModelProvider(this).get(UsersViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        userListRecyclerView.apply {
            layoutManager = this@UserListActivity.layoutManager
            adapter = usersAdapter
            addOnScrollListener(object :
                PaginationScrollListener(layoutManager as LinearLayoutManager) {
                override fun isLastPage(): Boolean {
                    return usersViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return usersViewModel.isLoading
                }

                override fun loadMoreItems() {
                    usersViewModel.loadMore()
                }
            })
        }

        usersViewModel.getUsers().observe(this, Observer {
            usersAdapter.userList = it
        })
    }
}