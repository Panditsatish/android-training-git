package com.application.khaokhana.ui.home

import com.application.khaokhana.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import kotlinx.android.synthetic.main.botttomsheet.*

class BottomSheetFragment(var callback:CameraGallery): BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.CustomBottomSheetDialogTheme
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.botttomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtCamera.setOnClickListener {
            callback.onclick(2)
        }
        txtGallery.setOnClickListener {
            callback.onclick(1)
        }
    }

    interface CameraGallery{
       fun onclick(v: Int)
    }

}