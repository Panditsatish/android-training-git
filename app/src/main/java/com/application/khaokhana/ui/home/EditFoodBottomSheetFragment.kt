package com.application.khaokhana.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.khaokhana.R
import com.application.khaokhana.model.food.Food
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_edit_food_bottom_sheet.*

class EditFoodBottomSheetFragment(var foodData: Food, var callback: EditDelete) :
    BottomSheetDialogFragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_food_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickListener()
    }


    private fun onClickListener() {

        lytEditFoodItem.setOnClickListener {
            callback.onclick(foodData, 1)
            dismiss()
        }
        lytDeletFood.setOnClickListener {
            callback.onclick(foodData, 2)
            dismiss()
        }

    }

    interface EditDelete {
        fun onclick(foodData: Food?, v: Int)
    }
}