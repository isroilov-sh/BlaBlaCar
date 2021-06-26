package tj.behruz.savorcarTj.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import tj.behruz.savorcarTj.api.CarApi
import tj.behruz.savorcarTj.api.TravelApi
import tj.behruz.savorcarTj.api.UserApi
import tj.behruz.savorcarTj.di.BaseViewModel
import tj.behruz.savorcarTj.models.*
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class UserViewModel : BaseViewModel() {


    @Inject
    lateinit var userApi: UserApi

    @Inject
    lateinit var travelApi: TravelApi

    @Inject
    lateinit var carApi: CarApi

    var errorMessage: MutableLiveData<String> = MutableLiveData()
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is UnknownHostException -> errorMessage.postValue("Неизвестный хост, попробуйте позже.")
            is ConnectException -> errorMessage.postValue("Не удаётся подключиться к серверу, попробуйте позже.")
            is TimeoutException -> errorMessage.postValue("Время ожидания истекло, попробуйте позже.")
            else -> errorMessage.postValue("Неизвестный ошибка, попробуйте позже.")
        }

    }

    fun signIn(phone: String, hash: String): LiveData<UserResponse> {
        val auth = MutableLiveData<UserResponse>()
        scope.launch(handler) {
            val authResult = userApi.signInAsync(phone = phone, hash = hash)
            auth.postValue(authResult.body())
        }

        return auth
    }

    fun updateProfilePhoto(
        home: RequestBody,
        editProfile: RequestBody,
        photo: MultipartBody.Part,
        userId: RequestBody,
        hash: RequestBody
    ): LiveData<EditModel> {
        val result = MutableLiveData<EditModel>()

        scope.launch(handler) {
            try {
                val resultResponse =
                    userApi.updateProfileImage(home, editProfile, photo, userId, hash = hash)
                result.postValue(resultResponse.body())
            } catch (e: Exception) {
                //    result.postValue(BaseResponse(400, "Неизвестная ошибка !", null))
            }


        }
        return result


    }

    fun updateProfile(
        name: String,
        city: String,
        bdate: String,
        gender: Int,
        userId: Int,
        hash: String,
        email: String
    ): LiveData<EditModel> {
        val result = MutableLiveData<EditModel>()

        scope.launch(handler) {
            try {
                val resultRespone = userApi.updateUserProfile(
                    name = name,
                    city = city,
                    bdate = bdate,
                    gender = gender,
                    user_id = userId,
                    hash = hash,
                    email = email
                )
                result.postValue(resultRespone.body())
            } catch (e: Exception) {

            }


        }
        return result


    }

    fun updateCar(
        carMark: String,
        carClass: String,
        carBody: String,
        carColor: String,
        number: String,
        userId: Int,
        hash: String
    ): LiveData<EditModel> {

        val result = MutableLiveData<EditModel>()

        scope.launch(handler) {
            try {
                val resultResponse = userApi.updateCar(
                    carMark = carMark.toInt(),
                    carClass = carClass.toInt(),
                    carBody = carBody.toInt(),
                    carColor = carColor,
                    number = number,
                    user_id = userId,
                    hash = hash
                )
                result.postValue(resultResponse.body())
            } catch (e: Exception) {
                Log.d("BEHRUZ", e.toString())
                e.printStackTrace()
            }


        }
        return result

    }


    fun updateCarPhoto(
        home: RequestBody,
        editProfile: RequestBody,
        photo: MultipartBody.Part,
        userId: RequestBody,
        hash: RequestBody
    ): LiveData<EditModel> {
        val result = MutableLiveData<EditModel>()

        scope.launch(handler) {
            try {
                val resultResponse =
                    userApi.updateCarImage(home, editProfile, photo, userId, hash = hash)
                result.postValue(resultResponse.body())
            } catch (e: Exception) {
                // result.postValue(BaseResponse(400, "Неизвестная ошибка !", null))
            }


        }
        return result


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

    fun getCarMarks(type: String, hash: String): LiveData<BaseModel<CarProperty>> {

        val cities = MutableLiveData<BaseModel<CarProperty>>()

        scope.launch(handler) {
            try {
                val cityResponse = carApi.getCarPropertiesAsync(hash = hash, method = type)
                cities.postValue(cityResponse.body())
            } catch (e: java.lang.Exception) {
                Log.d("BEHRUZ", e.toString())
            }


        }
        return cities


    }

    fun getCarClass(type: String, id: Int, hash: String): LiveData<BaseModel<CarProperty>> {

        val cities = MutableLiveData<BaseModel<CarProperty>>()

        scope.launch(handler) {
            try {
                val cityResponse = carApi.getCarClassAsync(hash = hash, method = type, carId = id)
                cities.postValue(cityResponse.body())
            } catch (e: java.lang.Exception) {
                Log.d("BEHRUZ", e.toString())
            }


        }
        return cities


    }

    fun registrationAsync(
        phone: String,
        email: String,
        gender: Int,
        bdate: String,
        city: String,
        first_name: String,
        last_name: String,
        hash: String,
        car_mark: String,
        car_color: String,
        gos_nomer: String,
        car_body: String,
        car_class: String
    ): LiveData<RegistrationModel> {
        val registrationPayload = MutableLiveData<RegistrationModel>()

        scope.launch(handler) {
            val cityResponse = userApi.registrationAsync(
                city = city,
                phone = phone,
                email = email,
                gander = gender,
                bdate = bdate,
                first_name = first_name,
                last_name = last_name,
                hash = hash,
                car_color = car_color,
                car_mark = car_mark,
                gos_nomer = gos_nomer,
                car_body = car_body,
                carClass = car_class
            )
            registrationPayload.postValue(cityResponse.body())

        }
        return registrationPayload

    }

    fun getAllReviews(id: Int, hash: String): LiveData<RateResponse> {

        val cities = MutableLiveData<RateResponse>()

        scope.launch(handler) {
            try {
                val cityResponse = userApi.getAllReviews(hash = hash, user_id = id)
                cities.postValue(cityResponse.body())
            } catch (e: java.lang.Exception) {

            }


        }
        return cities


    }

    fun getPreference(id: Int, hash: String): LiveData<PreferencePayload> {

        val cities = MutableLiveData<PreferencePayload>()

        scope.launch(handler) {
            try {
                val cityResponse = userApi.getPreference(hash = hash, user_id = id)
                cities.postValue(cityResponse.body())
            } catch (e: java.lang.Exception) {

            }


        }
        return cities


    }


    fun getUserInfo(id: Int, hash: String, userName: String): LiveData<UserInfoResponse> {

        val userInfo = MutableLiveData<UserInfoResponse>()

        scope.launch(handler) {
            try {
                val cityResponse =
                    userApi.getUserInfo(hash = hash, user_id = id, userName = userName)
                userInfo.postValue(cityResponse.body())
            } catch (e: java.lang.Exception) {

            }


        }
        return userInfo


    }

    fun addPreference(id: Int, hash: String, preference: String): LiveData<EditModel> {

        val userInfo = MutableLiveData<EditModel>()

        scope.launch(handler) {
            try {

                val cityResponse =
                    userApi.addPreference(hash = hash, user_id = id, preference = preference.toString())
                userInfo.postValue(cityResponse.body())
            } catch (e: java.lang.Exception) {

            }


        }
        return userInfo


    }

    fun sendFireBaseToken(userId: Int, token: String, hash: String): LiveData<EditModel> {
        val sendToken = MutableLiveData<EditModel>()
        scope.launch(handler) {
            try {
                val tokenResult =
                    userApi.sendFireBaseToken(user_id = userId, token = token, hash = hash)
                sendToken.postValue(tokenResult.body())
                Log.d("BEHRUZ", "{action ${tokenResult.body().toString()}}")
            } catch (e: java.lang.Exception) {
            }


        }
        return sendToken
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

}
