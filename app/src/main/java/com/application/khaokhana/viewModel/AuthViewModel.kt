package com.application.khaokhana.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.application.khaokhana.api.APICallHandler
import com.application.khaokhana.api.APICallManager

import com.application.khaokhana.model.base.Errors
import com.application.khaokhana.api.APIType
import com.application.khaokhana.callback.CallbackType
import com.application.khaokhana.model.Media
import com.application.khaokhana.model.base.CommonApiResponse
import com.application.khaokhana.model.food.Food
import com.application.khaokhana.model.output.UserProfileResponse
import com.application.khaokhana.model.food.FoodResponse
import com.application.khaokhana.model.food.ImageResponse
import com.application.khaokhana.model.output.MyProfileResponse
import com.application.khaokhana.utils.AppConstant
import com.application.khaokhana.utils.AppUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AuthViewModel : ViewModel(), APICallHandler<Any> {


    var loginSuccess =
        MutableLiveData<UserProfileResponse>()

    var error =
        MutableLiveData<Errors>()

    var foodListSuccess =
        MutableLiveData<FoodResponse>()

    var imageUploadSuccess =
        MutableLiveData<ImageResponse>()

    var myProfileSuccess =
        MutableLiveData<MyProfileResponse>()

    var addFoodSuccess =
        MutableLiveData<CommonApiResponse>()

    var foodDeleteSuccess =
        MutableLiveData<CommonApiResponse>()
    var foodUpdateSuccess =
        MutableLiveData<CommonApiResponse>()


    fun loginAPI(email: String?, password: String?) {
        val apiCallManager = APICallManager(APIType.SIGN_IN, this)
        apiCallManager.loginAPI(email, password)
    }

    fun getAllFoodAPI(vendorId: String?) {
        val apiCallManager = APICallManager(APIType.FOOD_LIST, this)
        apiCallManager.getAllFoodAPI(vendorId)
    }

    fun foodDeleteAPI(foodId: String?) {
        val apiCallManager = APICallManager(APIType.FOOD_DELETE, this)
        apiCallManager.foodDeleteAPI(foodId)
    }

    fun getMyProfile() {
        val apiCallManager = APICallManager(APIType.USER_PROFILE, this)
        apiCallManager.getMyProfile()
    }

    fun addFoodAPI(data: Food?) {
        val apiCallManager = APICallManager(APIType.ADD_FOOD, this)
        apiCallManager.addFoodAPI(data)
    }

    fun updateFoodAPI(data: Food?) {
        val apiCallManager = APICallManager(APIType.UPDATE_FOOD, this)
        apiCallManager.updateFoodAPI(data)
    }

    override fun onSuccess(apiType: APIType, response: Any?) {

        when (apiType) {

            APIType.SIGN_IN -> {
                val userProfileResponse = response as UserProfileResponse
                loginSuccess.setValue(userProfileResponse)
            }
            APIType.FOOD_LIST -> {
                val userFoodResponse = response as FoodResponse
                foodListSuccess.setValue(userFoodResponse)

            }
            APIType.ADD_PHOTO -> {
                val imageResponse = response as ImageResponse
                imageUploadSuccess.setValue(imageResponse)
            }
            APIType.USER_PROFILE -> {
                val foodResponse = response as MyProfileResponse
                myProfileSuccess.setValue(foodResponse)
            }

            APIType.ADD_FOOD, APIType.UPDATE_FOOD -> {
                val foodResponse = response as CommonApiResponse
                addFoodSuccess.setValue(foodResponse)
            }

            APIType.FOOD_DELETE -> {
                val foodResponse = response as CommonApiResponse
                foodDeleteSuccess.setValue(foodResponse)
            }
            APIType.UPDATE_FOOD -> {

                val foodResponse = response as CommonApiResponse
                foodUpdateSuccess.setValue(foodResponse)
            }

            else -> {
            }
        }

    }

    override fun onFailure(apiType: APIType, error: Errors) {
        this.error.value = error
    }

    fun addFoodImageUploadAPI(selectedMediaFiles: List<Media?>?) {

        val multipartList: MutableList<MultipartBody.Part> = ArrayList()

        for (i in selectedMediaFiles!!.indices) {
            val file = File(selectedMediaFiles[i]?.image)
            val requestFileSocialImage = RequestBody.create(
                AppUtil.getMimeType(selectedMediaFiles[i]?.image).toMediaTypeOrNull(), file
            )
            val socialImageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                AppConstant.MT.UPLOAD_MEDIA,
                file.name,
                requestFileSocialImage
            )

            multipartList.add(socialImageMultipart)
        }

        val apiCallManager = APICallManager(APIType.ADD_PHOTO, this)
        apiCallManager.addFoodImageUploadAPI(multipartList)

    }
}