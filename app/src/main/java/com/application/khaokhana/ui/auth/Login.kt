package com.application.khaokhana.ui.auth

import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.activity.viewModels
import com.application.khaokhana.R
import com.application.khaokhana.ui.home.DashBoardActivity
import com.application.khaokhana.base.BaseActivity
import com.application.khaokhana.utils.AppUtil
import com.application.khaokhana.utils.PreferenceKeeper
import com.application.khaokhana.viewModel.AuthViewModel
import kotlinx.android.synthetic.main.activity_login.*

class Login : BaseActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun layoutRes(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListener()
        setObservables()
        showPassword()
    }

    private fun setListener() {
        btnLogin.setOnClickListener {
            loginAPI()
        }
    }

    private fun loginAPI() {
        if (AppUtil.isConnection()) {
            val email = edEmail.text.toString()
            val password = etPassword.text.toString()
            if (loginEmailAPI(email, password)) {
                showProgressBar()
                hideSoftKeyBoard()
                viewModel.loginAPI(email, password)
            }
        }
    }

    private fun loginEmailAPI(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            AppUtil.showToast("Email cannot be blank")
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            AppUtil.showToast("Invalid Email")
            return false
        } else if (TextUtils.isEmpty(password)) {
            AppUtil.showToast("Password cannot be blank")
            return false
        } else if (password.length < 6) {
            AppUtil.showToast("Password must contain at least 6 characters")
            return false
        }
        return true
    }


    private fun setObservables() {

        viewModel.loginSuccess.observe(this) { data ->

            AppUtil.showToast(data?.message)
            hideProgressBar()
            PreferenceKeeper.getInstance().isLogin = true
            val useData = data?.employeeProfile
            PreferenceKeeper.getInstance().accessToken = useData?.accessToken
            PreferenceKeeper.getInstance().setUser(useData)
            launchActivity(DashBoardActivity::class.java)
            finish()
        }

        viewModel.error.observe(this) { errors ->
            hideProgressBar()
            AppUtil.showToast(errors.errorMessage)
        }
    }

    private fun showPassword() {
        ivPasswordHide?.setOnClickListener {
            val t = it.tag.toString().toInt()
            if (t == 0) {
                ivPasswordHide.setImageResource(R.drawable.ic_password_show)
                etPassword.transformationMethod = null
                ivPasswordHide.tag = "1"
            } else {
                ivPasswordHide.setImageResource(R.drawable.ic_password_hide)
                etPassword.transformationMethod = PasswordTransformationMethod()
                ivPasswordHide.tag = "0"
            }
        }
    }


}