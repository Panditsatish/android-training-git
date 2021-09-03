package com.application.khaokhana.ui.food

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.application.khaokhana.R
import com.application.khaokhana.base.App
import com.application.khaokhana.base.BaseActivity
import com.application.khaokhana.extension.invisible
import com.application.khaokhana.model.Media
import com.application.khaokhana.model.food.Food
import com.application.khaokhana.ui.home.BottomSheetFragment
import com.application.khaokhana.utils.AppConstant
import com.application.khaokhana.utils.AppConstant.IMAGE_PICK_CODE
import com.application.khaokhana.utils.AppConstant.REQUEST_CODE
import com.application.khaokhana.utils.AppUtil
import com.application.khaokhana.utils.PreferenceKeeper
import com.application.khaokhana.viewModel.AuthViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.android.synthetic.main.activity_vendor_profile.*
import java.io.ByteArrayOutputStream

class AddFoodActivity : BaseActivity(), BottomSheetFragment.CameraGallery {

    private val viewModel: AuthViewModel by viewModels()

    private val vendorId:String?=null

    private var selectedMediaFiles: MutableList<Media>? = ArrayList()

    val bottomSheetFragment = BottomSheetFragment(this@AddFoodActivity)

    override fun layoutRes(): Int {
        return R.layout.activity_add_food
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListener()
    }

    private fun setListener() {
        tvBack.setOnClickListener {
            finish()
        }
        image_button.setOnClickListener {
            validatePermission()
        }

        setObservables()
        btnAddFood.setOnClickListener {
            if (AppUtil.isConnection()) {
                showProgressBar()
                viewModel.addFoodImageUploadAPI(selectedMediaFiles)
            }
        }
        ivDelete.setOnClickListener {
            var tempUri: Uri? = null
            if (selectedMediaFiles !=null){
                val media = Media()
                media.image = tempUri?.let { getPath(it) }
                selectedMediaFiles?.removeAt(selectedMediaFiles!!.size - 1)
            }
            photo1.setImageDrawable(null);
            ivDelete.invisible()
        }
        ivDelete1.setOnClickListener {
            var tempUri: Uri? = null
            if (selectedMediaFiles !=null){
                val media = Media()
                media.image = tempUri?.let { getPath(it) }
                selectedMediaFiles?.removeAt(selectedMediaFiles!!.size - 1)
            }
            photo1.setImageDrawable(null);
            ivDelete1.invisible()
        }

    }

    private fun validatePermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Toast.makeText(this@AddFoodActivity, "Permission Granted", Toast.LENGTH_LONG)
                        .show()

                    bottomSheetFragment.show(supportFragmentManager, "bottomsheetdialog")

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@AddFoodActivity,
                        "storage_permission_denied_message",
                        Toast.LENGTH_LONG
                    ).show()
                    bottomSheetFragment.dismiss()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?
                ) {
                    AlertDialog.Builder(this@AddFoodActivity)
                        .setTitle("storage_permission_rationale_title")
                        .setMessage("storage_permission_rationale_message")
                        .setNegativeButton(
                            android.R.string.cancel,
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                dialogInterface.dismiss()
                                token?.cancelPermissionRequest()
                            })
                        .setPositiveButton(
                            android.R.string.ok,
                            DialogInterface.OnClickListener { dialogInterface, i ->
                                dialogInterface.dismiss()
                                token?.continuePermissionRequest()
                            })
                        .show()
                }
            }
            ).check()
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AppConstant.PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {

                    AppUtil.showToast("Permission denied")
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var tempUri: Uri? = null
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            photo1.setImageURI(data?.data)
            ivDelete.visibility = View.VISIBLE
            tempUri = data?.data
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            //  photo3.setImageURI(data?.data)
            photo1.setImageBitmap(data.extras?.get("data") as Bitmap)
            ivDelete.visibility = View.VISIBLE
            tempUri = getImageUri(applicationContext, data.extras?.get("data") as Bitmap)
        }
        val media = Media()
        media.image = tempUri?.let { getPath(it) }
        selectedMediaFiles?.add(media)

    }

    //get file path from storage
    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor? =
            App.INSTANCE.getContentResolver().query(uri, projection, null, null, null)
        return if (cursor != null) {
            // HERE YOU WILL GET A NULL POINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(column_index)
            cursor.close()
            filePath
        } else null
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun setObservables() {

        viewModel.imageUploadSuccess.observe(this) { data ->
            hideProgressBar()
            AppUtil.showToast(data?.message)
            if (AppUtil.isConnection()) {
                showProgressBar()
                val foodRequest = Food()
                foodRequest.status = 1
                foodRequest.isAvailable = 1
                foodRequest.name = etFoodName.text.toString()
                foodRequest.price = etprice.text.toString().toFloat()
                foodRequest.availableQuantity = etQuantity.text.toString().toInt()
                foodRequest.images = data.images
                viewModel.addFoodAPI(foodRequest)
                PreferenceKeeper.getInstance().isLogin = true
            }
        }

        viewModel.addFoodSuccess.observe(this) { data ->
            hideProgressBar()
            AppUtil.showToast(data?.message)
            viewModel.getAllFoodAPI(vendorId)
            finish()
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)
        }
    }

    override fun onclick(v: Int) {
        if (v == 1) {
            pickImageFromGallery()
        } else {
            pickImageFromCamera()
        }
        bottomSheetFragment.dismiss()
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, AppConstant.IMAGE_PICK_CODE)
    }

    private fun pickImageFromCamera() {
        try {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, REQUEST_CODE)
        } catch (e: Exception) {
            println(e.message)

        }
    }

    override fun onStoragePickUp(data: MutableList<Media>?) {
        super.onStoragePickUp(data)
        data?.let {
            if (selectedMediaFiles?.size == 3) {
                AppUtil.showToast("Maximum 3 images can be captured")
                return
            }
            if (selectedMediaFiles?.size ?: 0 > 0) {
                selectedMediaFiles?.addAll(it)
            } else {
                selectedMediaFiles = it
            }
        }
    }

}
