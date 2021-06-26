package tj.behruz.savorcarTj.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import tj.behruz.savorcarTj.api.TravelApi
import tj.behruz.savorcarTj.di.BaseViewModel
import tj.behruz.savorcarTj.models.TripsResponse
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class HomeViewModel(): BaseViewModel() {


    @Inject
    lateinit var travelApi: TravelApi
    var errorMessage: MutableLiveData<String> = MutableLiveData()
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is UnknownHostException -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
            is ConnectException -> errorMessage.postValue("Не удаётся подключиться к серверу, попробуйте позже.")
            is TimeoutException -> errorMessage.postValue("Время ожидания истекло, попробуйте позже.")
            else -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
        }

    }


    fun getAllTrips(userId: Int, hash: String): LiveData<TripsResponse> {

        val trips = MutableLiveData<TripsResponse>()
        scope.launch(handler) {
            val tripResult = travelApi.getTrips(userId = userId, hash = hash)
            trips.postValue(tripResult.body())
        }

        return trips


    }

    fun getAllHistory(userId: Int, hash: String): LiveData<TripsResponse> {

        val trips = MutableLiveData<TripsResponse>()
        viewModelScope.launch(handler) {
            val tripResult = travelApi.getAllHistory(userId = userId, hash = hash)
            trips.postValue(tripResult.body())
        }

        return trips


    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

}