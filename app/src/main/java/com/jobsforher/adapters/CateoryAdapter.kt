package com.jobsforher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.jobsforher.R
import com.jobsforher.models.CategoriesMainView
import com.jobsforher.activities.GroupsCategoryPage


class CateoryAdapter(private val mDataList: ArrayList<CategoriesMainView>, val mType:Int) : RecyclerView.Adapter<CateoryAdapter.MyViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.category_row, parent, false)
        if(mType==1){
            view = LayoutInflater.from(parent.context).inflate(R.layout.category_row, parent, false)
        }
        else if(mType==2){
            view = LayoutInflater.from(parent.context).inflate(R.layout.groups_row, parent, false)
        }
        this.context = parent.context;
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.row_text.text = mDataList[position].name

        if (mDataList[position].image_url!!.length>0) {

            Picasso.with(context)
                .load(mDataList[position].image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_image)

        }else{

            Picasso.with(context)
                .load(R.drawable.ic_launcher)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.row_image)
        }

        holder.row_layout.setOnClickListener {
            (context as GroupsCategoryPage).sendData(mDataList[position].id.toString())


        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var row_text: TextView
        internal lateinit var row_image: ImageView
        internal  var row_layout: LinearLayout

        init {
            row_text = itemView.findViewById<View>(R.id.row_text) as TextView
            row_image = itemView.findViewById<View>(R.id.row_image) as ImageView
            row_layout = itemView.findViewById<View>(R.id.mainlayout) as LinearLayout
        }
    }
}