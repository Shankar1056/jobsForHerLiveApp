package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jobsforher.R
import com.jobsforher.models.GroupsView

class ZProfileAdapter(private val mDataList: ArrayList<GroupsView>) : RecyclerView.Adapter<ZProfileAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.zactivity_main, parent, false)
//        mygroups_row
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_name.text = mDataList[position].label
        holder.row_noOfMembers.text = mDataList[position].noOfMembers
        holder.row_description.text = mDataList[position].description
        holder.row_location.text = mDataList[position].location

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_name: TextView
        internal var row_label: TextView
        internal var row_noOfMembers: TextView
        internal var row_description:TextView
        internal var row_location: TextView

        init {
            row_name = itemView.findViewById<View>(R.id.row_name) as TextView
            row_label = itemView.findViewById<View>(R.id.row_label) as TextView
            row_noOfMembers = itemView.findViewById<View>(R.id.row_noOfMembers) as TextView
            row_description = itemView.findViewById<View>(R.id.row_description) as TextView
            row_location = itemView.findViewById<View>(R.id.row_location) as TextView
        }
    }
}