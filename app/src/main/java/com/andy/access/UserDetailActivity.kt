package com.andy.access

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.andy.access.adapter.UsersAdapter.Companion.USER_LOGIN
import com.andy.access.adapter.getBadge
import com.andy.access.adapter.visibleOrGone
import com.andy.access.utils.CircleTransform
import com.andy.access.viewmodels.UsersViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class UserDetailActivity : AppCompatActivity() {

    private val usersViewModel by lazy { ViewModelProvider(this).get(UsersViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        usersViewModel.getUserDetail().observe(this, Observer { userDetail ->
            Picasso.with(this)
                .load(userDetail.avatarUrl)
                .placeholder(R.mipmap.ic_launcher)
                .transform(CircleTransform())
                .into(avatar_img)

            name_txt.text = userDetail.name
            bio_txt.text = userDetail.bio

            login_txt.text = userDetail.login
            badge_txt.apply {
                text = getBadge(userDetail.isAdmin)
                visibleOrGone(userDetail.isAdmin)
            }

            location_txt.text = userDetail.location
            blog_txt.text = userDetail.blog
        })

        val userLogin: String? = intent.getStringExtra(USER_LOGIN)
        userLogin?.let {
            usersViewModel.loadUserDetail(it)
        }
    }
}