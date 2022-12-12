package com.codinglance.githubtrendingrepo.ui.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codinglance.githubtrendingrepo.model.Item
import com.codinglance.githubtrendingrepo.model.RepoResponse
import com.codinglance.githubtrendingrepo.repository.Repository
import com.codinglance.githubtrendingrepo.repository.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application)
{
    var repolist: ArrayList<Item> = ArrayList()
    var mSubscriber= MutableLiveData(false)

    init {
    getRepo()
    }



    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val _repoState: MutableLiveData<ResultState<RepoResponse>> = MutableLiveData()
    val repoState: LiveData<ResultState<RepoResponse>> = _repoState


    fun getRepo() {
        viewModelScope.launch(Dispatchers.IO)
        {
            _repoState.postValue(ResultState.Loading)


            try {

                val response=repository.getRepo()
                if (response.items.isNotEmpty()) {
                    _repoState.postValue(ResultState.Success(response))
                    repolist= response.items as ArrayList<Item>
                    mSubscriber.postValue(true)
                }
            }

            catch (e:Exception){
                _repoState.postValue(ResultState.Error(e.localizedMessage.toString()))

            }

        }
    }

}