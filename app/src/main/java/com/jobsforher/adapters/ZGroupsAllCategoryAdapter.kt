package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.jobsforher.R
import com.jobsforher.models.Groups

class ZGroupsAllCategoryAdapter(private val mDataList: ArrayList<Groups>) : RecyclerView.Adapter<ZGroupsAllCategoryAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.zgroups_allcategory_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_text.text = mDataList[position].text
        Picasso.with(context)
            .load("http://cdn.journaldev.com/wp-content/uploads/2016/11/android-image-picker-project-structure.png")
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.row_image)

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_text: TextView
        internal lateinit var row_image: ImageView

        init {
            row_text = itemView.findViewById<View>(R.id.row_text) as TextView
            row_image = itemView.findViewById<View>(R.id.row_image) as ImageView
        }
    }
}