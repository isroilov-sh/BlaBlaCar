package tj.behruz.savorcarTj.networking

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Networking {

    companion object {

        const val route = "android/"
        const val action = "action"
        const val hash = "hash"
        const val method = "method"
        private const val baseUrl = "https://blablacar.tj/api/"

        private var retrofit: Retrofit? = null

        fun getApiClient(): Retrofit {

            if (retrofit == null) {

                val client = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
                    .callTimeout(1, TimeUnit.MINUTES).readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()

                val moshi = Moshi.Builder().build()

                retrofit = Retrofit.Builder().baseUrl(baseUrl).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit!!
        }

    }
}