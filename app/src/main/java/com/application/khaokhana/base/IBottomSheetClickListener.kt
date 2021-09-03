package com.application.khaokhana.base

import com.application.khaokhana.model.Media


interface IBottomSheetClickListener {
    fun onBottomSheetItemClicked(
        position: Int,
        type: BottomSheetType?,
        data: Media?
    )
}