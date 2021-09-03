package com.application.khaokhana.ui.splash

import android.content.Intent
import android.os.Bundle

import com.application.khaokhana.R
import com.application.khaokhana.ui.auth.Login
import com.application.khaokhana.base.BaseActivity
import com.application.khaokhana.utils.AppConstant
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class splash : BaseActivity() {

  override fun layoutRes(): Int {
        return R.layout.activity_splash
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateActivity()
    }
    private  fun navigateActivity(){
        GlobalScope.launch{
            delay(AppConstant.SPLASH_DELAY)
            gotoScreen()
        }
    }

    private  fun gotoScreen(){
            startActivity(Intent(this, Login::class.java))
        finish()
    }
}