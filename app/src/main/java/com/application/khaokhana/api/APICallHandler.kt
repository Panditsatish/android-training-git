package com.application.khaokhana.api

import com.application.khaokhana.model.base.Errors

interface APICallHandler<T> {

    fun onSuccess(apiType: APIType, response: T?)

    fun onFailure(apiType: APIType, error: Errors)
}