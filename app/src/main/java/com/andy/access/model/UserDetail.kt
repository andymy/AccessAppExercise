package com.andy.access.model

import com.google.gson.annotations.SerializedName

data class UserDetail(

    @SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("bio")
    val bio: String? = null,

    @SerializedName("login")
    val login: String? = null,

    @SerializedName("site_admin")
    val isAdmin: Boolean = false,

    @SerializedName("location")
    val location: String? = null,

    @SerializedName("blog")
    val blog: String? = null
)