package tj.behruz.savorcarTj.ui.travel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import tj.behruz.savorcarTj.api.TravelApi
import tj.behruz.savorcarTj.di.BaseViewModel
import tj.behruz.savorcarTj.models.*
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class TravelViewModel: BaseViewModel() {

    @Inject
    lateinit var travelApi: TravelApi

    var errorMessage: MutableLiveData<String> = MutableLiveData()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is UnknownHostException -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
            is ConnectException -> errorMessage.postValue("Не удаётся подключиться к серверу, попробуйте позже.")
            is TimeoutException -> errorMessage.postValue("Время ожидания истекло, попробуйте позже.")
            else -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
        }

    }


    fun getCitiesAsync(hash: String): LiveData<BaseModel<City>> {
        val cities = MutableLiveData<BaseModel<City>>()

        scope.launch(handler) {
            try {
                val cityResponse = travelApi.getCitiesAsync(hash = hash)
                cities.postValue(cityResponse.body())
            } catch (e: Exception) {

            }


        }
        return cities
    }


    fun searchTrip(userId: String, from: String, to: String, date: String, count: Int, hash: String): LiveData<SearchTrip> {
        val trip = MutableLiveData<SearchTrip>()

        try {

            scope.launch(handler) {

                val result = travelApi.searchTravelAsync(userId = userId.toInt(), fromCity = from, toCity = to, date = date, count = count, hash = hash).body()
                trip.postValue(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("ERROR",e.message.toString())
        }
        return trip


    }


    fun addTravel(userId: Int, from: String,fromPlace:String, to: String,toPlace:String, price: String, date: String, time: String, number: Int, hash: String): LiveData<EditModel> {
        val trip = MutableLiveData<EditModel>()
        try {
            scope.launch(handler) {
                val sendResult = travelApi.addTravelAsync(userId = userId, from = from, to = to, price = price, date = date, time = time, number = number, hash = hash,
                    fromPlace = fromPlace,toPlace = toPlace).body()
                trip.postValue(sendResult)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

        return trip

    }

    fun cancelBookingDriver(userId: Int,hash: String,bookingId:String,comment:String):LiveData<EditModel>{
        val result = MutableLiveData<EditModel>()
        try {
            scope.launch(handler) {
                val res = travelApi.cancelBookingDriver(userId = userId, hash = hash, bookingId = bookingId.toInt(),comment = comment)
                result.postValue(res.body())
            }
        } catch (e: Exception) {

        }
        return result
    }

    fun cancelTrip(userId: Int, hash: String, tripId: Int): LiveData<EditModel> {
        val result = MutableLiveData<EditModel>()
        try {
            scope.launch(handler) {
                val res = travelApi.cancelTrip(userId = userId, hash = hash, tripId = tripId)
                result.postValue(res.body())
            }
        } catch (e: Exception) {

        }
        return result
    }

    fun addBooking(userId: Int,count: Int,tripId: Int,hash: String):LiveData<EditModel>{
        val result = MutableLiveData<EditModel>()
        try {
            GlobalScope.launch {
                val res = travelApi.addBooking(userId = userId, hash = hash, tripId = tripId,count = count)
                result.postValue(res.body())
            }
        } catch (e: Exception) {

        }
        return result
    }

    fun getTripInfo(userId: Int, tripId: Int, hash: String): LiveData<TripInfo> {
        val tripInfo = MutableLiveData<TripInfo>()
        scope.launch(handler) {
            val result = travelApi.getTripInfo(userId = userId, tripId = tripId, hash = hash).body()
            tripInfo.postValue(result)
        }
        return tripInfo
    }


    fun cancelBookingPassenger(userId: Int,count: Int,tripId: Int,hash: String,bookingId: Int):LiveData<EditModel>{
        val result = MutableLiveData<EditModel>()
        try {
            scope.launch(handler) {
                val res = travelApi.cancelBookingPassenger(userId = userId, hash = hash, tripId = tripId,count = count,bookingId = bookingId)
                result.postValue(res.body())
            }
        } catch (e: Exception) {

        }
        return result
    }

    fun confirmBooking(userId: Int,bookingId: String,count: Int,hash: String):LiveData<EditModel>{
        val result = MutableLiveData<EditModel>()
        try {
            scope.launch(handler) {
                val res = travelApi.confirmBooking(userId = userId, hash = hash, bookingId = bookingId.toInt(),count = count)
                result.postValue(res.body())
            }
        } catch (e: Exception) {

        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

}