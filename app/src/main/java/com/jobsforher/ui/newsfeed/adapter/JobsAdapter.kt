package com.jobsforher.ui.newsfeed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.BR
import com.jobsforher.R
import com.jobsforher.data.model.RecommendedJobsBody
import com.jobsforher.databinding.ItemJobsBinding

class JobsAdapter(
    private val list: ArrayList<RecommendedJobsBody>,
    private val listener: JobsItemClicked
) :
    RecyclerView.Adapter<JobsAdapter.ViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_jobs, parent, false
        )

        return ViewHolder(binding as ItemJobsBinding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val imageModelList = list[position]
        holder.bind(imageModelList)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private var itemRowBinding: ItemJobsBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(obj: Any) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
            itemRowBinding.btnApplied.setOnClickListener {
                listener.onApplyClicked(adapterPosition)
            }
        }
    }

    interface JobsItemClicked {
        fun onApplyClicked(pos: Int)
    }

}
