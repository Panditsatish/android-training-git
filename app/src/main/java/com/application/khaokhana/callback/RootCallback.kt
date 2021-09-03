package com.application.khaokhana.callback

import android.view.View

interface RootCallback<T> {
    fun onRootCallback(index: Int, data: T, type: CallbackType, view: View){}
    fun onRootCallback(type: CallbackType) {}

}