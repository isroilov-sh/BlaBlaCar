package tj.behruz.savorcarTj.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import tj.behruz.savorcarTj.models.*
import tj.behruz.savorcarTj.networking.Networking

interface TravelApi {

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getTrips(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "getTrips",
        @Field("user_id") userId: Int,
        @Field("hash") hash: String
    ): Response<TripsResponse>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getCitiesAsync(
        @Field(Networking.action) login: String = "login",
        @Field(Networking.method) getCities: String = "getCities",
        @Field("hash") hash: String
    ): Response<BaseModel<City>>


    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun searchTravelAsync(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) searchTrip: String = "searchTrip",
        @Field("user_id") userId: Int,
        @Field("from") fromCity: String,
        @Field("to") toCity: String,
        @Field("count") count: Int,
        @Field("date") date: String,
        @Field("hash") hash: String
    ): Response<SearchTrip>


    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun addTravelAsync(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) add: String = "addTrip",
        @Field("user_id") userId: Int,
        @Field("from") from: String,
        @Field("to") to: String,
        @Field("price") price: String,
        @Field("date") date: String,
        @Field("time") time: String,
        @Field("number") number: Int,
        @Field("hash") hash: String,
        @Field("from_place") fromPlace: String,
        @Field("to_place") toPlace: String,
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun cancelBookingDriver(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "cancelBookingDriver",
        @Field("user_id") userId: Int,
        @Field("hash") hash: String,
        @Field("booking_id") bookingId: Int,
        @Field("comment") comment: String
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun cancelTrip(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "cancelTrip",
        @Field("user_id") userId: Int,
        @Field("hash") hash: String,
        @Field("trip_id") tripId: Int
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getTripInfo(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "getTripInfo",
        @Field("user_id") userId: Int,
        @Field("trip_id") tripId: Int,
        @Field("hash") hash: String
    ): Response<TripInfo>


    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun addBooking(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "addBooking",
        @Field("user_id") userId: Int,
        @Field("count") count: Int,
        @Field("trip_id") tripId: Int,
        @Field("hash") hash: String
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun getAllHistory(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "getTripsHistory",
        @Field("user_id") userId: Int,
        @Field("hash") hash: String
    ): Response<TripsResponse>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun confirmBooking(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "confirmBooking",
        @Field("user_id") userId: Int,
        @Field("hash") hash: String,
        @Field("booking_id") bookingId: Int,
        @Field("count") count: Int
    ): Response<EditModel>

    @FormUrlEncoded
    @POST(Networking.route)
    suspend fun cancelBookingPassenger(
        @Field(Networking.action) home: String = "home",
        @Field(Networking.method) method: String = "cancelBookingDriver",
        @Field("user_id") userId: Int,
        @Field("hash") hash: String,
        @Field("booking_id") bookingId: Int,
        @Field("tripId") tripId: Int,
        @Field("count") count: Int
    ): Response<EditModel>

}