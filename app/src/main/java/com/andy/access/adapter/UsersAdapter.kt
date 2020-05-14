package com.andy.access.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andy.access.R
import com.andy.access.UserDetailActivity
import com.andy.access.model.User
import com.andy.access.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_user.view.*
import kotlin.properties.Delegates

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    companion object {
        const val USER_LOGIN = "userLogin"
    }

    var userList: List<User> by Delegates.observable(listOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.list_item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int) {
        val user = userList[position]

        Picasso.with(viewHolder.view.context)
            .load(user.avatarUrl)
            .transform(CircleTransform())
            .into(viewHolder.view.avatar_img)

        viewHolder.view.login_txt.text = user.name
        viewHolder.view.badge_txt.apply {
            text = getBadge(user.isAdmin)
            visibleOrGone(user.isAdmin)
        }

        viewHolder.clickListener = {
            val intent = Intent(viewHolder.view.context, UserDetailActivity::class.java).apply {
                putExtra(USER_LOGIN, user.name)
            }
            viewHolder.view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = userList.count()

    inner class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var clickListener: ((View) -> Unit)? = null

        init {
            view.setOnClickListener {
                clickListener?.invoke(it)
            }
        }
    }
}

fun getBadge(isAdmin: Boolean): String {
    return if (isAdmin) {
        "Admin"
    } else {
        ""
    }
}

fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}