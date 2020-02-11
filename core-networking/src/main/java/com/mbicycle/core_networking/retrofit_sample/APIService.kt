package com.mbicycle.core_networking.retrofit_sample

import com.mbicycle.core_utils.Empty
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/*
 * Property of  MBicycle Development Company
 * https://mbicycle.com
 */

/**
 * It's a sample of Retrofit's api service
 */
interface APIService {

    @Multipart
    @POST("your address")
    fun performAChefRegistration(
        @Part acf: List<MultipartBody.Part>,
        @Part cert: List<MultipartBody.Part>,
        @Part dto: MultipartBody.Part,
        @Part rar: List<MultipartBody.Part>
    ): Call<Empty>

    @POST("your address")
    fun performDinerRegistration(@Body dinerRegistrationRequestBody: Any): Call<Empty>

    @GET("user/me")
    fun whoAmI(): Call<Any>
}
