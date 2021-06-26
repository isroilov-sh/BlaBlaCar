package tj.behruz.savorcarTj.di

import androidx.lifecycle.ViewModel
import tj.behruz.savorcarTj.ui.home.HomeViewModel
import tj.behruz.savorcarTj.ui.login.LoginViewModel
import tj.behruz.savorcarTj.ui.profile.UserViewModel
import tj.behruz.savorcarTj.ui.travel.TravelViewModel

abstract class BaseViewModel():ViewModel() {


    private val injector: ViewModelInjector = DaggerViewModelInjector.builder().repositoryModule(NetworkModule).build()


    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {

            is LoginViewModel ->   injector.inject(this)
            is HomeViewModel->injector.inject(this)
            is TravelViewModel->injector.inject(this)
            is UserViewModel->injector.inject(this  )

        }
    }
}