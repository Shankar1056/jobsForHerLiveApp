package com.jobsforher.ui.expert_chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.BR
import com.jobsforher.R
import com.jobsforher.data.model.ExpertChatBody
import com.jobsforher.databinding.ItemExpertChatBinding

class ExpertChatAdapter(
    private val list: ArrayList<ExpertChatBody>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<ExpertChatAdapter.ViewHolder>() {
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_expert_chat, parent, false
        )

        return ViewHolder(binding as ItemExpertChatBinding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val imageModelList = list[position]
        holder.bind(imageModelList, listener)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private var itemRowBinding: ItemExpertChatBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(obj: Any, listener: OnItemClickListener) {
            itemRowBinding.setVariable(BR.model, obj)
            itemRowBinding.executePendingBindings()
            itemRowBinding.viewDetails.setOnClickListener {
                listener.onViewDetailsClicked(adapterPosition)
            }
            itemRowBinding.joinChat.setOnClickListener {
                listener.onJoinClicked(adapterPosition)
            }
        }

    }

    interface OnItemClickListener {
        fun onJoinClicked(pos: Int)
        fun onViewDetailsClicked(pos: Int)
    }

}
