package tj.behruz.savorcarTj.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import tj.behruz.savorcarTj.api.UserApi
import tj.behruz.savorcarTj.di.BaseViewModel
import tj.behruz.savorcarTj.models.LoginModel
import tj.behruz.savorcarTj.models.UserResponse
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class LoginViewModel: BaseViewModel() {


    @Inject
    lateinit var userApi: UserApi

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

    fun login(phone: String, hash: String): LiveData<LoginModel> {
        val loginResult = MutableLiveData<LoginModel>()

        scope.launch(handler) {
            val result = userApi.loginAsync(phone = phone, hash = hash).body()
            loginResult.postValue(result)
        }

        return loginResult
    }

    fun signIn(phone: String, hash: String): LiveData<UserResponse> {
        val auth = MutableLiveData<UserResponse>()
        scope.launch(handler) {
            val authResult = userApi.signInAsync(phone = phone, hash = hash)
            auth.postValue(authResult.body())
        }

        return auth
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }


}