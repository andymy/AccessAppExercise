package com.andy.access.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andy.access.api.GithubService
import com.andy.access.model.UserDetail
import com.andy.access.model.User
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class UsersViewModel : ViewModel() {

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    private var nextSince = 0

    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers(0)
        }
    }

    private val userDetail: MutableLiveData<UserDetail> by lazy {
        MutableLiveData<UserDetail>()
    }

    fun getUsers(): LiveData<List<User>> = users

    fun getUserDetail(): LiveData<UserDetail> = userDetail

    fun loadMore() {
        if (!isLastPage) {
            loadUsers(nextSince)
        }
    }

    private fun loadUsers(since: Int) {
        GithubService.create().getUsers(since)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Response<List<User>>> {

                override fun onSubscribe(d: Disposable) {
                    isLoading = true
                }

                override fun onSuccess(response: Response<List<User>>) {
                    nextSince = response.getLindHeader()?.parseNextSince() ?: 0

                    val userResponseList = response.body().orEmpty()
                    if (users.value.isNullOrEmpty()) {
                        users.value = userResponseList
                    } else {
                        val moreUserList = users.value.orEmpty().toMutableList()
                        moreUserList.addAll(userResponseList)
                        if (moreUserList.size >= 100) {
                            isLastPage = true
                        }
                        users.value = moreUserList
                    }
                    isLoading = false
                }

                override fun onError(e: Throwable) {
                    isLoading = false
                }
            })
    }

    fun loadUserDetail(name: String) {
        GithubService.create().getUserDetail(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Response<UserDetail>> {

                override fun onSubscribe(d: Disposable) {
                }

                override fun onSuccess(response: Response<UserDetail>) {
                    userDetail.value = response.body()
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    private fun Response<List<User>>.getLindHeader(): String? {
        return this.headers().get("link").also {
            println("linkHeader=$it")
        }
    }

    private fun String.parseNextSince(): Int {
        fun String.parseSince(): Int {
            val sinceIndex = this.indexOf("since")
            val startIndex = this.indexOf("=", sinceIndex) + 1
            val endIndex = this.indexOf("&", sinceIndex)
            return this.subSequence(startIndex, endIndex).toString().toInt()
        }

        this.split(",").forEach { string ->
            if (string.contains("next")) {
                return string.parseSince().also {
                    println("nextSince=$it")
                }
            }
        }
        return 0
    }
}