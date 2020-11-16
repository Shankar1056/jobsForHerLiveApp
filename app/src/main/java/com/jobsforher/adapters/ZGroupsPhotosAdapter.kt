package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.jobsforher.R

class ZGroupsPhotosAdapter(private val mDataList: ArrayList<String>) : RecyclerView.Adapter<ZGroupsPhotosAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.zgroups_photos_adapter_row, parent, false)

        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

//        Log.d("TAGG", mDataList[position].toString())
        if(mDataList[position].isNotEmpty()) {
            Picasso.with(context)
                .load(mDataList[position].toString())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_imageview)
        }else{
            Picasso.with(context)
                .load(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_imageview)
        }

    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal lateinit var row_imageview: ImageView

        init {
            row_imageview = itemView.findViewById<View>(R.id.row_image_adapter) as ImageView
        }
    }
}