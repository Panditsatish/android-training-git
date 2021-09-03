package com.application.khaokhana.ui.home

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.application.khaokhana.R
import com.application.khaokhana.base.BaseActivity
import com.application.khaokhana.callback.CallbackType
import com.application.khaokhana.callback.RootCallback
import com.application.khaokhana.model.food.Food
import com.application.khaokhana.ui.food.AddFoodActivity
import com.application.khaokhana.ui.food.EditFoodActivity
import com.application.khaokhana.utils.AppConstant
import com.application.khaokhana.utils.AppUtil
import com.application.khaokhana.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_dash_board.*
import kotlinx.android.synthetic.main.activity_delete_food.*
import kotlinx.android.synthetic.main.activity_vendor_profile.*
import kotlinx.android.synthetic.main.food_item_list.*

class DashBoardActivity : BaseActivity(), RootCallback<Any>, SwipeRefreshLayout.OnRefreshListener, EditFoodBottomSheetFragment.EditDelete {

    private var foodAdapter: DashboardAdapter? = null
    private val viewModel: AuthViewModel by viewModels()
    var vendorId: String? = null

    private var foods: MutableList<Food>? = null
    override fun layoutRes(): Int {
        return R.layout.activity_dash_board
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNav()
        setAdapter()
        setObservables()
        setListener()

        if (AppUtil.isConnection()) {
            showProgressBar()
            viewModel.getAllFoodAPI(vendorId)
        }
        if (AppUtil.isConnection()) {
            showProgressBar()
            viewModel.getMyProfile()
        }
    }

    private fun setListener() {

        ivMenu.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }

        ivCross?.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
        }

        ivAddFood.setOnClickListener {
            val intent = Intent(this, AddFoodActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setNav() {
        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }


    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    private fun setAdapter() {
        foodAdapter = DashboardAdapter()
        DashboardRecyclerView.setHasFixedSize(true) //every item of the RecyclerView has a fix size
        DashboardRecyclerView.adapter = foodAdapter
        foodAdapter?.setRootCallback(this as RootCallback<Any>)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFoodAPI(vendorId)
    }

    override fun onRefresh() {
        if (AppUtil.isConnection()) {
            viewModel.getMyProfile()
        }
    }

    private fun setObservables() {

        viewModel.myProfileSuccess.observe(this) { data ->
            tvName?.text = "Hello ${data.profileData?.firstName}"
            tvMenuName?.text = AppUtil.getFullName(data.profileData?.firstName, data.profileData?.lastName)
            tvMenuEmail?.text = data.profileData?.email
            val w = data.profileData?.walletBalance
            tvMenuWallet?.text = w?.let { AppUtil.getRupee(w) }
            tvMenuPhone?.text = data.profileData?.phone
        }

        viewModel.foodListSuccess.observe(this) { data ->
            hideProgressBar()
            foods = data.foodItemList
            foodAdapter?.setData(foods)
        }

        viewModel.foodDeleteSuccess.observe(this) { data ->
            hideProgressBar()
            AppUtil.showToast(data?.message)
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)
        }

    }

    /*var itemCount = 0
    var totalPrice = 0.0f*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQUEST_CODES.FOOD_REQ) {
            val b = data?.extras
            val index = b?.getInt(AppConstant.BK.PAYMENT)
            if (resultCode == Activity.RESULT_OK) {
                if (index == 1 || index == 2) { // 1 for payment done, 2 for cancel, null back pressed
                    if (AppUtil.isConnection()) {
                        showProgressBar()
                        viewModel.getAllFoodAPI(vendorId)
                    }
                }
            }
        }
    }


    override fun onRootCallback(index: Int, data: Any, type: CallbackType, view: View) {
        //super.onRootCallback(index, data, type, view)
        when(type) {
            CallbackType.DASHBOARD_ADAPTER_MENU -> {
                val v=data as Food
                val bottomSheetFragment = EditFoodBottomSheetFragment(v,this@DashBoardActivity)
                bottomSheetFragment.show(supportFragmentManager, "bottomsheetdialog")
            }
        }
    }

    private fun deleteFood(foodId: String?) {
        val dialogbox: Dialog?
        dialogbox = Dialog(this)
        dialogbox.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogbox.setContentView(R.layout.activity_delete_food)
        dialogbox.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogbox.show()
        dialogbox.btnRemove.setOnClickListener {
            //    rootCallback?.onRootCallback(1, data!!, CallbackType.DELETE_FOOD, it)
            if (AppUtil.isConnection()) {
                showProgressBar()
                viewModel.foodDeleteAPI(foodId)
                onResume()
                dialogbox.dismiss()
            }
        }

        dialogbox.ivCancelDeleteFood.setOnClickListener {
            dialogbox.dismiss()
        }
    }

    private fun editFood(foodData: Food?) {
        val bundle = Bundle()
        bundle.putSerializable(AppConstant.BK.FOODS,foodData)
        launchActivity(EditFoodActivity::class.java,bundle)
    }

    override fun onclick(foodData: Food?, v: Int) {
        if (v == 1) {
            editFood(foodData)
        } else {
            val foodId:String? = foodData?.foodId
            deleteFood(foodId)
        } }

}