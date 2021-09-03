package com.application.khaokhana.ui.food


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.viewModels
import com.application.khaokhana.R
import com.application.khaokhana.base.App
import com.application.khaokhana.base.BaseActivity
import com.application.khaokhana.model.Media
import com.application.khaokhana.model.food.Food
import com.application.khaokhana.utils.AppConstant
import com.application.khaokhana.utils.AppUtil
import com.application.khaokhana.viewModel.AuthViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.activity_edit_food.*
import java.io.ByteArrayOutputStream


class EditFoodActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private var foodId: String? = null



    override fun layoutRes(): Int {
        return R.layout.activity_edit_food
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListener()
        setObservables()
        getBundleData()
    }

    private fun setListener() {
        ivBack.setOnClickListener {
            finish()
        }
        btnSave.setOnClickListener {
            AppUtil.preventTwoClick(it)
            val foodName = etFoodName.text.toString().trim()
            val foodPrice = etFoodPrice.text.toString().trim()
            val foodQyt = etFoodQuantity.text.toString().trim()
            if (foodName.isNotEmpty() && foodPrice.isNotEmpty() && foodPrice.isNotEmpty()) {
                updateFoodAPI(foodName, foodPrice, foodQyt)

            } else {
                AppUtil.showToast("something going wrong")
            }
        }

    }


    private fun updateFoodAPI(foodName: String, foodPrice: String, foodQyt: String/*,imageView: ImageView*/) {
        if (AppUtil.isConnection()) {
            val foodReq = Food()
            foodReq.foodId = foodId
            foodReq.isAvailable = 1
            foodReq.status = 1
            foodReq.name = foodName
            foodReq.price = foodPrice.toFloat()
            foodReq.availableQuantity = foodQyt.toInt()
            viewModel.updateFoodAPI(foodReq)
        } else {
            AppUtil.showToast(getString(R.string.msg_network_connection))
        }
    }

    private fun getBundleData() {
        val bundle: Bundle? = intent.extras
        bundle?.let {
            val foodData = it.getSerializable(AppConstant.BK.FOODS) as Food?
            foodId = foodData?.foodId
            setDataOnUI(foodData)
        }
    }

    private fun setDataOnUI(foodData: Food?) {
        foodData?.let {
            etFoodName.setText(it.name)
            etFoodPrice.setText(it.price.toString())
            etFoodQuantity.setText(it.availableQuantity.toString())
        }
    }

    private fun setObservables() {

        viewModel.addFoodSuccess.observe(this) { data ->
            hideProgressBar()
            AppUtil.showToast(data?.message)
            finish()
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)
        }

    }
}
