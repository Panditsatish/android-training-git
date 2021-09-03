package com.application.khaokhana.api

import com.application.khaokhana.model.output.UserProfileResponse
import com.application.khaokhana.model.base.BaseResponse
import com.application.khaokhana.model.base.CommonApiResponse
import com.application.khaokhana.model.food.Food
import com.application.khaokhana.model.food.FoodResponse
import com.application.khaokhana.model.food.ImageResponse
import com.application.khaokhana.model.output.MyProfileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface IApiService {

    @FormUrlEncoded
    @POST("v1/employee/login")
    fun loginAPI(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<BaseResponse<UserProfileResponse?>>

    @GET("v1/food/list")
    fun getAllFoodAPI(@Query("vendorId") foodId: String?): Call<BaseResponse<FoodResponse?>>

    @GET("v1/employee/getMyProfile")
    fun getMyProfile(): Call<BaseResponse<MyProfileResponse?>>

    @Multipart
    @POST("v1/upload/images")
    fun addPhotos(
        @Part mediaFiles: List<MultipartBody.Part>
    ): Call<BaseResponse<ImageResponse?>>

    @POST("v1/food/add")
    fun addFoodAPI(@Body paymentProduct: Food?): Call<BaseResponse<CommonApiResponse?>>

    @DELETE("v1/food/delete")
    fun foodDeleteAPI(@Query("foodId") foodId: String?): Call<BaseResponse<CommonApiResponse?>>


    @PUT("v1/food/update")
    fun updateFoodAPI(@Body paymentProduct: Food?): Call<BaseResponse<CommonApiResponse?>>


}