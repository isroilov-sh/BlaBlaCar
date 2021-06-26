package tj.behruz.savorcarTj.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import tj.behruz.savorcarTj.models.BaseModel
import tj.behruz.savorcarTj.models.CarProperty
import tj.behruz.savorcarTj.networking.Networking

interface CarApi {


    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getCarPropertiesAsync(
        @Field(Networking.action) login: String = "login",
        @Field(Networking.method) method: String,
        @Field("hash") hash: String
    ): Response<BaseModel<CarProperty>>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getCarClassAsync(
        @Field(Networking.action) login: String = "login",
        @Field(Networking.method) method: String = "getCarClass",
        @Field("car_mark") carId: Int,
        @Field("hash") hash: String
    ): Response<BaseModel<CarProperty>>


}
