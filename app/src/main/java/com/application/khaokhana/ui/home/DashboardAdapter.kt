package com.application.khaokhana.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.application.khaokhana.R
import com.application.khaokhana.callback.CallbackType
import com.application.khaokhana.callback.RootCallback
import com.application.khaokhana.model.food.Food
import com.application.khaokhana.utils.AppUtil
import com.application.khaokhana.utils.GlideUtils
import kotlinx.android.synthetic.main.food_item_list.view.*

class DashboardAdapter() : RecyclerView.Adapter<DashboardAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.food_item_list, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val data = products?.get(position)
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return products?.size ?: 0
    }

    private var rootCallback: RootCallback<Any>? = null

    fun setRootCallback(rootCallback: RootCallback<Any>) {
        this.rootCallback = rootCallback
    }

    private var products: MutableList<Food>? = null
    fun setData(products: MutableList<Food>?) {
        this.products = products
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: View) :
        RecyclerView.ViewHolder(binding.rootView) {
        fun bind(data: Food?, position: Int) {


            if (data?.images != null && data.images?.size ?: 0 > 0) {
                GlideUtils.loadImageFullWidth(binding.ivFoodItem, data.images?.get(0))
            }

            if (data != null) {
                binding.tvRate.text = AppUtil.getRupee(data.price)
            }
            binding.tvItemName.text = data?.name

            binding.ivIcon.setOnClickListener {
                AppUtil.preventTwoClick(it)
                if (data != null) {
                    rootCallback?.onRootCallback(
                        position, data,
                        CallbackType.DASHBOARD_ADAPTER_MENU, it
                    )
                }
            }

        }
    }
}

