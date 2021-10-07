package tj.behruz.savorcarTj.di

import dagger.Component
import tj.behruz.savorcarTj.ui.home.HomeViewModel
import tj.behruz.savorcarTj.ui.login.LoginViewModel
import tj.behruz.savorcarTj.ui.profile.UserViewModel
import tj.behruz.savorcarTj.ui.travel.TravelViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(loginViewModel: LoginViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(travelViewModel: TravelViewModel)
    fun inject(userViewModel: UserViewModel)



    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun repositoryModule(networkModule: NetworkModule): Builder
    }

}