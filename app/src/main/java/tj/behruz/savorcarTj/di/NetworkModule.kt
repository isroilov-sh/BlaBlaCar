package tj.behruz.savorcarTj.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import tj.behruz.savorcarTj.api.CarApi
import tj.behruz.savorcarTj.api.TravelApi
import tj.behruz.savorcarTj.api.UserApi
import tj.behruz.savorcarTj.networking.Networking

@Module
// Safe here as we are dealing with a Dagger 2 module
@Suppress("unused")
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {
        return Networking.getApiClient()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun userApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun travelApi(retrofit: Retrofit): TravelApi {
        return retrofit.create(TravelApi::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun carApi(retrofit: Retrofit): CarApi {
        return retrofit.create(CarApi::class.java)
    }

}