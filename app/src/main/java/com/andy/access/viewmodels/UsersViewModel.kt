package com.andy.access.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andy.access.api.GithubService
import com.andy.access.model.User
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class UsersViewModel : ViewModel() {

    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<User>> = users

    private fun loadUsers() {
        GithubService.create().getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Response<List<User>>> {

                override fun onSubscribe(d: Disposable) {
                }

                override fun onSuccess(response: Response<List<User>>) {
                    users.value = response.body()
                }

                override fun onError(e: Throwable) {
                }
            })
    }
}