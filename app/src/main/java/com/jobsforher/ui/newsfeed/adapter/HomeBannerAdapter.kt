package com.jobsforher.ui.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.BR
import com.jobsforher.R
import com.jobsforher.data.model.HomeBannerData
import com.jobsforher.databinding.ItemHomeBannerBinding

class HomeBannerAdapter(private val list: ArrayList<HomeBannerData>) :
    RecyclerView.Adapter<HomeBannerAdapter.ViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_home_banner, parent, false
        )

        return ViewHolder(binding as ItemHomeBannerBinding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val imageModelList = list[position]
        holder.bind(imageModelList)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private var itemRowBinding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(obj: Any) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
        }
    }

}
