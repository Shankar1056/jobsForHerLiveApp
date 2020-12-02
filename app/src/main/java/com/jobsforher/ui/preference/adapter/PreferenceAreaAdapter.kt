package com.jobsforher.ui.preference.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.BR
import com.jobsforher.R
import com.jobsforher.data.model.PreferenceFunctionalAreaBody
import com.jobsforher.databinding.ItemFunctionalAreaBinding

class PreferenceAreaAdapter(
    private val list: ArrayList<PreferenceFunctionalAreaBody>,
    val listener: CityItemClick
) :
    RecyclerView.Adapter<PreferenceAreaAdapter.ViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ViewHolder {


        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_functional_area, parent, false
        )
        return ViewHolder(binding as ItemFunctionalAreaBinding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val imageModelList = list[position]
        holder.bind(imageModelList)

        holder.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private var itemRowBinding: ItemFunctionalAreaBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(obj: Any) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
        }
    }

    interface CityItemClick {
        fun onItemClicked(position: Int)
    }

}