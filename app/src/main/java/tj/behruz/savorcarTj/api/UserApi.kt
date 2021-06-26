package tj.behruz.savorcarTj.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import tj.behruz.savorcarTj.models.*
import tj.behruz.savorcarTj.networking.Networking

interface UserApi {

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun loginAsync(
        @Field(Networking.action) login: String = "login",
        @Field(Networking.method) signup_by_number: String = "signup_by_number",
        @Field("phone") phone: String,
        @Field("hash") hash: String
    ): Response<LoginModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun signInAsync(
        @Field(Networking.action) login: String = "login",
        @Field(Networking.method) signin: String = "signin",
        @Field("phone") phone: String,
        @Field("hash") hash: String
    ): Response<UserResponse>


    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getCitiesAsync(
        @Field(Networking.action) login: String = "login",
        @Field(Networking.method) getCities: String = "getCities",
        @Field("hash") hash: String
    ): Response<BaseModel<City>>


    @Multipart
    @POST(Networking.route)
    suspend fun updateProfileImage(
        @Part("action") home: RequestBody,
        @Part("method") editProfile: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("user_id") userId: RequestBody,
        @Part("hash") hash: RequestBody
    ): Response<EditModel>

    @Multipart
    @POST(Networking.route)
    suspend fun updateCarImage(
        @Part("action") home: RequestBody,
        @Part("method") editProfile: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("user_id") userId: RequestBody,
        @Part("hash") hash: RequestBody
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun updateCar(
        @Field("action") home: String = "home",
        @Field("method") method: String = "editCar",
        @Field("car_mark") carMark: Int,
        @Field("car_class") carClass: Int,
        @Field("car_body") carBody: Int,
        @Field("car_color") carColor: String,
        @Field("gos_nomer") number: String,
        @Field("user_id") user_id: Int,
        @Field("hash") hash: String
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun updateUserProfile(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "editProfile",
        @Field("name") name: String,
        @Field("city") city: String,
        @Field("bdate") bdate: String,
        @Field("gander") gender: Int,
        @Field("user_id") user_id: Int,
        @Field("hash") hash: String,
        @Field("email") email: String
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun registrationAsync(
        @Field(Networking.action) login: String = "login",
        @Field(Networking.method) signup: String = "signup",
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("gander") gander: Int,
        @Field("bdate") bdate: String,
        @Field("city") city: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("hash") hash: String,
        @Field("car_mark") car_mark: String,
        @Field("car_body") car_body: String,
        @Field("car_class") carClass: String,
        @Field(
            "car_color"
        ) car_color: String,
        @Field("gos_nomer") gos_nomer: String
    ): Response<RegistrationModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getAllReviews(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "getRecieveReviews",
        @Field("user_id") user_id: Int,
        @Field("hash") hash: String
    ): Response<RateResponse>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getPutReviews(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "getPutReviews",
        @Field("user_id") user_id: Int,
        @Field("hash") hash: String
    ): Response<RateResponse>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getPreference(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "getAllPreference",
        @Field("user_id") user_id: Int,
        @Field("hash") hash: String
    ): Response<PreferencePayload>


    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getUserInfo(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "getUserInfo",
        @Field("user_id") user_id: Int,
        @Field("hash") hash: String,
        @Field("user_to_get") userName: String
    ): Response<UserInfoResponse>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun addPreference(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "editPreference",
        @Field("user_id") user_id: Int,
        @Field("hash") hash: String,
        @Field("preference") preference: String
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun sendFireBaseToken(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "sendAndroidToken",
        @Field("user_id") user_id: Int,
        @Field("android_token") token: String,
        @Field("hash") hash: String
    ): Response<EditModel>
}

